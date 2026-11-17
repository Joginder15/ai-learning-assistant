package com.jk.ai_learning_assistant;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
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

    // Remembers past messages per conversationId (ChatMemory is auto-configured, in-memory).
    @Bean
    MessageChatMemoryAdvisor memoryAdvisor(ChatMemory chatMemory) {
        return MessageChatMemoryAdvisor.builder(chatMemory).build();
    }
}
