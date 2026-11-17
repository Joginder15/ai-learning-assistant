package com.jk.ai_learning_assistant;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LearningController {

    record Quiz(String question, List<String> options, int correctIndex, String explanation) {}

    private final ChatClient chatClient;
    private final MessageChatMemoryAdvisor memoryAdvisor;

    public LearningController(ChatClient chatClient, MessageChatMemoryAdvisor memoryAdvisor) {
        this.chatClient = chatClient;
        this.memoryAdvisor = memoryAdvisor;
    }

    // 1. Chat with memory
    @GetMapping("/chat")
    String chat(@RequestParam String message, @RequestParam String conversationId){
        return chatClient.prompt()
                .user(message)
                .advisors(a -> a
                        .advisors(memoryAdvisor)
                        .param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

    // 2. The same chat, streamed
    @GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> chatStream(@RequestParam String message, @RequestParam String conversationId){
        return chatClient.prompt()
                .user(message)
                .advisors(a ->  a
                        .advisors(memoryAdvisor)
                        .param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content();
    }

    // 3. A quiz question as a Java object (no memory needed here)
    @GetMapping("/quiz")
    Quiz quiz(@RequestParam String topic){
        return chatClient.prompt()
                .user(u -> u
                        .text("Write one multiple-choice question about {topic} with 4 options.")
                        .param("topic", topic))
                .call()
                .entity(Quiz.class);
    }
}
