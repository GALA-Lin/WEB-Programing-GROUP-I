// 文件路径: src/main/java/com/student/webproject/common/response/Result.java
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


    // --- 【修改部分】资源创建成功的静态方法 (HTTP 201) ---
    /**
     * 创建成功，可以自定义消息
     * @param data 创建成功后返回的数据
     * @param message 自定义的成功消息
     */
    public static <T> Result<T> created(T data, String message) {
        Result<T> result = new Result<>();
        result.setCode(201);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 创建成功，使用默认消息
     * @param data 创建成功后返回的数据
     */
    public static <T> Result<T> created(T data) {
        return created(data, "创建成功");
    }


    // --- 失败的静态方法 ---
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }
}