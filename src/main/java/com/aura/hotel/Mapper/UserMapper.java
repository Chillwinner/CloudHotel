package com.aura.hotel.Mapper;

import com.aura.hotel.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    
    // 1. 通过手机号查询 (用于登录校验和判断注册重复)
    User selectByPhone(String phone);

    // 通过邮箱查询 (用于注册判重)
    User selectByEmail(String email);

    // 2. 根据ID查询 (对应 getUserInfo)
    User selectById(Long id);

    // 3. 插入新用户 (注册)
    int insertUser(User user);

    // 4. 更新用户信息 (对应 updateUser)
    int updateById(User user);
}