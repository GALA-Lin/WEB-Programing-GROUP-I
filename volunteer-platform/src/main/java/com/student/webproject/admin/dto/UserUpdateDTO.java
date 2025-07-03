package com.student.webproject.admin.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String realName;
    private String email;
    private String phoneNumber;
    private String role;
    private Integer status; // 1 for active, 0 for disabled
}