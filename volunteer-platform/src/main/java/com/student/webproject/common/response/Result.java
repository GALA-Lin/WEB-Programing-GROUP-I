package com.student.webproject.common.response;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    // 私有化构造函数，强制使用静态工厂方法创建实例
    private Result() {}

    // --- 成功的静态方法 ---
    public static <T> Result<T> success(T data, String message) {
        Result<T> result = new Result<>();
        result.setCode(200); // 默认成功码 200
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success(T data) {
        return success(data, "操作成功");
    }

    public static Result<?> created(Object data) {
        Result<Object> result = new Result<>();
        result.setCode(201); // 资源创建成功码 201
        result.setMessage("创建成功");
        result.setData(data);
        return result;
    }

    // --- 失败的静态方法 ---
    public static Result<?> error(Integer code, String message) {
        Result<?> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }
}