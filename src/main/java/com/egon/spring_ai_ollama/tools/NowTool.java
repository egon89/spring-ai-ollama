package com.egon.spring_ai_ollama.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;

public class NowTool {
  private static final Logger log = LoggerFactory.getLogger(NowTool.class);

  @Tool(description = "Get the current date and time in the user's timezone")
  String getCurrentDateTime() {
    final var now = LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();

    log.info("NowTool invoked: {})", now);

    return now;
  }
}
