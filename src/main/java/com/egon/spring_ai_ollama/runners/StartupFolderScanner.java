package com.egon.spring_ai_ollama.runners;

import com.egon.spring_ai_ollama.services.DocumentIngestionService;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
public class StartupFolderScanner implements ApplicationRunner {

  @Value("${rag.documents-path}")
  private String folder;

  private final DocumentIngestionService ingestionService;

  public StartupFolderScanner(DocumentIngestionService ingestionService) {
    this.ingestionService = ingestionService;
  }

  @Override
  public void run(@NonNull ApplicationArguments args) throws Exception {

    Path path = Paths.get(folder);

    try (Stream<Path> files = Files.list(path)) {

      files.filter(Files::isRegularFile)
          .forEach(ingestionService::process);
    }
  }
}
