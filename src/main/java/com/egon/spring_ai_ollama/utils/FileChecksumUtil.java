package com.egon.spring_ai_ollama.utils;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;

public class FileChecksumUtil {

  private FileChecksumUtil() {
    throw new UnsupportedOperationException();
  }

  public static String sha256(Path path) {

    try (InputStream is = Files.newInputStream(path)) {

      MessageDigest digest = MessageDigest.getInstance("SHA-256");

      byte[] buffer = new byte[8192];
      int n;

      while ((n = is.read(buffer)) > 0) {
        digest.update(buffer, 0, n);
      }

      return HexFormat.of().formatHex(digest.digest());

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
