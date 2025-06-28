package com.student.webproject.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.student.webproject.admin.entity.Activity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 活动表的 Mapper 接口 (数据库操作手)
 * @Mapper 注解告诉 Spring Boot 和 MyBatis-Plus: "这是一个操作数据库的 Mapper 接口，请帮我实现它。"
 */
@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {

    // 目前这里是空的
    // 通过继承 BaseMapper<Activity>，我们就自动拥有了对 Activity 实体的
    // 增、删、改、查等一系列常用方法，比如 insert(), deleteById(), updateById(), selectById() 等。
    // 我们暂时不需要在这里手写任何方法。

}