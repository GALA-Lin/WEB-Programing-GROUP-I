package com.student.webproject.common;

import java.util.Collections;
import java.util.Map;

/**
 * 全局API响应封装类
 * @param <T> data字段的数据类型
 */
public class Result<T> {
    private int code;
    private String message;
    private T data;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // --- 静态工厂方法 ---

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    public static <T> Result<T> success(T data) {
        return success("操作成功", data);
    }

    public static Result<?> successMessage(String message) {
        return success(message, null);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    // --- Getters ---
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}