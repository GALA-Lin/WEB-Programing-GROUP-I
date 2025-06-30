package com.student.webproject.activity.controller;

import com.student.webproject.activity.dto.ActivityListResponse;
import com.student.webproject.activity.dto.ActivityDetailResponse;
import com.student.webproject.activity.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    // 查询活动列表
    @GetMapping
    public ActivityListResponse getActivities(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String category) {
        return activityService.getActivities(page, pageSize, category);
    }

    // 查询活动详情
    @GetMapping("/{id}")
    public ActivityDetailResponse getActivity(@PathVariable Long id, @RequestParam String currentUserId) {
        return activityService.getActivityById(id, currentUserId);
    }

    // 报名活动
    @PostMapping("/{id}/enroll")
    public ResponseEntity<?> enroll(@PathVariable Long id, @RequestParam String currentUserId) {
        activityService.enrollActivity(id, currentUserId);
        return ResponseEntity.ok().build();
    }

    // 取消报名
    @DeleteMapping("/{id}/enroll")
    public ResponseEntity<?> unenroll(@PathVariable Long id, @RequestParam String currentUserId) {
        activityService.unenrollActivity(id, currentUserId);
        return ResponseEntity.ok().build();
    }
}