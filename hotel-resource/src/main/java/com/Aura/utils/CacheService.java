package com.Aura.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class CacheService {

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private RedissonClient redisson;

    private Random random = new Random();

    // 存缓存（逻辑过期 + 随机偏移防雪崩）
    public void set(String key, Object data, long ttlSeconds) {
        try {
            if (data == null) {
                redis.opsForValue().set(key, "NULL", ttlSeconds, TimeUnit.SECONDS);
                return;
            }
            long extra = (random.nextInt(30) + 1) * 60 * 1000;
            long expire = System.currentTimeMillis() + ttlSeconds * 1000 + extra;

            JSONObject wrapper = new JSONObject();
            wrapper.put("d", JSON.toJSONString(data));
            wrapper.put("e", expire);
            redis.opsForValue().set(key, wrapper.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 取缓存，过期也返回
    public <T> T get(String key, Class<T> type) {
        try {
            String json = redis.opsForValue().get(key);
            if (json == null || "NULL".equals(json)) return null;
            JSONObject wrapper = JSON.parseObject(json);
            return JSON.parseObject(wrapper.getString("d"), type);
        } catch (Exception e) {
            return null;
        }
    }

    // 取缓存（列表）
    public <T> List<T> getList(String key, Class<T> elementType) {
        try {
            String json = redis.opsForValue().get(key);
            if (json == null || "NULL".equals(json)) return null;
            JSONObject wrapper = JSON.parseObject(json);
            return JSON.parseArray(wrapper.getString("d"), elementType);
        } catch (Exception e) {
            return null;
        }
    }

    // 检查是否需要刷新
    public boolean needRefresh(String key) {
        try {
            String json = redis.opsForValue().get(key);
            if (json == null || "NULL".equals(json)) return true;
            JSONObject wrapper = JSON.parseObject(json);
            return System.currentTimeMillis() > wrapper.getLong("e");
        } catch (Exception e) {
            return true;
        }
    }

    // 获取缓存对应的写锁（防缓存击穿）
    public RLock getLock(String cacheKey) {
        return redisson.getReadWriteLock("lock:" + cacheKey).writeLock();
    }

    // 删缓存
    public void del(String... keys) {
        if (keys != null && keys.length > 0) {
            redis.delete(Arrays.asList(keys));
        }
    }

    // 通配符删
    public void delPattern(String pattern) {
        Set<String> keys = redis.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redis.delete(keys);
        }
    }
}
