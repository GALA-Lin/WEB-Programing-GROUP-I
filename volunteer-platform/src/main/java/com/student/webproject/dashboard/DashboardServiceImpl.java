package com.student.webproject.dashboard;

import com.student.webproject.admin.mapper.AdminActivityMapper;
import com.student.webproject.dashboard.dto.DashboardStatsDTO;
import com.student.webproject.organization.mapper.OrganizationMapper;
import com.student.webproject.user.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserMapper userMapper;
    private final AdminActivityMapper activityMapper;
    private final OrganizationMapper organizationMapper;

    @Override
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        // 1. 获取总用户数
        stats.setTotalUsers(userMapper.selectCount(null));

        // 2. 获取总活动数
        stats.setTotalActivities(activityMapper.selectCount(null));

        // 3. 计算总志愿时长
        // 注意：这里直接在Java中流式计算，如果用户量巨大，更优的做法是直接在SQL中使用SUM()
        BigDecimal totalHours = userMapper.selectList(null).stream()
                .map(user -> user.getTotalServiceHours() == null ? BigDecimal.ZERO : user.getTotalServiceHours())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.setTotalServiceHours(totalHours);

        // 4. 获取总组织数
        stats.setTotalOrganizations(organizationMapper.selectCount(null));

        return stats;
    }
}