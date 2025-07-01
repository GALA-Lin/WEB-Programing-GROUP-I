// 文件路径: src/main/java/com/student/webproject/admin/service/ActivityAdminService.java
package com.student.webproject.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.student.webproject.admin.dto.ActivityCreateDTO;
import com.student.webproject.admin.entity.Activity;
import com.student.webproject.common.response.Result; // 1. 导入我们新建的 Result 类

/**
 * 活动管理后台服务的接口（工作职责说明书）
 *
 * 【核心变化】：所有方法的返回值，都从 String 或 Object，改为了 Result<T> 类型。
 */
public interface ActivityAdminService {

    /**
     * 定义一个“创建新活动”的职责
     * @param dto 包含了新活动信息的数据包裹
     * @return 【修改】返回一个 Result 包裹，里面的 data 是新创建的 Activity 对象。
     */
    Result<Activity> createActivity(ActivityCreateDTO dto);

    /**
     * 新增：定义一个“更新活动”的职责
     * @param id 要更新的活动的ID
     * @param dto 包含了更新后信息的数据包裹
     * @return 【修改】返回一个 Result 包裹，里面的 data 是更新后的 Activity 对象。
     */
    Result<Activity> updateActivity(Long id, ActivityCreateDTO dto);

    /**
     * 新增：定义一个“删除活动”的职责
     * @param id 要删除的活动的ID
     * @return 【修改】返回一个 Result 包裹。因为删除操作不需要返回额外数据，所以我们用 Result<Void> 表示 data 部分为空。
     */
    Result<Void> deleteActivity(Long id);

    /**
     * 新增：定义一个“查询活动列表（分页）”的职责
     * @param page 当前页码
     * @param pageSize 每页数量
     * @return 【修改】返回一个 Result 包裹，里面的 data 是 MyBatis-Plus 的分页对象 IPage<Activity>。
     */
    Result<IPage<Activity>> listActivities(Long page, Long pageSize);
}