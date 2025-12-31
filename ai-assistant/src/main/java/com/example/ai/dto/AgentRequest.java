package com.example.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentRequest {

    @NotBlank(message = "任务描述不能为空")
    private String task;

    private String userId;

    private List<String> enabledTools;

    private AgentType agentType;

    public enum AgentType {
        JAVA_ASSISTANT,
        CODE_REVIEWER,
        DOCUMENT_QA,
        MULTI_AGENT
    }
}
