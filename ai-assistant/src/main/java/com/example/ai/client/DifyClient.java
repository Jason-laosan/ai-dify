package com.example.ai.client;

import com.example.ai.config.AiProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "ai.dify", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DifyClient {

    private final OkHttpClient okHttpClient;
    private final AiProperties aiProperties;
    private final ObjectMapper objectMapper;

    public DifyResponse sendMessage(String query, String userId, String conversationId, Map<String, Object> inputs) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("inputs", inputs != null ? inputs : new HashMap<>());
            requestBody.put("query", query);
            requestBody.put("response_mode", "blocking");
            requestBody.put("user", userId != null ? userId : "default-user");
            if (conversationId != null && !conversationId.isEmpty()) {
                requestBody.put("conversation_id", conversationId);
            }

            String jsonBody = objectMapper.writeValueAsString(requestBody);
            log.debug("Sending request to Dify: {}", jsonBody);

            Request request = new Request.Builder()
                    .url(aiProperties.getDify().getBaseUrl() + "/v1/chat-messages")
                    .addHeader("Authorization", "Bearer " + aiProperties.getDify().getApiKey())
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                    .build();

            try (Response response = okHttpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                    log.error("Dify API error: {} - {}", response.code(), errorBody);
                    return DifyResponse.error("API调用失败: " + response.code());
                }

                String responseBody = response.body().string();
                log.debug("Dify response: {}", responseBody);

                JsonNode jsonNode = objectMapper.readTree(responseBody);
                return DifyResponse.builder()
                        .answer(jsonNode.path("answer").asText())
                        .conversationId(jsonNode.path("conversation_id").asText())
                        .messageId(jsonNode.path("message_id").asText())
                        .success(true)
                        .build();
            }
        } catch (Exception e) {
            log.error("Error calling Dify API", e);
            return DifyResponse.error("调用失败: " + e.getMessage());
        }
    }

    public Flux<String> sendMessageStream(String query, String userId, String conversationId, Map<String, Object> inputs) {
        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();

        CompletableFuture.runAsync(() -> {
            try {
                Map<String, Object> requestBody = new HashMap<>();
                requestBody.put("inputs", inputs != null ? inputs : new HashMap<>());
                requestBody.put("query", query);
                requestBody.put("response_mode", "streaming");
                requestBody.put("user", userId != null ? userId : "default-user");
                if (conversationId != null && !conversationId.isEmpty()) {
                    requestBody.put("conversation_id", conversationId);
                }

                String jsonBody = objectMapper.writeValueAsString(requestBody);

                Request request = new Request.Builder()
                        .url(aiProperties.getDify().getBaseUrl() + "/v1/chat-messages")
                        .addHeader("Authorization", "Bearer " + aiProperties.getDify().getApiKey())
                        .addHeader("Content-Type", "application/json")
                        .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                        .build();

                try (Response response = okHttpClient.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        sink.tryEmitError(new RuntimeException("API调用失败: " + response.code()));
                        return;
                    }

                    ResponseBody body = response.body();
                    if (body != null) {
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(body.byteStream()))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                if (line.startsWith("data: ")) {
                                    String data = line.substring(6);
                                    if (!data.equals("[DONE]")) {
                                        JsonNode node = objectMapper.readTree(data);
                                        String answer = node.path("answer").asText("");
                                        if (!answer.isEmpty()) {
                                            sink.tryEmitNext(answer);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    sink.tryEmitComplete();
                }
            } catch (Exception e) {
                log.error("Error in streaming", e);
                sink.tryEmitError(e);
            }
        });

        return sink.asFlux();
    }

    public ConversationHistory getConversationHistory(String conversationId, String userId) throws IOException {
        HttpUrl url = HttpUrl.parse(aiProperties.getDify().getBaseUrl() + "/v1/messages")
                .newBuilder()
                .addQueryParameter("conversation_id", conversationId)
                .addQueryParameter("user", userId)
                .addQueryParameter("limit", "20")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + aiProperties.getDify().getApiKey())
                .get()
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("获取历史失败: " + response.code());
            }

            String responseBody = response.body().string();
            return objectMapper.readValue(responseBody, ConversationHistory.class);
        }
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class DifyResponse {
        private String answer;
        private String conversationId;
        private String messageId;
        private boolean success;
        private String errorMessage;

        public static DifyResponse error(String message) {
            return DifyResponse.builder()
                    .success(false)
                    .errorMessage(message)
                    .build();
        }
    }

    @lombok.Data
    public static class ConversationHistory {
        private java.util.List<Message> data;
        private int limit;
        private boolean hasMore;

        @lombok.Data
        public static class Message {
            private String id;
            private String query;
            private String answer;
            private long createdAt;
        }
    }
}
