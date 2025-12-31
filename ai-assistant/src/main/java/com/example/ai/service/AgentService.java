package com.example.ai.service;

import com.example.ai.dto.AgentRequest;
import com.example.ai.dto.AgentResponse;

public interface AgentService {

    AgentResponse executeTask(AgentRequest request);

    interface JavaAssistant {
        String chat(String message);
    }
}
