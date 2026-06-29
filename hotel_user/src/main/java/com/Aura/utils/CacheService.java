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

/**
 * Redis 缓存服务（逻辑过期 + 分布式锁）
 *
 * 存储格式: {"d": "实际数据JSON", "e": 逻辑过期时间戳}
 * - data=null 时存 "NULL" 占位，防止缓存穿透
 * - extra: 随机 1~30 分钟，防止缓存雪崩
 * - Redis 不设 TTL，靠逻辑过期时间判断是否需要刷新
 */
@Service
public class CacheService {

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private RedissonClient redisson;

    private Random random = new Random();

    /** 写缓存：数据为 null 存 "NULL" 占位，否则存逻辑过期时间 + 随机偏移 */
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

    /** 读缓存：返回反序列化后的对象，null 或 "NULL" 都返回 null */
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

    /** 读缓存（列表）：返回反序列化后的 List */
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

    /** 判断是否需要刷新：逻辑过期时间已过则返回 true */
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

    /** 获取 Redisson 读写锁的写锁（用于缓存刷新时加锁） */
    public RLock getLock(String cacheKey) {
        return redisson.getReadWriteLock("lock:" + cacheKey).writeLock();
    }

    /** 删除指定 key */
    public void del(String... keys) {
        if (keys != null && keys.length > 0) {
            redis.delete(Arrays.asList(keys));
        }
    }

    /** 按通配符批量删除 key */
    public void delPattern(String pattern) {
        Set<String> keys = redis.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redis.delete(keys);
        }
    }
}
