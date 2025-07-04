package com.student.webproject.organization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.student.webproject.organization.dto.OrganizationAdminViewDTO; // 新增导入
import com.student.webproject.organization.dto.OrganizationSummaryDTO;
import com.student.webproject.organization.entity.Organization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface OrganizationMapper extends BaseMapper<Organization> {

    // 前台使用的查询 (保持不变)
    @Select("SELECT o.id, o.name, o.description, u.real_name as leaderName, " +
            "(SELECT COUNT(*) FROM organization_members om WHERE om.organization_id = o.id AND om.status = 'approved') as totalMembers " +
            "FROM organizations o LEFT JOIN users u ON o.leader_id = u.id")
    List<OrganizationSummaryDTO> selectOrganizationSummaries();

    // --- ↓↓↓ 新增：后台管理专用的分页查询 ↓↓↓ ---
    @Select("SELECT o.id, o.name, o.description, o.leader_id as leaderId, u.real_name as leaderName, o.updated_at as updatedAt " +
            "FROM organizations o LEFT JOIN users u ON o.leader_id = u.id")
    IPage<OrganizationAdminViewDTO> selectOrganizationAdminViewPage(Page<?> page);
}