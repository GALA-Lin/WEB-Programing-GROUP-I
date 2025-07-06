package com.student.webproject.user.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String studentId;
    private String username;

    @JsonIgnore
    private String password;

    private String realName;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
    private String role;
    private Integer status;
    private LocalDateTime createdAt;

    // 确保 updatedAt 字段存在，并使用 @TableField 注解
    @TableField(update = "now()")
    private LocalDateTime updatedAt;

    // 确保 totalServiceHours 字段存在，并且类型是 BigDecimal
    @TableField("total_service_hours")
    private BigDecimal totalServiceHours;
}