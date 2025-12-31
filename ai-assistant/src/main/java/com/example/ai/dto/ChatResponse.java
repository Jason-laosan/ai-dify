package com.example.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    private String messageId;

    private String conversationId;

    private String answer;

    private String model;

    private Map<String, Object> metadata;

    private Usage usage;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private boolean success;

    private String errorMessage;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Usage {
        private int promptTokens;
        private int completionTokens;
        private int totalTokens;
    }

    public static ChatResponse success(String answer, String conversationId) {
        return ChatResponse.builder()
                .answer(answer)
                .conversationId(conversationId)
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ChatResponse error(String errorMessage) {
        return ChatResponse.builder()
                .success(false)
                .errorMessage(errorMessage)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
