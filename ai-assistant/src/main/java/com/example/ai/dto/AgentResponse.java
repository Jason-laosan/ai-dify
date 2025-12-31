package com.example.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentResponse {

    private String taskId;

    private String result;

    private List<ToolExecution> toolExecutions;

    private List<AgentStep> steps;

    private boolean success;

    private String errorMessage;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ToolExecution {
        private String toolName;
        private String input;
        private String output;
        private long durationMs;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgentStep {
        private int stepNumber;
        private String agentName;
        private String thought;
        private String action;
        private String result;
    }
}
