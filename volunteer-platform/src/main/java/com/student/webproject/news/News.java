package com.student.webproject.news;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("news")
public class News {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;
    private String summary;
    private String content;

    @TableField("author_id")
    private Long authorId;

    @TableField("likes_count")
    private Integer likesCount;

    @TableField("comments_count")
    private Integer commentsCount;

    @TableField("favorites_count")
    private Integer favoritesCount;

    @TableField("published_at")
    private LocalDateTime publishedAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @TableField(exist = false)
    private String authorName;
}