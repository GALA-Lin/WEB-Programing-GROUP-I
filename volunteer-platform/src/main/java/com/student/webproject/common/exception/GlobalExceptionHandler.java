package com.student.webproject.common.exception;

import com.student.webproject.common.response.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 捕获所有运行时异常
     * @param e 捕获到的异常对象
     * @return 统一的错误响应
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 设置HTTP状态码为 400
    public Result<?> handleRuntimeException(RuntimeException e) {
        // e.getMessage() 将会获取我们在 Service 中抛出异常时的提示信息
        return Result.error(400, e.getMessage());
    }
}
