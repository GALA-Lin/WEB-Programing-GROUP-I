// 文件路径: src/main/java/com/student/webproject/news/News.java

/*
@Entity: 告诉 Spring 这是个实体类，对应数据库里的一张表。

@Table(name = "news"): 指定这张表的名字叫 news。

@Id: 标记这是主键。

@GeneratedValue: 设置主键自增长。

likeCount: 我们加一个 likeCount 字段用来记录点赞数。
 */

package com.student.webproject.news;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // 新闻标题

    @Lob // @Lob 表示这是一个大对象，通常用于存储较长文本
    @Column(nullable = false)
    private String content; // 新闻内容

    private String author; // 作者

    @Column(name = "like_count", nullable = false)
    private Integer likeCount = 0; // 点赞数，默认为0

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 创建时间

    // --- Getters and Setters ---
    // (你可以使用 IDE 自动生成它们)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}