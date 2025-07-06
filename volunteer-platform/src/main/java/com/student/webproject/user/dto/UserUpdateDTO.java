package com.student.webproject.user.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String realName;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
}