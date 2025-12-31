package com.example.ai.service;

import com.example.ai.dto.ChatRequest;
import com.example.ai.dto.ChatResponse;
import reactor.core.publisher.Flux;

public interface ChatService {

    ChatResponse chat(ChatRequest request);

    Flux<String> chatStream(ChatRequest request);
}
