package com.example.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

    @NotBlank(message = "消息内容不能为空")
    private String message;

    private String userId;

    private String conversationId;

    @Builder.Default
    private Map<String, Object> inputs = new HashMap<>();

    @Builder.Default
    private boolean stream = false;
}
