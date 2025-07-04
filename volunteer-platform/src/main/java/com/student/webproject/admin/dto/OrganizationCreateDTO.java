package com.student.webproject.admin.dto;

import lombok.Data;

@Data
public class OrganizationCreateDTO {
    private String name;
    private String description;
    private Long leaderId;
}