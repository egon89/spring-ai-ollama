package com.egon.spring_ai_ollama.repositories;

import com.egon.spring_ai_ollama.entities.ProcessedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProcessedFileRepository
    extends JpaRepository<ProcessedFile, UUID> {

  Optional<ProcessedFile> findByChecksum(String checksum);
}
