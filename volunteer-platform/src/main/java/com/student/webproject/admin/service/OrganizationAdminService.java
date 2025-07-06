package com.student.webproject.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.student.webproject.admin.dto.OrganizationCreateDTO;
import com.student.webproject.admin.dto.OrganizationUpdateDTO;
import com.student.webproject.common.response.Result;
import com.student.webproject.organization.dto.OrganizationAdminViewDTO; // 修改导入
import com.student.webproject.organization.entity.Organization;
import java.security.Principal; // 新增导入

public interface OrganizationAdminService {
    // 修改返回类型
    Result<IPage<OrganizationAdminViewDTO>> listOrganizations(Long page, Long pageSize);

    Result<Organization> createOrganization(OrganizationCreateDTO dto);

    // 为 update 和 delete 添加 Principal 参数用于鉴权
    Result<Organization> updateOrganization(Long id, OrganizationUpdateDTO dto, Principal principal);
    Result<Void> deleteOrganization(Long id, Principal principal);
}