package com.egon.spring_ai_ollama.configs;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

  @Bean
  public ChatClient ollamaChatClient(OllamaChatModel chatModel) {
    return ChatClient.create(chatModel);
  }
}
