package com.student.webproject.admin.service.impl;

import com.student.webproject.admin.dto.ActivityCreateDTO;
import com.student.webproject.admin.entity.Activity;
import com.student.webproject.admin.mapper.ActivityMapper;
import com.student.webproject.admin.service.ActivityAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ActivityAdminServiceImpl implements ActivityAdminService {

    // 3. 像之前注入 Service 一样，注入我们的数据库操作手 Mapper
    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public String createActivity(ActivityCreateDTO dto) {
        // --- 第1步：数据转换 (DTO -> Entity) ---
        // 我们不能直接把 DTO 存入数据库，必须先把它转换成数据库认识的 Entity
        Activity activity = new Activity();

        // 把 DTO 里的数据，一个个地搬到 Entity 对象里
        activity.setTitle(dto.getTitle());
        activity.setDescription(dto.getDescription());
        activity.setCoverImageUrl(dto.getCoverImageUrl());
        activity.setCategory(dto.getCategory());
        activity.setLocation(dto.getLocation());
        activity.setOrganizerId(dto.getOrganizerId());
        activity.setRecruitmentQuota(dto.getRecruitmentQuota());

        // --- 第2步：处理特殊数据和设置默认值 ---

        // 将字符串格式的时间，转换为数据库需要的 LocalDateTime 类型
        // 定义一个时间格式化器，要和前端传来的格式完全一致
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        activity.setStartTime(LocalDateTime.parse(dto.getStartTime(), formatter));
        activity.setEndTime(LocalDateTime.parse(dto.getEndTime(), formatter));

        // 设置一些 DTO 里没有，但数据库里需要的默认值
        activity.setCurrentEnrollment(0); // 新活动当前报名人数为0
        activity.setStatus("recruiting"); // 默认状态为“招募中”
        activity.setCreatedBy(1L); // 临时硬编码，假设创建者是ID为1的管理员
        activity.setCreatedAt(LocalDateTime.now()); // 创建时间为当前时间
        activity.setUpdatedAt(LocalDateTime.now()); // 更新时间也为当前时间

        // --- 第3步：调用 Mapper 执行数据库插入操作 ---
        // activityMapper.insert() 是 MyBatis-Plus 免费送给我们的方法
        // 它会自动生成一条 INSERT SQL 语句
        int rows = activityMapper.insert(activity);

        // --- 第4步：根据操作结果，返回不同的信息 ---
        if (rows > 0) {
            // 如果插入成功，rows 会大于0
            System.out.println("成功向数据库插入一条新活动！活动ID为: " + activity.getId());
            return "活动发布成功！";
        } else {
            // 如果插入失败
            return "活动发布失败，请检查后台日志。";
        }
    }
}