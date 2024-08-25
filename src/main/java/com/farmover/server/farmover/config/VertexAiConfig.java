package com.farmover.server.farmover.config;

import org.springframework.ai.vertexai.palm2.VertexAiPaLm2ChatModel;
import org.springframework.ai.vertexai.palm2.api.VertexAiPaLm2Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VertexAiConfig {

    @Value("${spring.ai.vertex.ai.api-key}")
    private String apiKey;
    
    @Bean
    public VertexAiPaLm2Api vertexAiPaLm2Api() {
        // Initialize and return the VertexAiPaLm2Api bean
        return new VertexAiPaLm2Api(apiKey); // Provide necessary configuration if required
    }

    @Bean
    public VertexAiPaLm2ChatModel vertexAiPaLm2ChatModel(VertexAiPaLm2Api vertexAiPaLm2Api) {
        // Initialize and return the VertexAiPaLm2ChatModel bean with the
        // VertexAiPaLm2Api
        return new VertexAiPaLm2ChatModel(vertexAiPaLm2Api);
    }
}
