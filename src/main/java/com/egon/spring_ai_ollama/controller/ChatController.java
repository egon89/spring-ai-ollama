package com.egon.spring_ai_ollama.controller;

import com.egon.spring_ai_ollama.dtos.CountryInfo;
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

  public ChatController(ChatService chatService) {
    this.chatService = chatService;
  }

  @GetMapping("/generate")
  public Map<String,String> generate(@RequestParam(value = "message", defaultValue = "Give me a short poem") String message) {
    return this.chatService.generate(message);
  }


  @GetMapping(
      value = "/generate-stream",
      produces = MediaType.TEXT_EVENT_STREAM_VALUE
  )
  public Flux<String> generateStream(@RequestParam(value = "message", defaultValue = "Give me a medium length poem") String message) {
    return this.chatService.streamGenerate(message);
  }

  @GetMapping("/country/{name}")
  public ResponseEntity<CountryInfo> streamCountry(@PathVariable String name) {
    return ResponseEntity.ok(this.chatService.getCountryInformation(name));
  }
}
