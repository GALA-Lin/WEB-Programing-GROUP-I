package com.student.webproject.user.dto;

import lombok.Data;

@Data
public class UserDetailDTO {
    private Integer id;
    private String username;
    private String realName;
    private String email;
    private String phoneNumber;
    private String studentId;
    private String avatarUrl;
    private String role;
    private Double totalServiceHours; // API文档要求包含此字段
}