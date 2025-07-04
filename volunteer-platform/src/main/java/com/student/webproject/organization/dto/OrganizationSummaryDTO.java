package com.student.webproject.organization.dto;
import lombok.Data;

@Data
public class OrganizationSummaryDTO {
    private Long id;
    private String name;
    private String description;
    private String leaderName;
    private Long totalMembers;
}