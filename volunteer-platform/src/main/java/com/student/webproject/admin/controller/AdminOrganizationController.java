package com.student.webproject.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.student.webproject.admin.dto.OrganizationCreateDTO;
import com.student.webproject.admin.dto.OrganizationUpdateDTO;
import com.student.webproject.admin.service.OrganizationAdminService;
import com.student.webproject.common.response.Result;
import com.student.webproject.organization.dto.OrganizationAdminViewDTO;
import com.student.webproject.organization.entity.Organization;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/admin/organizations")
@AllArgsConstructor
public class AdminOrganizationController {

    private final OrganizationAdminService organizationAdminService;

    @GetMapping
    public Result<IPage<OrganizationAdminViewDTO>> listOrganizations(
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long pageSize) {
        // 返回类型已更新为 OrganizationAdminViewDTO，以包含负责人姓名
        return organizationAdminService.listOrganizations(page, pageSize);
    }

    @PostMapping
    public Result<Organization> createOrganization(@RequestBody OrganizationCreateDTO dto) {
        return organizationAdminService.createOrganization(dto);
    }

    @PutMapping("/{id}")
    public Result<Organization> updateOrganization(@PathVariable Long id, @RequestBody OrganizationUpdateDTO dto, Principal principal) {
        // 已加入 Principal 参数用于权限校验
        return organizationAdminService.updateOrganization(id, dto, principal);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteOrganization(@PathVariable Long id, Principal principal) {
        // 已加入 Principal 参数用于权限校验
        return organizationAdminService.deleteOrganization(id, principal);
    }
}