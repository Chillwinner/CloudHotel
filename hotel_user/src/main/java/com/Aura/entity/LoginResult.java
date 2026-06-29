package com.Aura.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResult {
    private String token;
    private Long userId;
    private String nickname;
}
