package com.Aura.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Staff {
    private Long id;
    private String name;
    private String phone;
    private String password;  // 【新增】登录密码
    private Integer gender;   // 1-男, 2-女
    private Long hotelId;
    private Integer role;     // 1-店长, 2-经理, 3-员工
    private Integer status;   // 1-在职, 0-离职
    private LocalDateTime createTime;
}