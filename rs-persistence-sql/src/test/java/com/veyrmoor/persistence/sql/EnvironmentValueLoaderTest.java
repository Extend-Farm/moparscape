package com.veyrmoor.persistence.sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class EnvironmentValueLoaderTest {

  @TempDir
  Path tempDir;

  @Test
  void optionalOrDefaultReadsSecretFileAndTrimsTrailingLineEndings() throws Exception {
    Path passwordFile = tempDir.resolve("password.txt");
    Files.writeString(passwordFile, "secret-value\n");

    Map<String, String> environment = Map.of("RSPS_TEST_PASSWORD_FILE", passwordFile.toString());

    assertThat(
        EnvironmentValueLoader.optionalOrDefault(
            environment::get,
            "RSPS_TEST_PASSWORD",
            "RSPS_TEST_PASSWORD_FILE",
            "fallback"
        )
    ).isEqualTo("secret-value");
  }

  @Test
  void requireRejectsConflictingDirectAndFileInputs() throws Exception {
    Path passwordFile = tempDir.resolve("password.txt");
    Files.writeString(passwordFile, "secret-value");

    Map<String, String> environment = new HashMap<>();
    environment.put("RSPS_TEST_PASSWORD", "direct-value");
    environment.put("RSPS_TEST_PASSWORD_FILE", passwordFile.toString());

    assertThatThrownBy(
        () -> EnvironmentValueLoader.require(
            environment::get,
            "RSPS_TEST_PASSWORD",
            "RSPS_TEST_PASSWORD_FILE"
        )
    )
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Set either RSPS_TEST_PASSWORD or RSPS_TEST_PASSWORD_FILE");
  }

  @Test
  void requireRejectsEmptySecretFiles() throws Exception {
    Path passwordFile = tempDir.resolve("empty.txt");
    Files.writeString(passwordFile, "\n");

    Map<String, String> environment = Map.of("RSPS_TEST_PASSWORD_FILE", passwordFile.toString());

    assertThatThrownBy(
        () -> EnvironmentValueLoader.require(
            environment::get,
            "RSPS_TEST_PASSWORD",
            "RSPS_TEST_PASSWORD_FILE"
        )
    )
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Secret file from RSPS_TEST_PASSWORD_FILE is empty");
  }
}
