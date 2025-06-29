package com.student.webproject.admin.controller;

import com.student.webproject.admin.dto.ActivityCreateDTO;
import com.student.webproject.admin.service.ActivityAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}