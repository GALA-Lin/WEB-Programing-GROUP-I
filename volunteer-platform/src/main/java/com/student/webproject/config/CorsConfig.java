package com.student.webproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // 允许所有路径的跨域请求
                registry.addMapping("/**")
                        // 允许来自任何源的请求 (在开发环境中可以这样设置)
                        .allowedOriginPatterns("*")
                        // 允许所有常见的HTTP方法
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        // 允许携带凭证 (如Cookie)
                        .allowCredentials(true)
                        // 允许所有请求头
                        .allowedHeaders("*")
                        // 预检请求的有效期
                        .maxAge(3600);
            }
        };
    }
}