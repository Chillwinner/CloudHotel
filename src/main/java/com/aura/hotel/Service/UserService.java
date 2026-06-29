package com.aura.hotel.Service;

import com.aura.hotel.Mapper.UserMapper;
import com.aura.hotel.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    // 注册：判断手机号/邮箱是否已存在
    public String register(User user) {
        if (user.getPhone() == null || user.getPhone().isEmpty()) return "手机号不能为空";
        if (user.getPassword() == null || user.getPassword().isEmpty()) return "密码不能为空";
        if (user.getEmail() == null || user.getEmail().isEmpty()) return "邮箱不能为空";
        User exist = userMapper.selectByPhone(user.getPhone());
        if (exist != null) return "该手机号已被注册";
        User emailExist = userMapper.selectByEmail(user.getEmail());
        if (emailExist != null) return "邮箱已被使用";
        userMapper.insertUser(user);
        return "注册成功";
    }

    public User getUserInfo(Long id) {
        return userMapper.selectById(id);
    }

    public void updateUserInfo(User user) {
        userMapper.updateById(user);
    }
}