package com.egon.spring_ai_ollama.services;

import com.egon.spring_ai_ollama.entities.ProcessedFile;
import com.egon.spring_ai_ollama.repositories.ProcessedFileRepository;
import com.egon.spring_ai_ollama.utils.FileChecksumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

@Service
public class DocumentIngestionService {

  private static final Logger log = LoggerFactory.getLogger(DocumentIngestionService.class);
  private final ProcessedFileRepository repository;
  private final IngestionPipelineService pipeline;

  public DocumentIngestionService(
      ProcessedFileRepository repository,
      IngestionPipelineService pipeline
  ) {
    this.repository = repository;
    this.pipeline = pipeline;
  }

  @Async
  public void process(Path path) {
    var checksum = FileChecksumUtil.sha256(path);

    if (repository.findByChecksum(checksum).isPresent()) {
      log.warn("File already processed, skipping: {}", path.getFileName());
      return;
    }

    var documentId = UUID.randomUUID();

    log.info("Ingesting file: {}. Document ID: {}. Thread: {}", path.getFileName(), documentId, Thread.currentThread());

    var resource = new FileSystemResource(path);

    pipeline.ingest(resource, documentId);

    var file = ProcessedFile.of(documentId, path.getFileName().toString(), checksum, Instant.now());

    // repository.save(file);
  }
}
