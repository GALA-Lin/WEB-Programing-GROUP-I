package com.student.webproject.organization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.student.webproject.organization.entity.Organization;
import com.student.webproject.organization.dto.OrganizationSummaryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface OrganizationMapper extends BaseMapper<Organization> {
    @Select("SELECT o.id, o.name, o.description, u.real_name as leaderName, " +
            "(SELECT COUNT(*) FROM organization_members om WHERE om.organization_id = o.id) as totalMembers " +
            "FROM organizations o LEFT JOIN users u ON o.leader_id = u.id")
    List<OrganizationSummaryDTO> selectOrganizationSummaries();
}