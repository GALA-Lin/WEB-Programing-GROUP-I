package com.student.webproject.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 启用 Spring Security 的 Web 安全支持
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 我们之前配置的密码加密器，保持不变
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // --- 这是本次修改的核心 ---

        // 1. 关闭 CSRF 防护
        // 因为我们使用 JWT，是无状态的，所以不需要 CSRF 防护
        http.csrf(csrf -> csrf.disable());

        // 2. 配置 Session 管理策略为无状态 (STATELESS)
        // 同样因为我们使用 JWT
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 3. 配置接口的访问权限 (最关键的部分)
        http.authorizeHttpRequests(authz -> authz
                // 为注册和登录接口配置白名单，允许所有用户访问
                .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                // 除了上面配置的白名单接口，其他所有接口都需要认证（登录）后才能访问
                .anyRequest().authenticated()
        );

        return http.build();
    }
}