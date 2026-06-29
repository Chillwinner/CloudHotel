package com.Aura.utils;

import org.springframework.util.DigestUtils;

public class MD5Util {
    // 纯 MD5 加密
    public static String encrypt(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
}