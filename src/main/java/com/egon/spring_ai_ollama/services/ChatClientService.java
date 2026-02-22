package com.egon.spring_ai_ollama.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class ChatClientService {
  private final ChatClient chatClient;
  private final ChatMemory chatMemory;

  public ChatClientService(ChatClient chatClient, ChatMemory chatMemory) {
    this.chatClient = chatClient;
    this.chatMemory = chatMemory;
  }

  public Flux<String> streamChat(String id, String message) {
    AtomicReference<String> lastChunk = new AtomicReference<>("");

    return chatClient.prompt()
        .user(message)
        .advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
        .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, id))
        .stream()
        .content()
        .map(chunk -> {
          String previous = lastChunk.getAndSet(chunk);

          // If previous is empty, it's the first chunk
          if (previous.isEmpty()) {
            return chunk;
          }

          // If last character of previous is not space and current chunk doesn't start with punctuation
          boolean needsSpace = !previous.endsWith(" ") && !chunk.matches("^[.,!?)]");

          return (needsSpace ? " " : "") + chunk;
        })
        .concatWithValues("<END_OF_RESPONSE>");
  }
}
