package com.student.webproject.news;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface NewsMapper {

    /**
     * 分页查询新闻列表, 并关联查询作者名
     */
    @Select("SELECT " +
            "  n.id, n.title, n.summary, n.likes_count, n.comments_count, n.published_at, " +
            "  COALESCE(u.real_name, u.username) AS author_name " +
            "FROM news n " +
            "LEFT JOIN users u ON n.author_id = u.id " +
            "ORDER BY n.published_at DESC " +
            "LIMIT #{limit} OFFSET #{offset}")
    List<News> findListByPage(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询新闻总数
     */
    @Select("SELECT COUNT(*) FROM news")
    long countAll();

    /**
     * 根据ID查询新闻详情, 并关联查询作者名
     */
    @Select("SELECT " +
            "  n.*, " +
            "  COALESCE(u.real_name, u.username) AS author_name " +
            "FROM news n " +
            "LEFT JOIN users u ON n.author_id = u.id " +
            "WHERE n.id = #{id}")
    News findById(Long id);

    /**
     * 为指定ID的新闻增加点赞数
     */
    @Update("UPDATE news SET likes_count = likes_count + 1 WHERE id = #{id}")
    int incrementLikesCount(Long id);

    /**
     * 获取指定ID新闻的最新点赞数
     */
    @Select("SELECT likes_count FROM news WHERE id = #{id}")
    Integer findLikesCountById(Long id);
}