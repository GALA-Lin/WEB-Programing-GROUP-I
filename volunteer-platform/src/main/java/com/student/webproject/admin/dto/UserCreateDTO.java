package com.student.webproject.admin.dto;

import lombok.Data;

@Data
public class UserCreateDTO {
    private String username;
    private String password;
    private String realName;
    private String studentId;
    private String email;
    private String role; // e.g., "volunteer", "admin"
}