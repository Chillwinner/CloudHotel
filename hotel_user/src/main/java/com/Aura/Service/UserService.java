package com.Aura.Service;

import com.Aura.Mapper.UserMapper;
import com.Aura.entity.User;
import com.Aura.utils.CacheService;
import com.Aura.utils.MD5Util;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CacheService cache;

    // 注册（手机号、密码、邮箱必填）
    public String register(User user) {
        if (user.getPhone() == null || user.getPhone().isEmpty()) return "手机号不能为空";
        if (user.getPassword() == null || user.getPassword().isEmpty()) return "密码不能为空";
        if (user.getEmail() == null || user.getEmail().isEmpty()) return "邮箱不能为空";

        User exist = userMapper.selectByPhone(user.getPhone());
        if (exist != null) return "手机号已注册";

        User emailExist = userMapper.selectByEmail(user.getEmail());
        if (emailExist != null) return "邮箱已被使用";

        user.setPassword(MD5Util.encrypt(user.getPassword()));
        userMapper.insertUser(user);
        cache.delPattern("user:list:*");
        return "注册成功";
    }

    // 登录（只需要手机号和密码）
    public User login(String phone, String password) {
        User user = userMapper.selectByPhone(phone);
        if (user == null) return null;
        if (!user.getPassword().equals(MD5Util.encrypt(password))) return null;
        return user;
    }

    // 查用户信息
    public User getUserInfo(Long id) {
        String key = "user:info:" + id;
        User u = cache.get(key, User.class);
        if (u != null && !cache.needRefresh(key)) return u;

        RLock lock = cache.getLock(key);
        lock.lock();
        try {
            if (cache.needRefresh(key)) {
                u = userMapper.selectById(id);
                cache.set(key, u, 3600);
            } else {
                u = cache.get(key, User.class);
            }
            return u;
        } finally {
            lock.unlock();
        }
    }

    // 修改用户
    public void updateUserInfo(User user) {
        userMapper.updateById(user);
        cache.del("user:info:" + user.getId());
        cache.delPattern("user:list:*");
    }

    // 查用户列表
    public List<User> getUserList(String keyword) {
        String key = "user:list:" + (keyword != null ? keyword : "");
        List<User> list = cache.getList(key, User.class);
        if (list != null && !cache.needRefresh(key)) return list;

        RLock lock = cache.getLock(key);
        lock.lock();
        try {
            if (cache.needRefresh(key)) {
                list = userMapper.selectUserList(keyword);
                cache.set(key, list, 1800);
            } else {
                list = cache.getList(key, User.class);
            }
            return list;
        } finally {
            lock.unlock();
        }
    }

    // 封禁/解封
    public void changeUserStatus(Long userId, Integer status) {
        User exist = userMapper.selectById(userId);
        if (exist == null) throw new RuntimeException("用户不存在");
        userMapper.updateUserStatus(userId, status);
        cache.del("user:info:" + userId);
        cache.delPattern("user:list:*");
    }
}
