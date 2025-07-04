package com.student.webproject;

import org.mybatis.spring.annotation.MapperScan; // <-- 确保 import 了这个
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({
    "com.student.webproject.activity.mapper",
    "com.student.webproject.admin.mapper",
    "com.student.webproject.news",
    "com.student.webproject.user.mapper",
        "com.student.webproject.organization.mapper"
}) // 同时扫描activity和admin的mapper
public class WebProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebProjectApplication.class, args);
    }
}