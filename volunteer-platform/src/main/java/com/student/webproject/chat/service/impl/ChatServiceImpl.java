package com.student.webproject.chat.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.webproject.chat.dto.ChatMessageRequest;
import com.student.webproject.chat.dto.ChatMessageResponse;
import com.student.webproject.chat.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ChatServiceImpl implements ChatService {
    private static final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);

    // 使用YunWu AI的API URL，确保是正确的API端点
    @Value("${ai.model.api.url}")
    private String apiUrl;

    @Value("${ai.model.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // 备用响应库，当外部API调用失败时使用
    private final Map<String, String[]> fallbackResponses;
    private final Random random = new Random();

    public ChatServiceImpl() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.fallbackResponses = initializeFallbackResponses();
    }

    private Map<String, String[]> initializeFallbackResponses() {
        Map<String, String[]> responses = new HashMap<>();
        responses.put("default", new String[] {
                "您好，我是油炬智愿的AI助手。抱歉，我暂时无法连接到AI服务。请询问关于志愿服务的问题，我会尽力以本地知识库回答。",
                "欢迎使用油炬智愿平台！很抱歉，AI服务暂时不可用。请问有什么关于志愿服务的问题需要帮助吗？"
        });
        return responses;
    }

    @Override
    public ChatMessageResponse processMessage(ChatMessageRequest request) {
        logger.info("收到聊天请求: {}", request.getMessage());

        try {
            // 验证API密钥是否设置
            if (apiKey == null || apiKey.isEmpty() || apiKey.equals("")) {
                logger.error("API密钥未配置，无法调用外部服务");
                return new ChatMessageResponse(
                        "抱歉，AI服务未正确配置。请联系管理员设置有效的API密钥。",
                        LocalDateTime.now()
                );
            }

            // 准备请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);
            logger.debug("准备请求头: {}", headers);

            // 准备请求体 - 根据YunWu AI的API规范调整
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek-v3");  // 使用正确的模型名称

            List<Map<String, String>> messages = new ArrayList<>();

            // 添加系统角色消息
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "你是油炬智愿平台的AI助手，专门回答关于志愿服务的问题。回答要简洁、友善且专业。");
            messages.add(systemMessage);

            // 添加用户的实际问题
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", request.getMessage());
            messages.add(userMessage);

            requestBody.put("messages", messages);
            // 调整参数以适应YunWu API
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 2000);

            // 创建HTTP实体
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            logger.debug("准备发送请求到: {}", apiUrl);

            try {
                // 发送请求到YunWu AI API
                ResponseEntity<String> rawResponse = restTemplate.exchange(
                        apiUrl,
                        HttpMethod.POST,
                        entity,
                        String.class
                );

                logger.debug("收到原始响应: {}", rawResponse.getBody());

                // 检查响应状态码
                if (rawResponse.getStatusCode() == HttpStatus.OK && rawResponse.getBody() != null) {
                    String responseBody = rawResponse.getBody();

                    try {
                        // 使用Jackson正确解析JSON响应
                        JsonNode rootNode = objectMapper.readTree(responseBody);
                        JsonNode choicesNode = rootNode.path("choices");

                        if (choicesNode.isArray() && choicesNode.size() > 0) {
                            JsonNode messageNode = choicesNode.get(0).path("message");
                            String content = messageNode.path("content").asText();

                            if (content != null && !content.isEmpty()) {
                                return new ChatMessageResponse(content, LocalDateTime.now());
                            }
                        }
                    } catch (Exception e) {
                        logger.error("解析JSON响应失败: {}", e.getMessage(), e);
                    }
                }

                // 如果无法解析响应，返回一个通用错误消息
                logger.warn("无法从API响应中提取内容");
                return getFallbackResponse();

            } catch (HttpStatusCodeException e) {
                // 捕获HTTP错误状态码
                logger.error("API请求失败，状态码: {}, 响应: {}", e.getStatusCode(), e.getResponseBodyAsString());
                return new ChatMessageResponse(
                        "抱歉，AI服务暂时不可用 (HTTP错误: " + e.getStatusCode() + ")。请稍后再试。",
                        LocalDateTime.now()
                );
            }

        } catch (RestClientException e) {
            // 捕获RestTemplate客户端异常
            logger.error("RestTemplate客户端异常: {}", e.getMessage(), e);
            return getFallbackResponse();

        } catch (Exception e) {
            // 捕获所有其他异常
            logger.error("处理消息时发生未预期的错误: {}", e.getMessage(), e);
            return new ChatMessageResponse(
                    "抱歉，AI助手遇到了技术问题。请稍后再试。",
                    LocalDateTime.now()
            );
        }
    }

    private ChatMessageResponse getFallbackResponse() {
        String[] defaultResponses = fallbackResponses.get("default");
        String response = defaultResponses[random.nextInt(defaultResponses.length)];
        return new ChatMessageResponse(response, LocalDateTime.now());
    }
}
