package com.student.webproject.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 活动实体类，对应数据库中的 `activities` 表
 * @Data 注解 (Lombok): 自动生成 getter/setter 等方法。
 * @TableName("activities") 注解 (MyBatis-Plus):
 * 明确告诉 MyBatis-Plus，这个类对应的是数据库里的 `activities` 这张表。
 */
@Data
@TableName("activities")
public class Activity {

    // 注意：这里的属性名使用小驼峰命名法 (camelCase)
    // MyBatis-Plus 会自动将它映射到数据库表的蛇形命名法 (snake_case)
    // 例如：coverImageUrl 会自动对应 cover_image_url
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    private String title;
    private String description;
    private String coverImageUrl;
    private String category;
    private String location;
    private LocalDateTime startTime; // 使用 Java 8 的时间类型，更专业
    private LocalDateTime endTime;
    private Long organizerId;
    private Integer recruitmentQuota;
    private Integer currentEnrollment;
    private String status;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}