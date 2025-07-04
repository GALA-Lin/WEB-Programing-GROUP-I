package com.student.webproject.organization.dto;
import lombok.Data;

@Data
public class PendingApprovalDTO {
    private Long membershipId;
    private String realName;
    private String studentId;
    private String applyDate;
}