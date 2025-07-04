// 文件路径: volunteer-platform/src/main/java/com/student/webproject/user/entity/ServiceRecord.java
package com.student.webproject.user.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("service_records")
public class ServiceRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long activityId;

    private BigDecimal serviceHours;
    private String recordMethod;
    private Long recordedBy;
    private String remarks;
    private LocalDateTime recordedAt;
}