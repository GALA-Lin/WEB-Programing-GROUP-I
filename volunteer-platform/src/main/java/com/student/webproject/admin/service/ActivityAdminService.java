package com.student.webproject.admin.service;

import com.student.webproject.admin.dto.ActivityCreateDTO;

/**
 * 活动管理后台服务的接口（工作职责说明书）
 */
public interface ActivityAdminService {

    /**
     * 定义一个“创建新活动”的职责
     * @param dto 包含了新活动信息的数据包裹
     * @return 返回一个处理结果，我们先用 String 类型代替
     */
    String createActivity(ActivityCreateDTO dto);

    /**
     * 新增：定义一个“更新活动”的职责
     * @param id 要更新的活动的ID
     * @param dto 包含了更新后信息的数据包裹
     * @return 返回一个处理结果
     */
    String updateActivity(Long id, ActivityCreateDTO dto);

    /**
     * 新增：定义一个“删除活动”的职责
     * @param id 要删除的活动的ID
     * @return 返回一个处理结果
     */
    String deleteActivity(Long id);

    /**
     * 新增：定义一个“查询活动列表（分页）”的职责
     * @param page 当前页码
     * @param pageSize 每页数量
     * @return 返回一个包含列表和总数的结果
     */
    Object listActivities(Long page, Long pageSize);
}