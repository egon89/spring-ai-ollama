package com.egon.spring_ai_ollama.controller;

import com.egon.spring_ai_ollama.dtos.CountryInfo;
import com.egon.spring_ai_ollama.services.ChatClientService;
import com.egon.spring_ai_ollama.services.ChatService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

  private final ChatService chatService;
  private final ChatClientService chatClientService;

  public ChatController(ChatService chatService, ChatClientService chatClientService) {
    this.chatService = chatService;
    this.chatClientService = chatClientService;
  }

  @GetMapping("/generate")
  public Map<String,String> generate(@RequestParam(value = "message", defaultValue = "Give me a short poem") String message) {
    return this.chatService.generate(message);
  }


  @GetMapping(
      value = "/{chatId}/stream",
      produces = MediaType.TEXT_EVENT_STREAM_VALUE
  )
  public Flux<String> streamChat(
      @PathVariable String chatId,
      @RequestParam(value = "message", defaultValue = "Give me a medium length poem") String message
  ) {
    return this.chatClientService.streamChat(chatId, message);
  }

  @GetMapping("/country/{name}")
  public ResponseEntity<CountryInfo> streamCountry(@PathVariable String name) {
    return ResponseEntity.ok(this.chatService.getCountryInformation(name));
  }
}
