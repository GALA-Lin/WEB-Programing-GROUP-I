package com.student.webproject.activity.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Enrollment {

    private Long id;

    private Long userId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long activityId;

    private String status;
    private LocalDateTime enrolledAt;

}