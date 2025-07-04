package com.student.webproject.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.student.webproject.common.response.Result;
import com.student.webproject.organization.dto.*;
import com.student.webproject.organization.entity.Organization;
import com.student.webproject.organization.entity.OrganizationMember;
import com.student.webproject.organization.mapper.OrganizationMapper;
import com.student.webproject.organization.mapper.OrganizationMemberMapper;
import com.student.webproject.organization.service.OrganizationService;
import com.student.webproject.user.Entity.User;
import com.student.webproject.user.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final UserMapper userMapper;
    private final OrganizationMapper organizationMapper;
    private final OrganizationMemberMapper memberMapper;

    @Override
    public Result<List<OrganizationSummaryDTO>> listOrganizations() {
        return Result.success(organizationMapper.selectOrganizationSummaries());
    }

    @Override
    public Result<OrganizationDetailDTO> getOrganizationDetails(Long id, Principal principal) {
        Organization org = organizationMapper.selectById(id);
        if (org == null) throw new RuntimeException("组织不存在");

        User leader = userMapper.selectById(org.getLeaderId());
        User currentUser = userMapper.selectOne(new QueryWrapper<User>().eq("username", principal.getName()));

        OrganizationMember membership = memberMapper.selectOne(new QueryWrapper<OrganizationMember>()
                .eq("user_id", currentUser.getId()).eq("organization_id", id));

        OrganizationDetailDTO dto = new OrganizationDetailDTO();
        dto.setId(org.getId());
        dto.setName(org.getName());
        dto.setDescription(org.getDescription());
        dto.setLeaderName(leader != null ? leader.getRealName() : "N/A");
        dto.setMembers(memberMapper.selectMembersByOrganizationId(id));
        dto.setTotalMembers((long) dto.getMembers().size());
        dto.setIsMember(membership != null && "approved".equals(membership.getStatus()));

        return Result.success(dto);
    }

    @Override
    public Result<Void> applyToOrganization(Long orgId, Principal principal) {
        User currentUser = userMapper.selectOne(new QueryWrapper<User>().eq("username", principal.getName()));
        if (memberMapper.selectOne(new QueryWrapper<OrganizationMember>().eq("user_id", currentUser.getId()).eq("organization_id", orgId)) != null) {
            throw new RuntimeException("您已申请或已加入该组织");
        }
        OrganizationMember newMember = new OrganizationMember();
        newMember.setUserId(currentUser.getId());
        newMember.setOrganizationId(orgId);
        newMember.setStatus("pending"); // 申请状态
        newMember.setJoinDate(LocalDateTime.now());
        memberMapper.insert(newMember);
        return Result.success(null, "申请已提交，等待管理员审批");
    }

    @Override
    public Result<Void> leaveOrganization(Long orgId, Principal principal) {
        User currentUser = userMapper.selectOne(new QueryWrapper<User>().eq("username", principal.getName()));
        memberMapper.delete(new QueryWrapper<OrganizationMember>().eq("user_id", currentUser.getId()).eq("organization_id", orgId));
        return Result.success(null, "已成功退出组织");
    }

    @Override
    public Result<List<PendingApprovalDTO>> listPendingApprovals(Long orgId, Principal principal) {
        checkAdminOrLeaderPermission(orgId, principal);
        return Result.success(memberMapper.selectPendingApprovalsByOrganizationId(orgId));
    }

    @Override
    public Result<Void> approveApplication(Long membershipId, Principal principal) {
        OrganizationMember member = memberMapper.selectById(membershipId);
        if (member == null) throw new RuntimeException("申请记录不存在");
        checkAdminOrLeaderPermission(member.getOrganizationId(), principal);

        member.setStatus("approved");
        member.setMemberRole("member");
        memberMapper.updateById(member);
        return Result.success(null, "已批准该用户加入");
    }

    @Override
    public Result<Void> rejectOrRemoveMember(Long membershipId, Principal principal) {
        OrganizationMember member = memberMapper.selectById(membershipId);
        if (member == null) throw new RuntimeException("成员或申请记录不存在");
        checkAdminOrLeaderPermission(member.getOrganizationId(), principal);
        memberMapper.deleteById(membershipId);
        return Result.success(null, "已拒绝申请或移除成员");
    }

    private void checkAdminOrLeaderPermission(Long orgId, Principal principal) {
        User operator = userMapper.selectOne(new QueryWrapper<User>().eq("username", principal.getName()));
        Organization org = organizationMapper.selectById(orgId);
        boolean isSuperAdmin = "super_admin".equals(operator.getRole());
        boolean isLeader = Objects.equals(operator.getId(), org.getLeaderId());
        if (!isSuperAdmin && !isLeader) {
            throw new RuntimeException("权限不足，只有超级管理员或组织负责人可以操作");
        }
    }
}