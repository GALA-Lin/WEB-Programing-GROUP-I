package com.student.webproject.organization.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrganizationAdminViewDTO {
    private Long id;
    private String name;
    private String description;
    private Long leaderId;
    private String leaderName; // 新增负责人姓名字段
    private LocalDateTime updatedAt;
}