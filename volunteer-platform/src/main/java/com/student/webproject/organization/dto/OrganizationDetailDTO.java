package com.student.webproject.organization.dto;
import lombok.Data;
import java.util.List;

@Data
public class OrganizationDetailDTO {
    private Long id;
    private String name;
    private String description;
    private String leaderName;
    private Long totalMembers;
    private Boolean isMember;
    private List<MemberDTO> members;
}