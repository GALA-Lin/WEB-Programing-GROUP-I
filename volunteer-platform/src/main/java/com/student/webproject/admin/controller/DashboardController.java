package com.student.webproject.admin.controller;

import com.student.webproject.admin.dto.DashboardStatsDTO;
import com.student.webproject.admin.dto.LatestUpdatesDTO;
import com.student.webproject.admin.service.DashboardService;
import com.student.webproject.common.response.Result;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api") // 使用根路径，方便区分公开和后台接口
@AllArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    // 公开接口，供前台首页使用
    @GetMapping("/dashboard/stats")
    public Result<DashboardStatsDTO> getPublicStats() {
        return dashboardService.getPublicDashboardStats();
    }

    // 后台接口，需要管理员权限
    @GetMapping("/admin/dashboard/stats")
    public Result<DashboardStatsDTO> getAdminStats() {
        return dashboardService.getAdminDashboardStats();
    }

    // 后台接口，需要管理员权限
    @GetMapping("/admin/dashboard/latest-updates")
    public Result<LatestUpdatesDTO> getLatestUpdates() {
        return dashboardService.getDashboardLatestUpdates();
    }
}