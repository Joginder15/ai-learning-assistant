package com.jk.ai_learning_assistant;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    // One client for the whole app: system prompt + logging on every call.
    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("""
                        You are a patient learning assistant.
                        Explain in simple words and give one small example.
                        If you are not sure, say so.
                        """)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }
}
