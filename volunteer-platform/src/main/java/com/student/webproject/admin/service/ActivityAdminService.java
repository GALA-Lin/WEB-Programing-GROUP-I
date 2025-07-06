package com.student.webproject.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.student.webproject.admin.dto.ActivityCreateDTO;
import com.student.webproject.admin.dto.EnrollmentViewDTO; // 【新增】导入
import com.student.webproject.admin.entity.Activity;
import com.student.webproject.common.response.Result;

import java.io.ByteArrayInputStream; // 【新增】导入
import java.io.IOException;         // 【新增】导入
import java.util.List;              // 【新增】导入

public interface ActivityAdminService {

    Result<Activity> createActivity(ActivityCreateDTO dto);

    Result<Activity> updateActivity(Long id, ActivityCreateDTO dto);

    Result<Void> deleteActivity(Long id);

    Result<IPage<Activity>> listActivities(Long page, Long pageSize);

    /**
     * 获取活动报名名单的接口定义
     * @param activityId 活动ID
     * @return 报名者信息列表
     */
    Result<List<EnrollmentViewDTO>> getEnrollmentsByActivityId(Long activityId);

    /**
     * 导出报名名单为Excel的接口定义
     * @param activityId 活动ID
     * @return 包含Excel文件的字节输入流
     * @throws IOException IO异常
     */
    ByteArrayInputStream exportEnrollmentsToExcel(Long activityId) throws IOException;
}