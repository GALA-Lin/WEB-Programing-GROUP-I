package com.student.webproject.activity.dto;

import java.util.List;

public class ActivityListResponse {
    private int code;
    private String message;
    private Data data;

    // Getters and Setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private int total;
        private List<ActivityItem> list;

        // Getters and Setters
        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ActivityItem> getList() {
            return list;
        }

        public void setList(List<ActivityItem> list) {
            this.list = list;
        }
    }

    public static class ActivityItem {
        private Long id;
        private String title;
        private String description;
        private String coverImageUrl;
        private String category;
        private String location;
        private String startTime;
        private String endTime;
        private Integer recruitmentQuota;
        private Integer currentEnrollment;
        private String status;

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }


        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCoverImageUrl() {
            return coverImageUrl;
        }

        public void setCoverImageUrl(String coverImageUrl) {
            this.coverImageUrl = coverImageUrl;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }


        public Integer getRecruitmentQuota() {
            return recruitmentQuota;
        }

        public void setRecruitmentQuota(Integer recruitmentQuota) {
            this.recruitmentQuota = recruitmentQuota;
        }

        public Integer getCurrentEnrollment() {
            return currentEnrollment;
        }

        public void setCurrentEnrollment(Integer currentEnrollment) {
            this.currentEnrollment = currentEnrollment;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}