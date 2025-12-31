package com.example.ai.service.impl;

import com.example.ai.dto.AgentRequest;
import com.example.ai.dto.AgentResponse;
import com.example.ai.service.AgentService;
import com.example.ai.tools.JavaDevTools;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "ai.openai", name = "enabled", havingValue = "true", matchIfMissing = true)
public class JavaAssistantAgentService implements AgentService {

    private final ChatLanguageModel chatLanguageModel;
    private final JavaDevTools javaDevTools;

    private JavaAssistantAI javaAssistant;

    @PostConstruct
    public void init() {
        log.info("Initializing Java Assistant Agent with tools");
        
        javaAssistant = AiServices.builder(JavaAssistantAI.class)
                .chatLanguageModel(chatLanguageModel)
                .tools(javaDevTools)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                .build();
    }

    @SystemMessage("""
            你是一个专业的Java开发助手，具有以下能力：
            1. 搜索Java官方文档
            2. 查询Spring Boot配置属性
            3. 分析和审查Java代码
            4. 生成Maven依赖配置
            5. 执行数学计算
            
            请根据用户的需求，合理使用工具来完成任务。
            回答时请使用中文，保持专业和友好。
            """)
    interface JavaAssistantAI {
        String chat(String userMessage);
    }

    @Override
    public AgentResponse executeTask(AgentRequest request) {
        log.info("Executing agent task: {}", request.getTask());
        
        try {
            long startTime = System.currentTimeMillis();
            
            String result = javaAssistant.chat(request.getTask());
            
            long durationMs = System.currentTimeMillis() - startTime;
            log.info("Agent task completed in {} ms", durationMs);

            List<AgentResponse.AgentStep> steps = new ArrayList<>();
            steps.add(AgentResponse.AgentStep.builder()
                    .stepNumber(1)
                    .agentName("JavaAssistant")
                    .thought("分析用户需求")
                    .action("处理任务")
                    .result(result)
                    .build());

            return AgentResponse.builder()
                    .taskId(UUID.randomUUID().toString())
                    .result(result)
                    .steps(steps)
                    .success(true)
                    .build();

        } catch (Exception e) {
            log.error("Agent task execution failed", e);
            return AgentResponse.builder()
                    .taskId(UUID.randomUUID().toString())
                    .success(false)
                    .errorMessage("任务执行失败: " + e.getMessage())
                    .build();
        }
    }
}
