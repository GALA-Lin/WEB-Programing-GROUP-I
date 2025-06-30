
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
            
            ActivityDetailResponse.Data data = response.new Data();
            mapToActivityData(activity, data);
            
            // 检查用户是否已报名
            boolean isEnrolled = false;
            if (currentUserId != null && !currentUserId.isEmpty()) {
                isEnrolled = enrollmentRepository.existsByUserIdAndActivityId(Long.valueOf(currentUserId), id);
            }
            
            // 使用 isEnrolled 变量
            data.setEnrolled(isEnrolled); // 新增这一行
            
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

    // 新增：设置 organizerName 字段
    Long organizerId = activity.getOrganizerId();
    if (organizerId != null) {
        // 这里简化为直接设置一个示例值，实际应根据 organizerId 查询组织名称
        data.setOrganizerName("Example Organizer Name"); 
    } else {
        data.setOrganizerName(null);
    }
}

// 在 ActivityDetailResponse.Data 中添加 enrolled 字段
public static class Data {
    private boolean enrolled; // 新增字段
    
    // Getter and Setter for enrolled
    public boolean isEnrolled() {
        return enrolled;
    }

    public void setEnrolled(boolean enrolled) {
        this.enrolled = enrolled;
    }
}
