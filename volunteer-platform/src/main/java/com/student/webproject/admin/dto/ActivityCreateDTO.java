package com.student.webproject.admin.dto;
import lombok.Data;
@Data
public class ActivityCreateDTO {
    private String title;
    private String description;
    private String coverImageUrl;
    private String category;
    private String location;
    private String startTime;
    private String endTime;
    private Long organizerId;
    private Integer recruitmentQuota;
}
