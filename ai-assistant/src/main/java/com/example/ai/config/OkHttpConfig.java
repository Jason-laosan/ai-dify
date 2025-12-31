package com.example.ai.config;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class OkHttpConfig {

    private final AiProperties aiProperties;

    @Bean
    public OkHttpClient okHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        return new OkHttpClient.Builder()
                .connectTimeout(aiProperties.getDify().getTimeout().getConnect(), TimeUnit.SECONDS)
                .readTimeout(aiProperties.getDify().getTimeout().getRead(), TimeUnit.SECONDS)
                .writeTimeout(aiProperties.getDify().getTimeout().getWrite(), TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();
    }
}
