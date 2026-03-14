package com.egon.spring_ai_ollama.configs;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

  @Bean
  public ChatClient chatClient(OllamaChatModel chatModel, VectorStore vectorStore) {
    return ChatClient.builder(chatModel)
        .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
        .build();
  }
}
