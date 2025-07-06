package com.student.webproject.chat.service;

import com.student.webproject.chat.dto.ChatMessageRequest;
import com.student.webproject.chat.dto.ChatMessageResponse;

public interface ChatService {
    ChatMessageResponse processMessage(ChatMessageRequest request);
}