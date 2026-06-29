package com.aura.hotel.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Staff {
    private Long id;
    private String name;
    private String phone;
    private Integer gender;   // 1-男, 2-女
    private Long hotelId;
    private Integer role;     // 1-店长, 2-经理, 3-员工
    private Integer status;   // 1-在职, 0-离职
    private LocalDateTime createTime; // 对应数据库自动生成的时刻
}