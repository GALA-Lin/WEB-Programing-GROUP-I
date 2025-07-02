package com.student.webproject.user.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    // 用户可以只提供想要修改的字段
    private String realName;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
}