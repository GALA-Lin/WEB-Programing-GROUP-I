package com.student.webproject.activity.mapper;

import com.student.webproject.activity.entity.Activity;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ActivityMapper {
    @Select({
        "<script>",
        "SELECT * FROM activities",
        "<if test='category != null and category != \"\"'>",
        "WHERE category = #{category}",
        "</if>",
        "ORDER BY id DESC",
        "LIMIT #{offset}, #{pageSize}",
        "</script>"
    })
    List<Activity> selectActivities(@Param("offset") int offset, @Param("pageSize") int pageSize, @Param("category") String category);

    @Select({
        "<script>",
        "SELECT COUNT(*) FROM activities",
        "<if test='category != null and category != \"\"'>",
        "WHERE category = #{category}",
        "</if>",
        "</script>"
    })
    int countActivities(@Param("category") String category);

    @Select("SELECT * FROM activities WHERE id = #{id}")
    Activity selectById(@Param("id") Long id);

    @Update("UPDATE activities SET current_enrollment = #{currentEnrollment} WHERE id = #{id}")
    int updateCurrentEnrollment(@Param("id") Long id, @Param("currentEnrollment") int currentEnrollment);

    @Update("UPDATE activities SET title=#{title}, description=#{description}, cover_image_url=#{coverImageUrl}, category=#{category}, location=#{location}, start_time=#{startTime}, end_time=#{endTime}, recruitment_quota=#{recruitmentQuota}, current_enrollment=#{currentEnrollment}, status=#{status}, organizer_id=#{organizerId}, created_by=#{createdBy}, created_at=#{createdAt}, updated_at=#{updatedAt} WHERE id=#{id}")
    int updateActivity(Activity activity);

    @Insert("INSERT INTO activities (title, description, cover_image_url, category, location, start_time, end_time, recruitment_quota, current_enrollment, status, organizer_id, created_by, created_at, updated_at) VALUES (#{title}, #{description}, #{coverImageUrl}, #{category}, #{location}, #{startTime}, #{endTime}, #{recruitmentQuota}, #{currentEnrollment}, #{status}, #{organizerId}, #{createdBy}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertActivity(Activity activity);
}

