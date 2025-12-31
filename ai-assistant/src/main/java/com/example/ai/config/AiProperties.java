package com.example.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ai")
public class AiProperties {

    private DifyProperties dify = new DifyProperties();
    private OpenAiProperties openai = new OpenAiProperties();
    private LangGraphProperties langgraph = new LangGraphProperties();

    @Data
    public static class DifyProperties {
        private boolean enabled = true;
        private String baseUrl = "http://localhost";
        private String apiKey;
        private TimeoutProperties timeout = new TimeoutProperties();
    }

    @Data
    public static class OpenAiProperties {
        private boolean enabled = true;
        private String apiKey;
        private String model = "gpt-4";
        private double temperature = 0.7;
        private int maxTokens = 2000;
        private String baseUrl = "https://api.openai.com/v1";
    }

    @Data
    public static class LangGraphProperties {
        private boolean enabled = false;
        private String baseUrl = "http://localhost:5000";
    }

    @Data
    public static class TimeoutProperties {
        private int connect = 30;
        private int read = 60;
        private int write = 60;
    }
}
