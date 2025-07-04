package com.student.webproject.dashboard.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DashboardStatsDTO {
    private long totalUsers;
    private long totalActivities;
    private BigDecimal totalServiceHours;
    private long totalOrganizations;
}