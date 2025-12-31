package com.example.ai.service.impl;

import com.example.ai.client.DifyClient;
import com.example.ai.dto.ChatRequest;
import com.example.ai.dto.ChatResponse;
import com.example.ai.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service("difyChatService")
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "ai.dify", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DifyChatService implements ChatService {

    private final DifyClient difyClient;

    @Override
    public ChatResponse chat(ChatRequest request) {
        log.info("Processing chat request via Dify: {}", request.getMessage());
        
        DifyClient.DifyResponse difyResponse = difyClient.sendMessage(
                request.getMessage(),
                request.getUserId(),
                request.getConversationId(),
                request.getInputs()
        );

        if (difyResponse.isSuccess()) {
            return ChatResponse.builder()
                    .answer(difyResponse.getAnswer())
                    .conversationId(difyResponse.getConversationId())
                    .messageId(difyResponse.getMessageId())
                    .success(true)
                    .build();
        } else {
            return ChatResponse.error(difyResponse.getErrorMessage());
        }
    }

    @Override
    public Flux<String> chatStream(ChatRequest request) {
        log.info("Processing streaming chat request via Dify: {}", request.getMessage());
        
        return difyClient.sendMessageStream(
                request.getMessage(),
                request.getUserId(),
                request.getConversationId(),
                request.getInputs()
        );
    }
}
