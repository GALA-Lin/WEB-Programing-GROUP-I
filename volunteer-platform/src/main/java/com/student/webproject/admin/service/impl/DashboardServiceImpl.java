package com.student.webproject.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.student.webproject.admin.dto.DashboardStatsDTO;
import com.student.webproject.admin.dto.LatestUpdatesDTO;
import com.student.webproject.admin.entity.Activity;
import com.student.webproject.admin.mapper.AdminActivityMapper;
import com.student.webproject.admin.service.DashboardService;
import com.student.webproject.common.response.Result;
import com.student.webproject.organization.entity.Organization;
import com.student.webproject.organization.mapper.OrganizationMapper;
import com.student.webproject.user.Entity.User;
import com.student.webproject.user.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserMapper userMapper;
    private final AdminActivityMapper activityMapper;
    private final OrganizationMapper organizationMapper;

    private DashboardStatsDTO getCommonStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        stats.setTotalUsers(userMapper.selectCount(null));
        stats.setTotalActivities(activityMapper.selectCount(null));
        stats.setTotalOrganizations(organizationMapper.selectCount(null));

        // 计算总时长
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("sum(total_service_hours) as totalServiceHours");
        List<Object> result = userMapper.selectObjs(queryWrapper);
        if (result != null && !result.isEmpty() && result.get(0) != null) {
            stats.setTotalServiceHours((BigDecimal) result.get(0));
        } else {
            stats.setTotalServiceHours(BigDecimal.ZERO);
        }

        return stats;
    }

    @Override
    public Result<DashboardStatsDTO> getPublicDashboardStats() {
        return Result.success(getCommonStats(), "查询成功");
    }

    @Override
    public Result<DashboardStatsDTO> getAdminDashboardStats() {
        return Result.success(getCommonStats(), "查询成功");
    }

    @Override
    public Result<LatestUpdatesDTO> getDashboardLatestUpdates() {
        LatestUpdatesDTO updates = new LatestUpdatesDTO();

        // 获取最新的5个用户
        QueryWrapper<User> userQuery = new QueryWrapper<User>().orderByDesc("created_at").last("LIMIT 5");
        updates.setLatestUsers(userMapper.selectList(userQuery));

        // 获取最新的5个活动
        QueryWrapper<Activity> activityQuery = new QueryWrapper<Activity>().orderByDesc("created_at").last("LIMIT 5");
        updates.setLatestActivities(activityMapper.selectList(activityQuery));

        return Result.success(updates, "查询成功");
    }
}