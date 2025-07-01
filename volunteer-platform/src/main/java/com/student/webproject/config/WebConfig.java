package com.student.webproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许所有路径进行跨域
                .allowedOrigins("http://localhost:5173") // 允许这个源(你的Vue前端地址)跨域
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的请求方法
                .allowedHeaders("*") // 允许所有的请求头
                .allowCredentials(true) // 是否允许发送Cookie
                .maxAge(3600); // 预检请求的有效期，单位为秒
    }
}