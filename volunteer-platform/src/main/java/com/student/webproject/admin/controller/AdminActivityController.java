// 文件路径: src/main/java/com/student/webproject/admin/controller/AdminActivityController.java
package com.student.webproject.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.student.webproject.admin.dto.ActivityCreateDTO;
import com.student.webproject.admin.dto.EnrollmentViewDTO;
import com.student.webproject.admin.entity.Activity;
import com.student.webproject.admin.service.ActivityAdminService;
import com.student.webproject.common.response.Result; // 1. 导入 Result 类
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/activities")
public class AdminActivityController {

    @Autowired
    private ActivityAdminService activityAdminService;

    /**
     * 【修改】返回值从 String 改为 Result<Activity>
     */
    @PostMapping
    public Result<Activity> createActivity(@RequestBody ActivityCreateDTO activityData) {
        return activityAdminService.createActivity(activityData);
    }

    /**
     * 【修改】返回值从 String 改为 Result<Activity>
     */
    @PutMapping("/{id}")
    public Result<Activity> updateActivity(@PathVariable Long id, @RequestBody ActivityCreateDTO dto) {
        return activityAdminService.updateActivity(id, dto);
    }

    /**
     * 【修改】返回值从 String 改为 Result<Void>
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteActivity(@PathVariable Long id) {
        return activityAdminService.deleteActivity(id);
    }

    /**
     * 【修改】返回值从 Object 改为 Result<IPage<Activity>>
     */
    @GetMapping
    public Result<IPage<Activity>> listActivities(@RequestParam(required = false, defaultValue = "1") Long page,
                                                  @RequestParam(required = false, defaultValue = "10") Long pageSize) {
        return activityAdminService.listActivities(page, pageSize);
    }
    // 【新增】获取报名名单的API
    @GetMapping("/{id}/enrollments")
    public Result<List<EnrollmentViewDTO>> getEnrollments(@PathVariable Long id) {
        return activityAdminService.getEnrollmentsByActivityId(id);
    }

    // 【新增】导出报名名单Excel的API
    @GetMapping("/{id}/enrollments/export")
    public ResponseEntity<InputStreamResource> exportEnrollments(@PathVariable Long id) throws IOException {
        ByteArrayInputStream bis = activityAdminService.exportEnrollmentsToExcel(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=enrollments.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(bis));
    }
}