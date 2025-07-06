package com.student.webproject.user.dto;

import lombok.Data;
import java.math.BigDecimal; // 1. 导入 BigDecimal

@Data
public class UserDetailDTO {
    private Long id;
    private String username;
    private String realName;
    private String email;
    private String phoneNumber;
    private String studentId;
    private String avatarUrl;
    private String role;
    private BigDecimal totalServiceHours;
}