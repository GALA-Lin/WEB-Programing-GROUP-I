package com.student.webproject.user.controller;

import com.student.webproject.common.response.Result;
import com.student.webproject.user.Entity.User;
import com.student.webproject.user.dto.EnrolledActivityDTO;
import com.student.webproject.user.dto.ServiceRecordDTO;
import com.student.webproject.user.mapper.UserMapper;
import com.student.webproject.user.Service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserMapper userMapper;

    private User getUserByDetails(UserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("用户未登录");
        }
        User user = userMapper.selectByUsername(userDetails.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user;
    }

    @GetMapping("/service-records")
    public Result<List<ServiceRecordDTO>> getServiceRecords(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = getUserByDetails(userDetails);
        List<ServiceRecordDTO> records = profileService.getServiceRecordsByUserId(currentUser.getId());
        return Result.success(records, "时长明细查询成功");
    }

    @GetMapping("/enrollments")
    public Result<List<EnrolledActivityDTO>> getEnrollments(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = getUserByDetails(userDetails);
        List<EnrolledActivityDTO> activities = profileService.getEnrolledActivitiesByUserId(currentUser.getId());
        return Result.success(activities, "已报名活动查询成功");
    }
}