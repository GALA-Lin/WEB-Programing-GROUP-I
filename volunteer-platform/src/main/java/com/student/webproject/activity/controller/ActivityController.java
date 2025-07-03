package com.student.webproject.activity.controller;

import com.student.webproject.activity.dto.ActivityListResponse;
import com.student.webproject.activity.dto.ActivityDetailResponse;
import com.student.webproject.activity.service.ActivityService;
// 1. 导入 Principal
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    // 查询活动列表 (此接口通常是公开的，可以暂时不修改)
    @GetMapping
    public ActivityListResponse getActivities(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String category) {
        return activityService.getActivities(page, pageSize, category);
    }

    // 2. 修改：查询活动详情接口
    @GetMapping("/{id}")
    public ActivityDetailResponse getActivity(@PathVariable Long id, Principal principal) {
        // Principal 对象包含了当前登录用户的信息。如果用户未登录，它会是 null。
        String currentUsername = (principal != null) ? principal.getName() : null;
        return activityService.getActivityById(id, currentUsername);
    }

    // 3. 修改：报名活动接口
    @PostMapping("/{id}/enroll")
    public ResponseEntity<?> enroll(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new java.util.HashMap<String, Object>() {{
                put("code", 401);
                put("message", "未登录，无��报名");
            }});
        }
        String currentUsername = principal.getName();
        activityService.enrollActivity(id, currentUsername);
        return ResponseEntity.ok().body(new java.util.HashMap<String, Object>() {{
            put("code", 200);
            put("message", "报名成功");
        }});
    }

    // 4. 修改：取消报名接口
    @DeleteMapping("/{id}/enroll")
    public ResponseEntity<?> unenroll(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new java.util.HashMap<String, Object>() {{
                put("code", 401);
                put("message", "未登录，无法取消报名");
            }});
        }
        String currentUsername = principal.getName();
        activityService.unenrollActivity(id, currentUsername);
        return ResponseEntity.ok().body(new java.util.HashMap<String, Object>() {{
            put("code", 200);
            put("message", "取消报名成功");
        }});
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        java.util.Map<String, Object> body = new java.util.HashMap<>();
        body.put("code", 400);
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}