package com.Aura.Mapper;

import com.Aura.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    
    // 1. 通过手机号查询 (用于登录校验和判断注册重复)
    User selectByPhone(String phone);

    // 2. 根据邮箱查询 (注册时校验邮箱唯一)
    User selectByEmail(String email);

    // 3. 根据ID查询 (对应 getUserInfo)
    User selectById(Long id);

    // 4. 插入新用户 (注册)
    int insertUser(User user);

    // 5. 更新用户信息 (对应 updateUser)
    int updateById(User user);

    List<User> selectUserList(String keyword);

    int updateUserStatus(Long id, Integer status);
}