package com.student.webproject.controller;

import com.student.webproject.dto.ActivityCreateDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/activities")

public class AdminActivityController {
    @PostMapping
    public String createActivity(@RequestBody ActivityCreateDTO activityData) {
        // 为了验证我们是否成功收到了数据，我们把它打印出来
        System.out.println("成功收到前端发来的数据：" + activityData.toString());
        // 返回一句话，证明数据已经收到
        return "成功接收到活动标题：" + activityData.getTitle();
    }
}