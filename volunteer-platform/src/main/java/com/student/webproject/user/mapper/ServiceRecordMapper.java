// 文件路径: volunteer-platform/src/main/java/com/student/webproject/user/mapper/ServiceRecordMapper.java
package com.student.webproject.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
}