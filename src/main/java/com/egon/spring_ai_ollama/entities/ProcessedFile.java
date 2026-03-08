package com.egon.spring_ai_ollama.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "processed_files")
public class ProcessedFile {

  public ProcessedFile() {
  }

  public ProcessedFile(UUID id, String fileName, String checksum, Instant processedAt) {
    this.id = id;
    this.fileName = fileName;
    this.checksum = checksum;
    this.processedAt = processedAt;
  }

  @Id
  private UUID id;

  private String fileName;

  private String checksum;

  private Instant processedAt;

  public static ProcessedFile of(UUID id, String fileName, String checksum, Instant processedAt) {
    var currentId = id != null ? id : UUID.randomUUID();

    return new ProcessedFile(currentId, fileName, checksum, processedAt);
  }
}
