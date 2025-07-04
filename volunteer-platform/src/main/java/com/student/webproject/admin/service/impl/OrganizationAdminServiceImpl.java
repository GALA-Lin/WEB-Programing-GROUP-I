package com.student.webproject.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.student.webproject.admin.dto.OrganizationCreateDTO;
import com.student.webproject.admin.dto.OrganizationUpdateDTO;
import com.student.webproject.admin.service.OrganizationAdminService;
import com.student.webproject.common.response.Result;
import com.student.webproject.organization.dto.OrganizationAdminViewDTO;
import com.student.webproject.organization.entity.Organization;
import com.student.webproject.organization.mapper.OrganizationMapper;
import com.student.webproject.user.Entity.User;
import com.student.webproject.user.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.util.Objects;

@Service
@AllArgsConstructor
public class OrganizationAdminServiceImpl implements OrganizationAdminService {

    private final OrganizationMapper organizationMapper;
    private final UserMapper userMapper; // 引入 UserMapper 用于鉴权

    @Override
    public Result<IPage<OrganizationAdminViewDTO>> listOrganizations(Long page, Long pageSize) {
        Page<OrganizationAdminViewDTO> pageRequest = new Page<>(page, pageSize);
        IPage<OrganizationAdminViewDTO> pageResult = organizationMapper.selectOrganizationAdminViewPage(pageRequest);
        return Result.success(pageResult, "组织列表查询成功");
    }

    @Override
    public Result<Organization> createOrganization(OrganizationCreateDTO dto) {
        // 创建组织通常只有超级管理员可以，这里暂时不加鉴权，或默认只有super_admin能访问此接口
        if (organizationMapper.selectOne(new QueryWrapper<Organization>().eq("name", dto.getName())) != null) {
            throw new RuntimeException("组织名称已存在");
        }
        Organization organization = new Organization();
        organization.setName(dto.getName());
        organization.setDescription(dto.getDescription());
        organization.setLeaderId(dto.getLeaderId());
        organizationMapper.insert(organization);
        return Result.created(organization, "组织创建成功");
    }

    @Override
    public Result<Organization> updateOrganization(Long id, OrganizationUpdateDTO dto, Principal principal) {
        checkPermission(id, principal); // 权限校验
        Organization organization = organizationMapper.selectById(id);
        if (organization == null) {
            throw new RuntimeException("组织不存在");
        }
        if (dto.getName() != null) organization.setName(dto.getName());
        if (dto.getDescription() != null) organization.setDescription(dto.getDescription());
        if (dto.getLeaderId() != null) organization.setLeaderId(dto.getLeaderId());
        organizationMapper.updateById(organization);
        return Result.success(organization, "组织信息更新成功");
    }

    @Override
    public Result<Void> deleteOrganization(Long id, Principal principal) {
        checkPermission(id, principal); // 权限校验
        if (organizationMapper.selectById(id) == null) {
            throw new RuntimeException("删除失败，找不到ID为 " + id + " 的组织。");
        }
        organizationMapper.deleteById(id);
        return Result.success(null, "组织删除成功");
    }

    // --- ↓↓↓ 新增：统一的权限校验方法 ↓↓↓ ---
    private void checkPermission(Long orgId, Principal principal) {
        User operator = userMapper.selectOne(new QueryWrapper<User>().eq("username", principal.getName()));
        Organization org = organizationMapper.selectById(orgId);
        if (org == null) throw new RuntimeException("操作失败，组织不存在");

        boolean isSuperAdmin = "super_admin".equals(operator.getRole());
        boolean isLeader = Objects.equals(operator.getId(), org.getLeaderId());

        if (!isSuperAdmin && !isLeader) {
            throw new RuntimeException("权限不足，只有超级管理员或该组织负责人可以操作！");
        }
    }
}