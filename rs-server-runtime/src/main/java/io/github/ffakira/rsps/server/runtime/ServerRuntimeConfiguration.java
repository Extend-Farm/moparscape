package io.github.ffakira.rsps.server.runtime;

import java.nio.file.Path;
import java.time.Duration;

public record ServerRuntimeConfiguration(Path workingDirectory, Duration tickInterval) {

  public static ServerRuntimeConfiguration defaults(Path workingDirectory) {
    return new ServerRuntimeConfiguration(workingDirectory, Duration.ofMillis(600));
  }
}
