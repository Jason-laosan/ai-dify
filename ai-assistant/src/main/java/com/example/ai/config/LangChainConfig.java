package com.example.ai.config;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LangChainConfig {

    private final AiProperties aiProperties;

    @Bean
    @ConditionalOnProperty(prefix = "ai.openai", name = "enabled", havingValue = "true", matchIfMissing = true)
    public ChatLanguageModel chatLanguageModel() {
        log.info("Initializing OpenAI ChatLanguageModel with model: {}", aiProperties.getOpenai().getModel());
        
        return OpenAiChatModel.builder()
                .apiKey(aiProperties.getOpenai().getApiKey())
                .baseUrl(aiProperties.getOpenai().getBaseUrl())
                .modelName(aiProperties.getOpenai().getModel())
                .temperature(aiProperties.getOpenai().getTemperature())
                .maxTokens(aiProperties.getOpenai().getMaxTokens())
                .timeout(Duration.ofSeconds(60))
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "ai.openai", name = "enabled", havingValue = "true", matchIfMissing = true)
    public StreamingChatLanguageModel streamingChatLanguageModel() {
        log.info("Initializing OpenAI StreamingChatLanguageModel");
        
        return OpenAiStreamingChatModel.builder()
                .apiKey(aiProperties.getOpenai().getApiKey())
                .baseUrl(aiProperties.getOpenai().getBaseUrl())
                .modelName(aiProperties.getOpenai().getModel())
                .temperature(aiProperties.getOpenai().getTemperature())
                .timeout(Duration.ofSeconds(120))
                .build();
    }

    @Bean
    public MessageWindowChatMemory chatMemory() {
        return MessageWindowChatMemory.withMaxMessages(20);
    }
}
