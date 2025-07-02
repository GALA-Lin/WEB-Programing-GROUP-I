package com.student.webproject.news;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface NewsMapper extends BaseMapper<News> {

    @Select("SELECT n.id, n.title, n.summary, n.likes_count, n.comments_count, n.published_at, COALESCE(u.real_name, u.username) AS author_name FROM news n LEFT JOIN users u ON n.author_id = u.id ORDER BY n.published_at DESC")
    IPage<News> findNewsPageWithAuthor(Page<News> page);

    @Select("SELECT n.*, COALESCE(u.real_name, u.username) AS author_name FROM news n LEFT JOIN users u ON n.author_id = u.id WHERE n.id = #{id}")
    News findNewsDetailWithAuthor(@Param("id") Long id);
}