package com.student.webproject.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/activities")

public class AdminActivityController {
    @PostMapping
    public String createActivity() {
        return "成功接收到发布新活动的请求！";
    }
}
