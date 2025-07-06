package com.student.webproject.chat.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatMessageResponse {
    // Getters 和 Setters
    private String message;
    private LocalDateTime timestamp;

    // 构造函数
    public ChatMessageResponse() {}

    public ChatMessageResponse(String message, LocalDateTime timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
