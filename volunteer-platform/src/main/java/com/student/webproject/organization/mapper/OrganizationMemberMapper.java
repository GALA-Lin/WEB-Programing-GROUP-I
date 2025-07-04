package com.student.webproject.organization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.student.webproject.organization.entity.OrganizationMember;
import com.student.webproject.organization.dto.MemberDTO;
import com.student.webproject.organization.dto.PendingApprovalDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface OrganizationMemberMapper extends BaseMapper<OrganizationMember> {
    @Select("SELECT om.id as membershipId, u.id as userId, u.real_name as realName, u.avatar_url as avatarUrl, " +
            "om.member_role as memberRole, DATE_FORMAT(om.join_date, '%Y-%m-%d') as joinDate " +
            "FROM organization_members om JOIN users u ON om.user_id = u.id " +
            "WHERE om.organization_id = #{organizationId}")
    List<MemberDTO> selectMembersByOrganizationId(Long organizationId);

    @Select("SELECT om.id as membershipId, u.real_name as realName, u.student_id as studentId, " +
            "DATE_FORMAT(om.join_date, '%Y-%m-%d') as applyDate " +
            "FROM organization_members om JOIN users u ON om.user_id = u.id " +
            "WHERE om.organization_id = #{organizationId} AND om.status = 'pending'")
    List<PendingApprovalDTO> selectPendingApprovalsByOrganizationId(Long organizationId);
}