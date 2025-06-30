package com.student.webproject;

import org.mybatis.spring.annotation.MapperScan; // <-- 确保 import 了这个
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.student.webproject") // <-- 检查点：这里的包名是否完全正确？
public class WebProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebProjectApplication.class, args);
    }
}