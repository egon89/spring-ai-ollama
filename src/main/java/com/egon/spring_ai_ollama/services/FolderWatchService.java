package com.egon.spring_ai_ollama.services;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.*;
import java.util.concurrent.Executors;

@Component
public class FolderWatchService {

  private static final Logger log = LoggerFactory.getLogger(FolderWatchService.class);

  @Value("${rag.documents-path}")
  private String folder;

  private final DocumentIngestionService ingestionService;

  public FolderWatchService(DocumentIngestionService ingestionService) {
    this.ingestionService = ingestionService;
  }

  @PostConstruct
  public void startWatcher() {
    Executors.newVirtualThreadPerTaskExecutor().submit(this::watchFolder);
  }

  private void watchFolder() {
    try {
      log.info("Starting folder watch service for folder: {}. Thread: {}", folder, Thread.currentThread());

      var watchService = FileSystems.getDefault().newWatchService();

      var path = Paths.get(folder);

      path.register(
          watchService,
          StandardWatchEventKinds.ENTRY_CREATE
      );

      while (true) {
        var key = watchService.take(); // blocking but cheap

        for (WatchEvent<?> event : key.pollEvents()) {
          var createdFile =
              path.resolve((Path) event.context());

          log.info("New file detected: {}. Thread: {}", createdFile.getFileName(), Thread.currentThread());

          ingestionService.process(createdFile);
        }

        key.reset();
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
