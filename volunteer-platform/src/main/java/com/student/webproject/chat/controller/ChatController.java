package com.student.webproject.chat.controller;

import com.student.webproject.chat.dto.ChatMessageRequest;
import com.student.webproject.chat.dto.ChatMessageResponse;
import com.student.webproject.chat.service.ChatService;
import com.student.webproject.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/send")
    public ResponseEntity<Result<ChatMessageResponse>> sendMessage(@RequestBody ChatMessageRequest request) {
        ChatMessageResponse response = chatService.processMessage(request);
        return ResponseEntity.ok(Result.success(response));
    }
}
