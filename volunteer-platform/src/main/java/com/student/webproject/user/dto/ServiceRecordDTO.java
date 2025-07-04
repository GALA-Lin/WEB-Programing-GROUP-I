// 文件路径: volunteer-platform/src/main/java/com/student/webproject/user/dto/ServiceRecordDTO.java
package com.student.webproject.user.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ServiceRecordDTO {

    private Long activityId;

    private String activityTitle;
    private BigDecimal serviceHours;
    private LocalDateTime recordedAt;
    private String remarks;
}