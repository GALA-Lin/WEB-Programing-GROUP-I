// 文件路径: src/main/java/com/student/webproject/admin/service/impl/ActivityAdminServiceImpl.java
package com.student.webproject.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.student.webproject.admin.dto.ActivityCreateDTO;
import com.student.webproject.admin.entity.Activity;
import com.student.webproject.admin.mapper.ActivityMapper;
import com.student.webproject.admin.service.ActivityAdminService;
import com.student.webproject.common.response.Result; // 1. 确保导入了 Result 类
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ActivityAdminServiceImpl implements ActivityAdminService {

    private final ActivityMapper activityMapper;

    public ActivityAdminServiceImpl(ActivityMapper activityMapper) {
        this.activityMapper = activityMapper;
    }

    /**
     * 【修改】重写 createActivity 方法
     */
    @Override
    public Result<Activity> createActivity(ActivityCreateDTO dto) {
        Activity activity = new Activity();
        // --- 数据转换和设置默认值部分，保持不变 ---
        activity.setTitle(dto.getTitle());
        activity.setDescription(dto.getDescription());
        activity.setCoverImageUrl(dto.getCoverImageUrl());
        activity.setCategory(dto.getCategory());
        activity.setLocation(dto.getLocation());
        activity.setOrganizerId(dto.getOrganizerId());
        activity.setRecruitmentQuota(dto.getRecruitmentQuota());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        activity.setStartTime(LocalDateTime.parse(dto.getStartTime(), formatter));
        activity.setEndTime(LocalDateTime.parse(dto.getEndTime(), formatter));

        activity.setCurrentEnrollment(0);
        activity.setStatus("recruiting");
        activity.setCreatedBy(1L); // 以后会从 Token 中获取当前登录用户ID
        activity.setCreatedAt(LocalDateTime.now());
        activity.setUpdatedAt(LocalDateTime.now());

        // --- 数据库操作 ---
        int rows = activityMapper.insert(activity);

        // --- 【修改】返回结果部分 ---
        if (rows > 0) {
            // 使用 Result.created 静态方法返回成功响应
            // Mybatis-Plus 在插入后会自动将生成的主键ID回填到 activity 对象中
            return Result.created(activity, "活动发布成功");
        } else {
            // 【重要修改】不再返回字符串，而是抛出异常
            // 这个异常会被我们之前写的 GlobalExceptionHandler 捕获，并转换成标准的JSON错误返回给前端
            throw new RuntimeException("活动发布失败，数据库插入操作未成功。");
        }
    }

    /**
     * 【修改】重写 updateActivity 方法
     */
    @Override
    public Result<Activity> updateActivity(Long id, ActivityCreateDTO dto) {
        Activity activityFromDB = activityMapper.selectById(id);

        // --- 【修改】错误处理 ---
        if (activityFromDB == null) {
            // 直接抛出异常，而不是返回字符串
            throw new RuntimeException("更新失败，找不到ID为 " + id + " 的活动。");
        }

        // --- DTO 数据覆盖部分，保持不变 ---
        if (dto.getTitle() != null) activityFromDB.setTitle(dto.getTitle());
        // ... (省略其他 if...null 判断)
        activityFromDB.setUpdatedAt(LocalDateTime.now());

        // --- 数据库操作 ---
        int rows = activityMapper.updateById(activityFromDB);

        // --- 【修改】返回结果 ---
        if (rows > 0) {
            return Result.success(activityFromDB, "ID为 " + id + " 的活动更新成功！");
        } else {
            throw new RuntimeException("更新失败，数据库更新操作未成功。");
        }
    }

    /**
     * 【修改】重写 deleteActivity 方法
     */
    @Override
    public Result<Void> deleteActivity(Long id) {
        // 先检查一下活动是否存在，这样可以给前端更明确的错误信息
        if (activityMapper.selectById(id) == null) {
            throw new RuntimeException("删除失败，找不到ID为 " + id + " 的活动。");
        }

        int rows = activityMapper.deleteById(id);

        if (rows > 0) {
            // 删除成功，返回一个不包含 data 的成功响应
            return Result.success(null, "ID为 " + id + " 的活动删除成功！");
        } else {
            throw new RuntimeException("删除失败，数据库删除操作未成功。");
        }
    }

    /**
     * 【修改】重写 listActivities 方法
     */
    @Override
    public Result<IPage<Activity>> listActivities(Long page, Long pageSize) {
        IPage<Activity> pageRequest = new Page<>(page, pageSize);
        IPage<Activity> pageResult = activityMapper.selectPage(pageRequest, null);

        // 【修改】将分页结果用 Result.success() 包装后返回
        return Result.success(pageResult, "活动列表查询成功");
    }
}