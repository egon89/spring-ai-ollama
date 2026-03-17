package com.egon.spring_ai_ollama.configs;

import com.egon.spring_ai_ollama.tools.NowTool;
import com.egon.spring_ai_ollama.tools.StockTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;

@Configuration
public class ChatConfig {

  private static final Logger log = LoggerFactory.getLogger(ChatConfig.class);

  @Bean
  @Primary
  public ChatClient chatClientWithVectorStore(OllamaChatModel chatModel, VectorStore vectorStore, SyncMcpToolCallbackProvider tools) {
    logSyncMcpProvider(tools);

    return chatClientBuilderWithTools(chatModel, tools)
        .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
        .build();
  }

  /**
   * This bean is useful for testing the chat client without the vector store.
   * It can be injected using @Qualifier("chatClientWithoutVectorStore") in the constructor of the desired class.
   */
  @Bean("chatClientWithoutVectorStore")
  public ChatClient chatClientWithoutVectorStore(
      OllamaChatModel chatModel,
      SyncMcpToolCallbackProvider tools
  ) {
    logSyncMcpProvider(tools);

    return chatClientBuilderWithTools(chatModel, tools).build();
  }

  private static ToolCallback[] defaultTools() {
    return ToolCallbacks.from(new NowTool(), new StockTool());
  }

  private static String defaultSystem() {
    return """
      The filesystem tool must use the '/data' directory for all file operations.
      When asked to read or write a file, you should use the '/data' directory.
    """;
  }

  private ChatClient.Builder chatClientBuilderWithTools(OllamaChatModel chatModel, SyncMcpToolCallbackProvider tools) {
    logSyncMcpProvider(tools);

    return ChatClient.builder(chatModel)
        .defaultSystem(defaultSystem())
        .defaultToolCallbacks(ChatConfig::defaultTools, tools);
  }

  private void logSyncMcpProvider(SyncMcpToolCallbackProvider provider) {
    Arrays.stream(provider.getToolCallbacks()).forEach(tool ->
        log.info("Registering tool: {} ({})", tool.getToolDefinition().name(), tool.getToolDefinition().description()));
  }
}
