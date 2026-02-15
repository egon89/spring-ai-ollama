package com.egon.spring_ai_ollama.services;

import com.egon.spring_ai_ollama.dtos.CountryInfo;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Optional;

@Service
public class ChatService {

  private final OllamaChatModel chatModel;

  public ChatService(OllamaChatModel chatModel) {
    this.chatModel = chatModel;
  }

  public Map<String, String> generate(String message) {
    return Map.of("answer",
        Optional.ofNullable(this.chatModel.call(message)).orElse("No answer"));
  }

  public Flux<String> streamGenerate(String message) {
    return this.chatModel.stream(message);
  }

  public CountryInfo getCountryInformation(String name) {
    var outputConverter = new BeanOutputConverter<>(CountryInfo.class);

    String format = outputConverter.getFormat();
    String template = """
        Give information about the country {name}. I want to know the following information:
         - the name of the capital city
         - the continent the country is located in
         - the territorial size in kilometers of the country
         - the population of the country (number of inhabitants in integer format)
         - the official language of the country
         - the name of the currency used in the country
         - the summer average temperature of the country
         - the winter average temperature of the country
        
        For number information use only dot as decimal separator. Do not use commas to separate thousands.
        For example, if the population is 1 million, write 1000000, not 1,000,000.
        If the size is 1.5 million square kilometers, write 1500000, not 1.5 million or 1,500,000.
        
        About the response:
        {format}
        """;

    Prompt prompt = PromptTemplate.builder()
        .template(template)
        .variables(Map.of(
            "name", name,
            "format", format
        ))
        .build()
        .create();

    var generation = this.chatModel.call(prompt).getResult();
    var result = Optional.ofNullable(generation).map(Generation::getOutput).orElseThrow();
    var countryInfoStr = Optional.ofNullable(result.getText()).orElseThrow();

    return outputConverter.convert(countryInfoStr);
  }
}
