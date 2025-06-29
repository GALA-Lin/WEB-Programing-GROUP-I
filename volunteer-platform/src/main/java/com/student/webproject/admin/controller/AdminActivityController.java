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
        System.out.println("Controller 收到了更新请求，要更新的活动ID是: " + id);
        System.out.println("更新后的数据是: " + dto.toString());

        // TODO: 我们稍后会在这里调用 Service 的更新方法
        return "成功接收到更新请求！";
    }
}