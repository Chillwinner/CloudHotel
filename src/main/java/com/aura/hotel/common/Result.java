package com.aura.hotel.common;

import lombok.Data;
import java.io.Serializable;

/**
 * 统一后端返回格式
 * @param <T> 数据载体类型
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer code;    // 状态码 (200:成功, 500:错误, 401:无权等)
    private String message;  // 提示信息
    private T data;          // 具体数据

    // 私有化构造，通过静态方法调用
    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // --- 成功返回 ---
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    // --- 失败返回 ---
    public static <T> Result<T> error() {
        return new Result<>(500, "服务器内部错误", null);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }

    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}