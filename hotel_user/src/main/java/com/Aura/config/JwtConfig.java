package com.Aura.config;

import com.Aura.utils.JwtUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        JwtUtil.setSecretKey(secretKey);
    }
}
