package com.egon.spring_ai_ollama.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * This service orchestrates the ingestion pipeline. It reads a document, splits it into chunks, and stores those chunks in the vector store.
 * The pipeline uses the Spring AI ETL Pipeline pattern, which consists of a DocumentReader, a DocumentTransformer, and a DocumentWriter.
 * The pipeline consists of the following:
 * 1. DocumentReader: TikaDocumentReader
 * 2. DocumentTransformer: TokenTextSplitter
 * 3. DocumentWriter: VectorStore
 * <a href="https://docs.spring.io/spring-ai/reference/api/etl-pipeline.html">Spring AI ETL Pipeline</a>
 */
@Service
public class IngestionPipelineService {

  public static final String DOCUMENT_UNIQUE_ID = "documentUniqueId";
  private static final Logger log = LoggerFactory.getLogger(IngestionPipelineService.class);

  private final VectorStore vectorStore;

  public IngestionPipelineService(VectorStore vectorStore) {
    this.vectorStore = vectorStore;
  }

  public void ingest(Resource resource, UUID id) {
    var reader = new TikaDocumentReader(resource);

    var documents = reader.read()
        .stream()
        .peek(doc -> doc.getMetadata().put(DOCUMENT_UNIQUE_ID, id))
        .toList();

    var splitter = new TokenTextSplitter();

    var chunks = splitter.apply(documents);

    log.info("Document ID: {} got split into {} chunks", id, chunks.size());

    for (Document chunk : chunks) {
      log.debug("Chunk ID: {}. Metadata: {}", chunk.getId(), chunk.getMetadata());
      log.debug("Chunk ID: {}. Content: {}", chunk.getId(), chunk.getText());
    }

    vectorStore.add(chunks);
  }
}
