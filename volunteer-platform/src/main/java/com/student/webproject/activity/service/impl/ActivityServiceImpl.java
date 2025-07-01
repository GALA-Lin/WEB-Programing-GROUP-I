package com.student.webproject.activity.service.impl;

import com.student.webproject.activity.dto.ActivityListResponse;
import com.student.webproject.activity.dto.ActivityDetailResponse;
import com.student.webproject.activity.service.ActivityService;
import com.student.webproject.activity.repository.ActivityRepository;
import com.student.webproject.activity.entity.Activity;
import com.student.webproject.activity.entity.Enrollment;
import com.student.webproject.activity.repository.EnrollmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final EnrollmentRepository enrollmentRepository;

    public ActivityServiceImpl(ActivityRepository activityRepository, EnrollmentRepository enrollmentRepository) {
        this.activityRepository = activityRepository;
        this.enrollmentRepository = enrollmentRepository;
        if (activityRepository == null || enrollmentRepository == null) {
            throw new IllegalArgumentException("Repositories cannot be null");
        }
    }

    @Override
    public ActivityListResponse getActivities(int page, int pageSize, String category) {
        if (page <= 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Page and pageSize must be positive numbers");
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Activity> activityPage;

        if (category == null || category.isEmpty()) {
            activityPage = activityRepository.findAll(pageable);
        } else {
            activityPage = activityRepository.findByCategory(category, pageable);
        }

        ActivityListResponse response = new ActivityListResponse();
        response.setCode(200);
        response.setMessage("Success");

        ActivityListResponse.Data data = new ActivityListResponse.Data();
        data.setTotal((int) activityPage.getTotalElements());

        List<ActivityListResponse.ActivityItem> items = new ArrayList<>();
        for (Activity activity : activityPage.getContent()) {
            items.add(mapToActivityItem(activity));
        }

        data.setList(items);
        response.setData(data);

        return response;
    }

    @Override
    public ActivityDetailResponse getActivityById(Long id, String currentUserId) {
        if (id == null) {
            ActivityDetailResponse response = new ActivityDetailResponse();
            response.setCode(400);
            response.setMessage("Invalid activity ID");
            return response;
        }

        return activityRepository.findById(id)
                .map(activity -> {
                    ActivityDetailResponse response = new ActivityDetailResponse();
                    response.setCode(200);
                    response.setMessage("Success");

                    ActivityDetailResponse.Data data = new ActivityDetailResponse.Data();
                    mapToActivityData(activity, data);

                    boolean isEnrolled = false;
                    if (isNumeric(currentUserId)) {
                        isEnrolled = enrollmentRepository.existsByUserIdAndActivityId(Long.valueOf(currentUserId), id);
                    }
                    data.setIsEnrolled(isEnrolled);

                    response.setData(data);
                    return response;
                })
                .orElseGet(() -> {
                    ActivityDetailResponse response = new ActivityDetailResponse();
                    response.setCode(404);
                    response.setMessage("Activity not found");
                    return response;
                });
    }

    @Override
    public void enrollActivity(Long id, String currentUserId) {
        // 添加日志，检查 currentUserId 的值和类型
        System.out.println("enrollActivity called with id: " + id + ", currentUserId: " + currentUserId);

        // 检查 currentUserId 是否为空
        if (currentUserId == null || currentUserId.trim().isEmpty()) {
            System.err.println("Invalid user ID: currentUserId is null or empty");
            throw new RuntimeException("Invalid user ID: cannot be null or empty");
        }

        // 检查 currentUserId 是否为数字
        if (!isNumeric(currentUserId)) {
            System.err.println("Invalid user ID: currentUserId is not numeric");
            throw new RuntimeException("Invalid user ID: must be a numeric value");
        }

        Long userId = Long.valueOf(currentUserId);

        // 检查活动是否存在
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> {
                    System.err.println("Activity not found with id: " + id);
                    return new RuntimeException("Activity not found");
                });

        // 检查用户是否已报名
        if (enrollmentRepository.existsByUserIdAndActivityId(userId, id)) {
            System.err.println("User already enrolled: userId=" + userId + ", activityId=" + id);
            throw new RuntimeException("Already enrolled");
        }

        // 检查活动是否还有名额
        if (activity.getCurrentEnrollment() >= activity.getRecruitmentQuota()) {
            System.err.println("No available slots for activityId: " + id);
            throw new RuntimeException("No available slots");
        }

        // 创建报名记录
        Enrollment enrollment = new Enrollment();
        enrollment.setUserId(userId);
        enrollment.setActivityId(id);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setStatus("enrolled"); // 修复：设置报名状态

        enrollmentRepository.save(enrollment);
        System.out.println("Enrollment saved: userId=" + userId + ", activityId=" + id);

        // 更新活动的当前报名人数
        activity.setCurrentEnrollment(activity.getCurrentEnrollment() + 1);
        activityRepository.save(activity);
        System.out.println("Activity updated: activityId=" + id + ", currentEnrollment=" + activity.getCurrentEnrollment());
    }
    @Override
    @Transactional // 保证取消报名操作在事务中执行，避免EntityManager错误
    public void unenrollActivity(Long id, String currentUserId) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        if (!isNumeric(currentUserId)) {
            throw new RuntimeException("Invalid user ID");
        }

        enrollmentRepository.deleteByUserIdAndActivityId(Long.valueOf(currentUserId), id);

        if (activity.getCurrentEnrollment() > 0) {
            activity.setCurrentEnrollment(activity.getCurrentEnrollment() - 1);
            activityRepository.save(activity);
        }
    }

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

    private boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}