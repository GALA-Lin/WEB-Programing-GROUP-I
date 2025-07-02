package com.student.webproject.news.dto;

import lombok.Data;

@Data
public class NewsDetailDTO {
    private Long id;
    private String title;
    private String summary;
    private String content;
    private String authorName;
    private String publishedAt;
    private Integer likesCount;
    private Integer commentsCount;
    private Integer favoritesCount;
}