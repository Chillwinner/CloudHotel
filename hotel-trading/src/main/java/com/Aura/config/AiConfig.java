package com.Aura.config;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Value("${ai.qwen.api-key}")
    private String apiKey;

    @Value("${ai.qwen.model}")
    private String modelName;

    @Bean("oa")
    public ChatModel chatLanguageModel() {
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .timeout(java.time.Duration.ofSeconds(60))
                .maxRetries(2)
                .build();
    }

    @Bean("cmp")
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.withMaxMessages(20);
    }
}
