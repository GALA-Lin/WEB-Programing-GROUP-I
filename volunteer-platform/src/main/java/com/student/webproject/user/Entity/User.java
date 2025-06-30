package com.student.webproject.user.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
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

    // 数据库是 student_id, MyBatis Plus 会自动将驼峰命名 studentId 映射过去
    private String studentId;

    private String username;

    @JsonIgnore // 关键: 禁止将此字段返回给前端，确保密码安全
    private String password;

    private String realName;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 累计志愿服务总时长
     */
    private BigDecimal totalServiceHours;

    /**
     * 账户状态 (1:正常, 0:禁用)
     */
    private Integer status;
}
