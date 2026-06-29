package com.Aura.utils;

import org.springframework.util.DigestUtils;

public class MD5Util {
    public static String encrypt(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
}