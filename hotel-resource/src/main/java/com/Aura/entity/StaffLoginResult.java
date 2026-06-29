package com.Aura.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StaffLoginResult {
    private String token;
    private String name;
    private Integer role;
    private Long hotelId;
}
