package com.student.webproject.organization.dto;
import lombok.Data;

@Data
public class MemberDTO {
    private Long membershipId;
    private Long userId;
    private String realName;
    private String avatarUrl;
    private String memberRole; // 'member', 'officer'
    private String joinDate;
}