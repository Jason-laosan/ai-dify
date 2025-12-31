package com.example.ai.controller;

import com.example.ai.dto.ApiResponse;
import com.example.ai.dto.ChatRequest;
import com.example.ai.dto.ChatResponse;
import com.example.ai.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@Tag(name = "聊天接口", description = "AI聊天相关接口")
public class ChatController {

    private final ChatService difyChatService;
    private final ChatService langChainChatService;

    public ChatController(
            @Qualifier("difyChatService") ChatService difyChatService,
            @Qualifier("langChainChatService") ChatService langChainChatService) {
        this.difyChatService = difyChatService;
        this.langChainChatService = langChainChatService;
    }

    @PostMapping("/dify")
    @Operation(summary = "Dify聊天", description = "通过Dify平台进行AI对话")
    public ChatResponse chatWithDify(@Valid @RequestBody ChatRequest request) {
        log.info("Received Dify chat request: {}", request.getMessage());
        return difyChatService.chat(request);
    }

    @PostMapping(value = "/dify/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Dify流式聊天", description = "通过Dify平台进行流式AI对话")
    public Flux<String> chatStreamWithDify(@Valid @RequestBody ChatRequest request) {
        log.info("Received Dify streaming chat request: {}", request.getMessage());
        return difyChatService.chatStream(request);
    }

    @PostMapping("/langchain")
    @Operation(summary = "LangChain聊天", description = "通过LangChain4j进行AI对话")
    public ChatResponse chatWithLangChain(@Valid @RequestBody ChatRequest request) {
        log.info("Received LangChain chat request: {}", request.getMessage());
        return langChainChatService.chat(request);
    }

    @PostMapping("/dify/mvc")
    @Operation(summary = "Dify聊天（Spring MVC封装）", description = "通过Dify平台进行AI对话，返回标准ApiResponse封装结果")
    public ApiResponse<ChatResponse> chatWithDifyMvc(@Valid @RequestBody ChatRequest request) {
        log.info("Received Dify MVC chat request: {}", request.getMessage());
        return ApiResponse.ok(difyChatService.chat(request));
    }

    @PostMapping("/langchain/mvc")
    @Operation(summary = "LangChain聊天（Spring MVC封装）", description = "通过LangChain4j进行AI对话，返回标准ApiResponse封装结果")
    public ApiResponse<ChatResponse> chatWithLangChainMvc(@Valid @RequestBody ChatRequest request) {
        log.info("Received LangChain MVC chat request: {}", request.getMessage());
        return ApiResponse.ok(langChainChatService.chat(request));
    }

    @PostMapping(value = "/langchain/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "LangChain流式聊天", description = "通过LangChain4j进行流式AI对话")
    public Flux<String> chatStreamWithLangChain(@Valid @RequestBody ChatRequest request) {
        log.info("Received LangChain streaming chat request: {}", request.getMessage());
        return langChainChatService.chatStream(request);
    }

    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查聊天服务是否可用")
    public String health() {
        return "OK";
    }
}
