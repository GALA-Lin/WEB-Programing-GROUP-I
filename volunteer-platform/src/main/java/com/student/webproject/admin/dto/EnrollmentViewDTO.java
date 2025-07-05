package com.student.webproject.admin.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EnrollmentViewDTO {
    private Long userId;
    private String realName;
    private String studentId;
    private String phoneNumber;
    private LocalDateTime enrolledAt;
}