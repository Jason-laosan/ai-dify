package com.example.ai.controller;

import com.example.ai.dto.AgentRequest;
import com.example.ai.dto.AgentResponse;
import com.example.ai.dto.ApiResponse;
import com.example.ai.service.AgentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/agent")
@RequiredArgsConstructor
@Tag(name = "Agent接口", description = "AI Agent相关接口")
public class AgentController {

    private final AgentService agentService;

    @PostMapping("/execute")
    @Operation(summary = "执行Agent任务", description = "让AI Agent执行指定任务")
    public ApiResponse<AgentResponse> executeTask(@Valid @RequestBody AgentRequest request) {
        log.info("Received agent task request: {}", request.getTask());
        return ApiResponse.ok(agentService.executeTask(request));
    }

    @PostMapping("/java-assist")
    @Operation(summary = "Java开发助手", description = "专门用于Java开发相关问题的AI助手")
    public ApiResponse<AgentResponse> javaAssist(@RequestParam String question) {
        log.info("Received Java assist request: {}", question);
        
        AgentRequest request = AgentRequest.builder()
                .task(question)
                .agentType(AgentRequest.AgentType.JAVA_ASSISTANT)
                .build();
        
        return ApiResponse.ok(agentService.executeTask(request));
    }

    @PostMapping("/code-review")
    @Operation(summary = "代码审查", description = "AI代码审查服务")
    public ApiResponse<AgentResponse> codeReview(@RequestBody String code) {
        log.info("Received code review request");
        
        AgentRequest request = AgentRequest.builder()
                .task("请审查以下代码并提供改进建议：\n" + code)
                .agentType(AgentRequest.AgentType.CODE_REVIEWER)
                .build();
        
        return ApiResponse.ok(agentService.executeTask(request));
    }
}
