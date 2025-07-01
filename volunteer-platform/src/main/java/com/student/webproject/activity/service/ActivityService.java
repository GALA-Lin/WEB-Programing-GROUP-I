package com.student.webproject.activity.service;

import com.student.webproject.activity.dto.ActivityListResponse;
import com.student.webproject.activity.dto.ActivityDetailResponse;

public interface ActivityService {
    ActivityListResponse getActivities(int page, int pageSize, String category);
    ActivityDetailResponse getActivityById(Long id, String currentUserId);
    void enrollActivity(Long id, String currentUserId);
    void unenrollActivity(Long id, String currentUserId);
}