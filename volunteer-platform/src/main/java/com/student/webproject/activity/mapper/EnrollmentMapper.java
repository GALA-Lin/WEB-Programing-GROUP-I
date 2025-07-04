package com.student.webproject.activity.mapper;

import com.student.webproject.activity.entity.Enrollment;
import com.student.webproject.user.dto.EnrolledActivityDTO;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface EnrollmentMapper {
    @Select("SELECT * FROM enrollments WHERE user_id = #{userId} AND activity_id = #{activityId} LIMIT 1")
    Enrollment selectByUserIdAndActivityId(@Param("userId") Long userId, @Param("activityId") Long activityId);

    @Select("SELECT COUNT(*) > 0 FROM enrollments WHERE user_id = #{userId} AND activity_id = #{activityId}")
    boolean existsByUserIdAndActivityId(@Param("userId") Long userId, @Param("activityId") Long activityId);

    @Insert("INSERT INTO enrollments (user_id, activity_id, status, enrolled_at) VALUES (#{userId}, #{activityId}, #{status}, #{enrolledAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertEnrollment(Enrollment enrollment);

    @Delete("DELETE FROM enrollments WHERE user_id = #{userId} AND activity_id = #{activityId}")
    int deleteByUserIdAndActivityId(@Param("userId") Long userId, @Param("activityId") Long activityId);

    @Select("SELECT a.id as activityId, a.title, a.location, a.start_time as startTime, a.end_time as endTime, a.status " +
            "FROM enrollments e JOIN activities a ON e.activity_id = a.id " +
            "WHERE e.user_id = #{userId} AND e.status = 'enrolled' " +
            "ORDER BY a.start_time DESC")
    List<EnrolledActivityDTO> findEnrolledActivitiesByUserId(Long userId);
}

