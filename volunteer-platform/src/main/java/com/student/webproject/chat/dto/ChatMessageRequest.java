package com.student.webproject.chat.dto;

import lombok.Getter;

@Getter
public class ChatMessageRequest {
    private String message;

    // 构造函数、getter 和 setter
    public ChatMessageRequest() {}

    public ChatMessageRequest(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}