package com.veyrmoor.persistence.sql;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

final class EnvironmentValueLoader {

  private EnvironmentValueLoader() {
  }

  static String optionalOrDefault(String environmentKey, String fallbackValue) {
    return optionalOrDefault(System::getenv, environmentKey, fallbackValue);
  }

  static String optionalOrDefault(
      EnvironmentLookup environmentLookup,
      String environmentKey,
      String fallbackValue
  ) {
    String value = nonBlank(environmentLookup.get(environmentKey));
    return value == null ? fallbackValue : value;
  }

  static String optionalOrDefault(String environmentKey, String fileEnvironmentKey, String fallbackValue) {
    return optionalOrDefault(System::getenv, environmentKey, fileEnvironmentKey, fallbackValue);
  }

  static String optionalOrDefault(
      EnvironmentLookup environmentLookup,
      String environmentKey,
      String fileEnvironmentKey,
      String fallbackValue
  ) {
    String value = optional(environmentLookup, environmentKey, fileEnvironmentKey);
    return value == null ? fallbackValue : value;
  }

  static String require(String environmentKey) {
    return require(System::getenv, environmentKey);
  }

  static String require(EnvironmentLookup environmentLookup, String environmentKey) {
    String value = nonBlank(environmentLookup.get(environmentKey));
    if (value == null) {
      throw new IllegalArgumentException("Missing required environment variable: " + environmentKey);
    }
    return value;
  }

  static String require(String environmentKey, String fileEnvironmentKey) {
    return require(System::getenv, environmentKey, fileEnvironmentKey);
  }

  static String require(
      EnvironmentLookup environmentLookup,
      String environmentKey,
      String fileEnvironmentKey
  ) {
    String value = optional(environmentLookup, environmentKey, fileEnvironmentKey);
    if (value == null) {
      throw new IllegalArgumentException(
          "Missing required environment variable: " + environmentKey + " or " + fileEnvironmentKey
      );
    }
    return value;
  }

  private static String optional(
      EnvironmentLookup environmentLookup,
      String environmentKey,
      String fileEnvironmentKey
  ) {
    String directValue = nonBlank(environmentLookup.get(environmentKey));
    String filePath = nonBlank(environmentLookup.get(fileEnvironmentKey));
    if (directValue != null && filePath != null) {
      throw new IllegalArgumentException(
          "Set either " + environmentKey + " or " + fileEnvironmentKey + ", not both"
      );
    }
    if (filePath != null) {
      return readFileValue(fileEnvironmentKey, filePath);
    }
    return directValue;
  }

  private static String readFileValue(String fileEnvironmentKey, String filePath) {
    try {
      String rawValue = Files.readString(Path.of(filePath), StandardCharsets.UTF_8);
      String value = stripTrailingLineEndings(rawValue);
      if (value.isBlank()) {
        throw new IllegalArgumentException(
            "Secret file from " + fileEnvironmentKey + " is empty: " + filePath
        );
      }
      return value;
    } catch (IOException ioException) {
      throw new IllegalStateException(
          "Failed to read secret file from " + fileEnvironmentKey + ": " + filePath,
          ioException
      );
    }
  }

  private static String stripTrailingLineEndings(String value) {
    int end = value.length();
    while (end > 0) {
      char character = value.charAt(end - 1);
      if (character != '\n' && character != '\r') {
        break;
      }
      end--;
    }
    return value.substring(0, end);
  }

  private static String nonBlank(String value) {
    return value == null || value.isBlank() ? null : value;
  }

  @FunctionalInterface
  interface EnvironmentLookup {
    String get(String key);
  }
}
