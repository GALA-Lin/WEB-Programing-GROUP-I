package com.student.webproject.dashboard;

import com.student.webproject.common.response.Result;
import com.student.webproject.dashboard.dto.DashboardStatsDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@AllArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public Result<DashboardStatsDTO> getStats() {
        return Result.success(dashboardService.getDashboardStats());
    }
}