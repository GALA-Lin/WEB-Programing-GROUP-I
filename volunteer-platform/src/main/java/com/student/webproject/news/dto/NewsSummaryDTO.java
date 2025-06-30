package com.student.webproject.news.dto;

import lombok.Data;

@Data
public class NewsSummaryDTO {
    private Long id;
    private String title;
    private String summary;
    private String authorName;
    private String publishedAt;
    private Integer likesCount;
    private Integer commentsCount;
}