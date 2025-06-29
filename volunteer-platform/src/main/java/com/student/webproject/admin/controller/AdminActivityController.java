package com.student.webproject.admin.controller;

import com.student.webproject.admin.dto.ActivityCreateDTO;
import com.student.webproject.admin.service.ActivityAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/activities")
public class AdminActivityController {
    /**
     * @Autowired 注解告诉 Spring Boot:
     * "大管家，我需要一个能完成 ActivityAdminService 工作的部门，
     * 请你自动帮我把它找到并送到这里来。"
     * Spring 会自动找到我们之前写的那个 ActivityAdminServiceImpl。
     */
    @Autowired
    private ActivityAdminService activityAdminService;

    @PostMapping
    public String createActivity(@RequestBody ActivityCreateDTO activityData) {
        return activityAdminService.createActivity(activityData);
    }

    /**
     * 新增的“更新”接口
     * @PutMapping("/{id}"): 告诉 Spring Boot, 这个方法处理 PUT 类型的、
     * 并且路径末尾带有ID的请求。例如: PUT http://localhost:8080/api/admin/activities/1
     * @PathVariable Long id: 告诉 Spring Boot, "请把路径中 {id} 部分的值，取出来赋给 long id 这个变量。"
     * @RequestBody ActivityCreateDTO dto: 和之前一样，接收请求体中的 JSON 数据。
     */
    @PutMapping("/{id}")
    public String updateActivity(@PathVariable Long id, @RequestBody ActivityCreateDTO dto) {
        return activityAdminService.updateActivity(id, dto);
    }

    /**
     * 新增的“删除”接口
     * @DeleteMapping("/{id}"): 告诉 Spring Boot, 这个方法处理 DELETE 类型的、
     * 并且路径末尾带有ID的请求。例如: DELETE http://localhost:8080/api/admin/activities/1
     */
    @DeleteMapping("/{id}")
    public String deleteActivity(@PathVariable Long id) {
        // 直接将删除任务转交给 Service
        return activityAdminService.deleteActivity(id);
    }

    /**
     * 新增的“查询活动列表”接口
     * @GetMapping: 告诉 Spring Boot, 这个方法处理 GET 类型的请求。
     * @RequestParam(required = false, defaultValue = "1") Long page:
     * 这是一个查询参数，required = false 表示它不是必需的。
     * defaultValue = "1" 表示如果不传，默认值就是1。
     */
    @GetMapping
    public Object listActivities(@RequestParam(required = false, defaultValue = "1") Long page,
                                 @RequestParam(required = false, defaultValue = "10") Long pageSize) {
        // 将查询任务转交给 Service
        return activityAdminService.listActivities(page, pageSize);
    }
}