// 文件路径: volunteer-platform/src/main/java/com/student/webproject/user/mapper/ServiceRecordMapper.java
package com.student.webproject.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.student.webproject.admin.dto.ServiceRecordViewDTO;
import com.student.webproject.user.dto.ServiceRecordDTO;
import com.student.webproject.user.Entity.ServiceRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ServiceRecordMapper extends BaseMapper<ServiceRecord> {

    @Select("SELECT a.id as activityId, a.title as activityTitle, sr.service_hours as serviceHours, sr.recorded_at as recordedAt, sr.remarks " +
            "FROM service_records sr JOIN activities a ON sr.activity_id = a.id " +
            "WHERE sr.user_id = #{userId} ORDER BY sr.recorded_at DESC")
    List<ServiceRecordDTO> findServiceRecordsByUserId(Long userId);
    /**
     * 分页联表查询，用于后台管理列表
     */
    @Select("SELECT sr.id, sr.user_id, sr.activity_id, u.real_name, a.title as activityTitle, sr.service_hours, sr.remarks, sr.recorded_at " +
            "FROM service_records sr " +
            "LEFT JOIN users u ON sr.user_id = u.id " +
            "LEFT JOIN activities a ON sr.activity_id = a.id " +
            "ORDER BY sr.recorded_at DESC")
    IPage<ServiceRecordViewDTO> selectServiceRecordViewPage(Page<?> page);
}