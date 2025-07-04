package com.student.webproject.organization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("organization_members")
public class OrganizationMember {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long organizationId;
    private LocalDateTime joinDate;
    private String memberRole;
    private String status; // 'pending', 'approved'
}