package com.student.webproject.organization.controller;

import com.student.webproject.common.response.Result;
import com.student.webproject.organization.dto.OrganizationDetailDTO;
import com.student.webproject.organization.dto.OrganizationSummaryDTO;
import com.student.webproject.organization.dto.PendingApprovalDTO;
import com.student.webproject.organization.service.OrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api") // 根路径
@AllArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    // --- 公开接口 ---
    @GetMapping("/organizations")
    public Result<List<OrganizationSummaryDTO>> listOrganizations() {
        return organizationService.listOrganizations();
    }

    @GetMapping("/organizations/{id}")
    public Result<OrganizationDetailDTO> getOrganizationDetails(@PathVariable Long id, Principal principal) {
        return organizationService.getOrganizationDetails(id, principal);
    }

    // --- 用户操作接口 (需要登录) ---
    @PostMapping("/organizations/{id}/apply")
    public Result<Void> applyToOrganization(@PathVariable("id") Long orgId, Principal principal) {
        return organizationService.applyToOrganization(orgId, principal);
    }

    @DeleteMapping("/organizations/{id}/leave")
    public Result<Void> leaveOrganization(@PathVariable("id") Long orgId, Principal principal) {
        return organizationService.leaveOrganization(orgId, principal);
    }

    // --- 管理员/负责人操作接口 ---
    @GetMapping("/admin/organizations/{id}/pending-approvals")
    public Result<List<PendingApprovalDTO>> listPendingApprovals(@PathVariable("id") Long orgId, Principal principal) {
        return organizationService.listPendingApprovals(orgId, principal);
    }

    @PostMapping("/admin/memberships/{id}/approve")
    public Result<Void> approveApplication(@PathVariable("id") Long membershipId, Principal principal) {
        return organizationService.approveApplication(membershipId, principal);
    }

    @DeleteMapping("/admin/memberships/{id}")
    public Result<Void> rejectOrRemoveMember(@PathVariable("id") Long membershipId, Principal principal) {
        return organizationService.rejectOrRemoveMember(membershipId, principal);
    }
}