package com.example.ai.service.impl;

import com.example.ai.dto.ChatRequest;
import com.example.ai.dto.ChatResponse;
import com.example.ai.service.ChatService;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.output.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service("langChainChatService")
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "ai.openai", name = "enabled", havingValue = "true", matchIfMissing = true)
public class LangChainChatService implements ChatService {

    private final ChatLanguageModel chatLanguageModel;
    private final StreamingChatLanguageModel streamingChatLanguageModel;
    
    private final Map<String, MessageWindowChatMemory> conversationMemories = new ConcurrentHashMap<>();

    @Override
    public ChatResponse chat(ChatRequest request) {
        log.info("Processing chat request via LangChain4j: {}", request.getMessage());
        
        try {
            String conversationId = request.getConversationId();
            if (conversationId == null || conversationId.isEmpty()) {
                conversationId = UUID.randomUUID().toString();
            }

            MessageWindowChatMemory memory = conversationMemories.computeIfAbsent(
                    conversationId,
                    k -> MessageWindowChatMemory.withMaxMessages(20)
            );

            UserMessage userMessage = UserMessage.from(request.getMessage());
            memory.add(userMessage);

            Response<AiMessage> response = chatLanguageModel.generate(memory.messages());
            AiMessage aiMessage = response.content();
            memory.add(aiMessage);

            return ChatResponse.builder()
                    .answer(aiMessage.text())
                    .conversationId(conversationId)
                    .messageId(UUID.randomUUID().toString())
                    .success(true)
                    .usage(ChatResponse.Usage.builder()
                            .promptTokens(response.tokenUsage() != null ? response.tokenUsage().inputTokenCount() : 0)
                            .completionTokens(response.tokenUsage() != null ? response.tokenUsage().outputTokenCount() : 0)
                            .totalTokens(response.tokenUsage() != null ? response.tokenUsage().totalTokenCount() : 0)
                            .build())
                    .build();
        } catch (Exception e) {
            log.error("Error in LangChain chat", e);
            return ChatResponse.error("处理失败: " + e.getMessage());
        }
    }

    @Override
    public Flux<String> chatStream(ChatRequest request) {
        log.info("Processing streaming chat request via LangChain4j: {}", request.getMessage());
        
        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();

        streamingChatLanguageModel.generate(
                request.getMessage(),
                new dev.langchain4j.model.StreamingResponseHandler<AiMessage>() {
                    @Override
                    public void onNext(String token) {
                        sink.tryEmitNext(token);
                    }

                    @Override
                    public void onComplete(Response<AiMessage> response) {
                        sink.tryEmitComplete();
                    }

                    @Override
                    public void onError(Throwable error) {
                        sink.tryEmitError(error);
                    }
                }
        );

        return sink.asFlux();
    }

    public void clearConversation(String conversationId) {
        conversationMemories.remove(conversationId);
        log.info("Cleared conversation: {}", conversationId);
    }
}
