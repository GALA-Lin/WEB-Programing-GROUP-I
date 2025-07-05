package com.student.webproject.admin.service;

import com.student.webproject.admin.dto.DashboardStatsDTO;
import com.student.webproject.admin.dto.LatestUpdatesDTO;
import com.student.webproject.common.response.Result;

public interface DashboardService {

    /**
     * 获取公开的仪表盘统计数据
     */
    Result<DashboardStatsDTO> getPublicDashboardStats();

    /**
     * 获取管理员的仪表盘统计数据
     */
    Result<DashboardStatsDTO> getAdminDashboardStats();

    /**
     * 获取最新的动态数据
     */
    Result<LatestUpdatesDTO> getDashboardLatestUpdates();
}