package com.student.webproject.user.dto;

import lombok.Data;
import java.math.BigDecimal; // 1. 导入 BigDecimal

@Data
public class UserDetailDTO {
    private Long id; // 2. 类型改为 Long
    private String username;
    private String realName;
    private String email;
    private String phoneNumber;
    private String studentId;
    private String avatarUrl;
    private String role;

    // 3. 【核心修复】补上总志愿时长字段，类型为 BigDecimal
    private BigDecimal totalServiceHours;
}