package com.student.webproject.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.student.webproject.admin.dto.ActivityCreateDTO;
import com.student.webproject.admin.dto.EnrollmentViewDTO;
import com.student.webproject.admin.entity.Activity;
import com.student.webproject.admin.mapper.AdminActivityMapper;
import com.student.webproject.admin.service.ActivityAdminService;
import com.student.webproject.common.response.Result;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ActivityAdminServiceImpl implements ActivityAdminService {

    private final AdminActivityMapper activityMapper;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ActivityAdminServiceImpl(AdminActivityMapper activityMapper) {
        this.activityMapper = activityMapper;
    }
    // 【新增】查询报名名单的业务逻辑
    @Override
    public Result<List<EnrollmentViewDTO>> getEnrollmentsByActivityId(Long activityId) {
        List<EnrollmentViewDTO> enrollments = activityMapper.selectEnrollmentsByActivityId(activityId);
        return Result.success(enrollments, "报名名单查询成功");
    }

    // 【新增】导出报名名单为Excel的业务逻辑
    @Override
    public ByteArrayInputStream exportEnrollmentsToExcel(Long activityId) throws IOException {
        List<EnrollmentViewDTO> enrollments = activityMapper.selectEnrollmentsByActivityId(activityId);
        Activity activity = activityMapper.selectById(activityId);
        String sheetName = "活动报名表-" + (activity != null ? activity.getTitle() : activityId);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(sheetName);

            // 创建表头
            String[] headers = {"姓名", "学号", "手机号", "报名时间"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // 填充数据
            int rowIdx = 1;
            for (EnrollmentViewDTO enrollment : enrollments) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(enrollment.getRealName());
                row.createCell(1).setCellValue(enrollment.getStudentId());
                row.createCell(2).setCellValue(enrollment.getPhoneNumber());
                row.createCell(3).setCellValue(enrollment.getEnrolledAt().format(FORMATTER));
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public Result<Activity> createActivity(ActivityCreateDTO dto) {
        Activity activity = new Activity();
        activity.setTitle(dto.getTitle());
        activity.setDescription(dto.getDescription());
        activity.setCoverImageUrl(dto.getCoverImageUrl());
        activity.setCategory(dto.getCategory());
        activity.setLocation(dto.getLocation());
        activity.setOrganizerId(dto.getOrganizerId());
        activity.setRecruitmentQuota(dto.getRecruitmentQuota());


        activity.setStartTime(LocalDateTime.parse(dto.getStartTime().replace('T', ' '), FORMATTER));
        activity.setEndTime(LocalDateTime.parse(dto.getEndTime().replace('T', ' '), FORMATTER));

        activity.setCurrentEnrollment(0);
        activity.setStatus("recruiting");
        activity.setCreatedBy(1L);
        activity.setCreatedAt(LocalDateTime.now());
        activity.setUpdatedAt(LocalDateTime.now());

        int rows = activityMapper.insert(activity);

        if (rows > 0) {
            return Result.created(activity, "活动发布成功");
        } else {
            throw new RuntimeException("活动发布失败，数据库插入操作未成功。");
        }
    }

    @Override
    public Result<Activity> updateActivity(Long id, ActivityCreateDTO dto) {
        Activity activityFromDB = activityMapper.selectById(id);

        if (activityFromDB == null) {
            throw new RuntimeException("更新失败，找不到ID为 " + id + " 的活动。");
        }

        if (dto.getTitle() != null) {
            activityFromDB.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            activityFromDB.setDescription(dto.getDescription());
        }
        if (dto.getCoverImageUrl() != null) {
            activityFromDB.setCoverImageUrl(dto.getCoverImageUrl());
        }
        if (dto.getCategory() != null) {
            activityFromDB.setCategory(dto.getCategory());
        }
        if (dto.getLocation() != null) {
            activityFromDB.setLocation(dto.getLocation());
        }
        if (dto.getStartTime() != null) {
            // --- ↓↓↓ 关键修改：在解析前，将'T'替换为空格，兼容两种格式 ↓↓↓ ---
            activityFromDB.setStartTime(LocalDateTime.parse(dto.getStartTime().replace('T', ' '), FORMATTER));
        }
        if (dto.getEndTime() != null) {
            activityFromDB.setEndTime(LocalDateTime.parse(dto.getEndTime().replace('T', ' '), FORMATTER));
        }
        if (dto.getOrganizerId() != null) {
            activityFromDB.setOrganizerId(dto.getOrganizerId());
        }
        if (dto.getRecruitmentQuota() != null) {
            activityFromDB.setRecruitmentQuota(dto.getRecruitmentQuota());
        }

        activityFromDB.setUpdatedAt(LocalDateTime.now());

        int rows = activityMapper.updateById(activityFromDB);

        if (rows > 0) {
            return Result.success(activityFromDB, "ID为 " + id + " 的活动更新成功！");
        } else {
            throw new RuntimeException("更新失败，数据库更新操作未成功。");
        }
    }

    @Override
    public Result<Void> deleteActivity(Long id) {
        if (activityMapper.selectById(id) == null) {
            throw new RuntimeException("删除失败，找不到ID为 " + id + " 的活动。");
        }

        int rows = activityMapper.deleteById(id);

        if (rows > 0) {
            return Result.success(null, "ID为 " + id + " 的活动删除成功！");
        } else {
            throw new RuntimeException("删除失败，数据库删除操作未成功。");
        }
    }

    @Override
    public Result<IPage<Activity>> listActivities(Long page, Long pageSize) {
        IPage<Activity> pageRequest = new Page<>(page, pageSize);
        IPage<Activity> pageResult = activityMapper.selectPage(pageRequest, null);

        return Result.success(pageResult, "活动列表查询成功");
    }
}