package com.egon.spring_ai_ollama.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class IngestionPipelineService {

  public static final String DOCUMENT_UNIQUE_ID = "documentUniqueId";
  private static final Logger log = LoggerFactory.getLogger(IngestionPipelineService.class);

//  private final VectorStore vectorStore;
//
//  public IngestionPipelineService(VectorStore vectorStore) {
//    this.vectorStore = vectorStore;
//  }

  public void ingest(Resource resource, UUID id) {

    DocumentReader reader = new TikaDocumentReader(resource);

    List<Document> documents = reader.read()
        .stream()
        .map(doc -> {
          var newMeta = new HashMap<>(doc.getMetadata());
          newMeta.put(DOCUMENT_UNIQUE_ID, id);

          return new Document(doc.getId(), doc.getText(), newMeta);
        })
        .toList();

    TokenTextSplitter splitter = new TokenTextSplitter();

    List<Document> chunks = splitter.apply(documents);

    log.info("Document ID: {} got split into {} chunks", id, chunks.size());

    for (Document chunk : chunks) {
      log.debug("Chunk ID: {}. Metadata: {}", chunk.getId(), chunk.getMetadata());
      log.debug("Chunk ID: {}. Content: {}", chunk.getId(), chunk.getText());
    }

    // vectorStore.add(chunks);
  }
}
