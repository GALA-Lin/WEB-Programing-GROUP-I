package com.student.webproject.activity.service.impl;

import com.student.webproject.activity.dto.ActivityListResponse;
import com.student.webproject.activity.dto.ActivityDetailResponse;
import com.student.webproject.activity.service.ActivityService;
import com.student.webproject.activity.entity.Activity;
import com.student.webproject.activity.entity.Enrollment;
import com.student.webproject.activity.mapper.ActivityDao;
import com.student.webproject.activity.mapper.EnrollmentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityDao activityDao;
    private final EnrollmentMapper enrollmentMapper;

    public ActivityServiceImpl(ActivityDao activityDao, EnrollmentMapper enrollmentMapper) {
        this.activityDao = activityDao;
        this.enrollmentMapper = enrollmentMapper;
        if (activityDao == null || enrollmentMapper == null) {
            throw new IllegalArgumentException("Mappers cannot be null");
        }
    }

    @Override
    public ActivityListResponse getActivities(int page, int pageSize, String category) {
        if (page <= 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Page and pageSize must be positive numbers");
        }
        int offset = (page - 1) * pageSize;
        List<Activity> activityList = activityDao.selectActivities(offset, pageSize, category);
        int total = activityDao.countActivities(category);

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
    public ActivityDetailResponse getActivityById(Long id, String currentUserId) {
        if (id == null) {
            ActivityDetailResponse response = new ActivityDetailResponse();
            response.setCode(400);
            response.setMessage("Invalid activity ID");
            return response;
        }
        Activity activity = activityDao.selectById(id);
        if (activity == null) {
            ActivityDetailResponse response = new ActivityDetailResponse();
            response.setCode(404);
            response.setMessage("Activity not found");
            return response;
        }
        ActivityDetailResponse response = new ActivityDetailResponse();
        response.setCode(200);
        response.setMessage("Success");
        ActivityDetailResponse.Data data = new ActivityDetailResponse.Data();
        mapToActivityData(activity, data);
        boolean isEnrolled = false;
        if (isNumeric(currentUserId)) {
            isEnrolled = enrollmentMapper.existsByUserIdAndActivityId(Long.valueOf(currentUserId), id);
        }
        data.setIsEnrolled(isEnrolled);
        response.setData(data);
        return response;
    }

    @Override
    public void enrollActivity(Long id, String currentUserId) {
        System.out.println("enrollActivity called with id: " + id + ", currentUserId: " + currentUserId);
        if (currentUserId == null || currentUserId.trim().isEmpty()) {
            System.err.println("Invalid user ID: currentUserId is null or empty");
            throw new RuntimeException("Invalid user ID: cannot be null or empty");
        }
        if (!isNumeric(currentUserId)) {
            System.err.println("Invalid user ID: currentUserId is not numeric");
            throw new RuntimeException("Invalid user ID: must be a numeric value");
        }
        Long userId = Long.valueOf(currentUserId);
        Activity activity = activityDao.selectById(id);
        if (activity == null) {
            System.err.println("Activity not found with id: " + id);
            throw new RuntimeException("Activity not found");
        }
        if (enrollmentMapper.existsByUserIdAndActivityId(userId, id)) {
            System.err.println("User already enrolled: userId=" + userId + ", activityId=" + id);
            throw new RuntimeException("Already enrolled");
        }
        if (activity.getCurrentEnrollment() >= activity.getRecruitmentQuota()) {
            System.err.println("No available slots for activityId: " + id);
            throw new RuntimeException("No available slots");
        }
        Enrollment enrollment = new Enrollment();
        enrollment.setUserId(userId);
        enrollment.setActivityId(id);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setStatus("enrolled");
        enrollmentMapper.insertEnrollment(enrollment);
        System.out.println("Enrollment saved: userId=" + userId + ", activityId=" + id);
        activity.setCurrentEnrollment(activity.getCurrentEnrollment() + 1);
        activityDao.updateCurrentEnrollment(id, activity.getCurrentEnrollment());
        System.out.println("Activity updated: activityId=" + id + ", currentEnrollment=" + activity.getCurrentEnrollment());
    }

    @Override
    @Transactional
    public void unenrollActivity(Long id, String currentUserId) {
        Activity activity = activityDao.selectById(id);
        if (activity == null) {
            throw new RuntimeException("Activity not found");
        }
        if (!isNumeric(currentUserId)) {
            throw new RuntimeException("Invalid user ID");
        }
        enrollmentMapper.deleteByUserIdAndActivityId(Long.valueOf(currentUserId), id);
        if (activity.getCurrentEnrollment() > 0) {
            activity.setCurrentEnrollment(activity.getCurrentEnrollment() - 1);
            activityDao.updateCurrentEnrollment(id, activity.getCurrentEnrollment());
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