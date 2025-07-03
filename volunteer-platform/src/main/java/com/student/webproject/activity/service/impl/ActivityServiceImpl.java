package com.student.webproject.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper; // 导入QueryWrapper
import com.student.webproject.activity.dto.ActivityListResponse;
import com.student.webproject.activity.dto.ActivityDetailResponse;
import com.student.webproject.activity.service.ActivityService;
import com.student.webproject.admin.entity.Activity;
import com.student.webproject.activity.entity.Enrollment;
import com.student.webproject.user.Entity.User; // 导入User实体
import com.student.webproject.user.mapper.UserMapper; // 导入UserMapper
import com.student.webproject.admin.mapper.AdminActivityMapper;
import com.student.webproject.activity.mapper.EnrollmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private AdminActivityMapper activityMapper;
    @Autowired
    private EnrollmentMapper enrollmentMapper;
    @Autowired
    private UserMapper userMapper; // 注入UserMapper

    // ... （构造函数可以简化或移除，因为我们用了@Autowired）

    @Override
    public ActivityListResponse getActivities(int page, int pageSize, String category) {
        if (page <= 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Page and pageSize must be positive numbers");
        }
        int offset = (page - 1) * pageSize;
        List<Activity> activityList = activityMapper.selectActivities(offset, pageSize, category);
        int total = activityMapper.countActivities(category);

        ActivityListResponse response = new ActivityListResponse();
        response.setCode(200);
        response.setMessage("Success");

        ActivityListResponse.Data data = new ActivityListResponse.Data();
        data.setTotal(total);

        List<ActivityListResponse.ActivityItem> items = new ArrayList<>();
        for (Activity activity : activityList) {
            items.add(mapToActivityItem(activity));
        }
        data.setList(items);
        response.setData(data);
        return response;
    }

    @Override
    public ActivityDetailResponse getActivityById(Long id, String currentUsername) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new RuntimeException("Activity not found");
        }

        ActivityDetailResponse response = new ActivityDetailResponse();
        response.setCode(200);
        response.setMessage("Success");
        ActivityDetailResponse.Data data = new ActivityDetailResponse.Data();
        mapToActivityData(activity, data);

        boolean isEnrolled = false;
        // 如果用户名不为空（即用户已登录），则检查报名状态
        if (currentUsername != null) {
            User user = findUserByUsername(currentUsername);
            isEnrolled = enrollmentMapper.existsByUserIdAndActivityId(user.getId(), id);
        }
        data.setIsEnrolled(isEnrolled);
        response.setData(data);
        return response;
    }

    @Override
    @Transactional
    public void enrollActivity(Long id, String currentUsername) {
        User user = findUserByUsername(currentUsername);
        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new RuntimeException("Activity not found");
        }
        if (enrollmentMapper.existsByUserIdAndActivityId(user.getId(), id)) {
            throw new RuntimeException("Already enrolled");
        }
        if (activity.getCurrentEnrollment() >= activity.getRecruitmentQuota()) {
            throw new RuntimeException("No available slots");
        }
        Enrollment enrollment = new Enrollment();
        enrollment.setUserId(user.getId());
        enrollment.setActivityId(id);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setStatus("enrolled");
        enrollmentMapper.insertEnrollment(enrollment);
        activity.setCurrentEnrollment(activity.getCurrentEnrollment() + 1);
        activityMapper.updateCurrentEnrollment(id, activity.getCurrentEnrollment());
    }

    @Override
    @Transactional
    public void unenrollActivity(Long id, String currentUsername) {
        User user = findUserByUsername(currentUsername);
        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new RuntimeException("Activity not found");
        }
        enrollmentMapper.deleteByUserIdAndActivityId(user.getId(), id);
        if (activity.getCurrentEnrollment() > 0) {
            activity.setCurrentEnrollment(activity.getCurrentEnrollment() - 1);
            activityMapper.updateCurrentEnrollment(id, activity.getCurrentEnrollment());
        }
    }

    // 新增一个辅助方法，通过用户名查找用户
    private User findUserByUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }
        return user;
    }

    // ... (其他 mapToActivityItem 和 mapToActivityData 方法保持不变)
    private ActivityListResponse.ActivityItem mapToActivityItem(Activity activity) {
        ActivityListResponse.ActivityItem item = new ActivityListResponse.ActivityItem();
        item.setId(activity.getId());
        item.setTitle(activity.getTitle());
        item.setDescription(activity.getDescription());
        item.setCoverImageUrl(activity.getCoverImageUrl());
        item.setCategory(activity.getCategory());
        item.setLocation(activity.getLocation());
        item.setStartTime(activity.getStartTime().toString());
        item.setEndTime(activity.getEndTime().toString());
        item.setRecruitmentQuota(activity.getRecruitmentQuota());
        item.setCurrentEnrollment(activity.getCurrentEnrollment());
        item.setStatus(activity.getStatus());
        return item;
    }

    private void mapToActivityData(Activity activity, ActivityDetailResponse.Data data) {
        data.setId(activity.getId());
        data.setTitle(activity.getTitle());
        data.setDescription(activity.getDescription());
        data.setCoverImageUrl(activity.getCoverImageUrl());
        data.setCategory(activity.getCategory());
        data.setLocation(activity.getLocation());
        data.setStartTime(activity.getStartTime().toString());
        data.setEndTime(activity.getEndTime().toString());
        data.setRecruitmentQuota(activity.getRecruitmentQuota());
        data.setCurrentEnrollment(activity.getCurrentEnrollment());
        data.setStatus(activity.getStatus());
    }
}