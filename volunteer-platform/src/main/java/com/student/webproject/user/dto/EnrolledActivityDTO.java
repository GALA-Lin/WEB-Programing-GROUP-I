// 文件路径: volunteer-platform/src/main/java/com/student/webproject/user/dto/EnrolledActivityDTO.java
package com.student.webproject.user.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EnrolledActivityDTO {

    private Long activityId;

    private String title;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
}