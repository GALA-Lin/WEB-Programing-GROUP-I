package com.student.webproject.news.dto;

import lombok.Data;

@Data
public class NewsCreateDTO {
    private String title;
    private String summary;
    private String content;
}