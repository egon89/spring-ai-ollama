package com.egon.spring_ai_ollama.configs;

import com.egon.spring_ai_ollama.tools.NowTool;
import com.egon.spring_ai_ollama.tools.StockTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ChatConfig {

  @Bean
  @Primary
  public ChatClient chatClientWithVectorStore(OllamaChatModel chatModel, VectorStore vectorStore) {
    return ChatClient.builder(chatModel)
        .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
        .defaultToolCallbacks(ChatConfig::defaultTools)
        .build();
  }

  /**
   * This bean is useful for testing the chat client without the vector store.
   * It can be injected using @Qualifier("chatClientWithoutVectorStore") in the constructor of the desired class.
   */
  @Bean("chatClientWithoutVectorStore")
  public ChatClient chatClientWithoutVectorStore(OllamaChatModel chatModel) {
    return ChatClient.builder(chatModel)
        .defaultToolCallbacks(ChatConfig::defaultTools)
        .build();
  }

  private static ToolCallback[] defaultTools() {
    return ToolCallbacks.from(new NowTool(), new StockTool());
  }
}
