package com.student.webproject;

import org.mybatis.spring.annotation.MapperScan; // <-- 确保 import 了这个
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({
        "com.student.webproject.**.mapper",
        "com.student.webproject.news"
})
public class WebProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebProjectApplication.class, args);
    }
}