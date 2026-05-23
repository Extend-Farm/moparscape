package com.veyrmoor.transport.quic;

import com.veyrmoor.protocol.ProtocolVersion;
import java.nio.file.Path;

public record QuicTransportConfiguration(
    String host,
    String bindHost,
    int port,
    String applicationProtocol,
    Path certificateDirectory
) {

  private static final String DEFAULT_HOST = "localhost";
  private static final String DEFAULT_BIND_HOST = "127.0.0.1";
  private static final int DEFAULT_PORT = 43_594;

  public static QuicTransportConfiguration defaults(Path workingDirectory) {
    return new QuicTransportConfiguration(
        readSetting("rsps.quic.host", "RSPS_QUIC_HOST", DEFAULT_HOST),
        readSetting("rsps.quic.bindHost", "RSPS_QUIC_BIND_HOST", DEFAULT_BIND_HOST),
        readPort(),
        readSetting("rsps.quic.applicationProtocol", "RSPS_QUIC_APPLICATION_PROTOCOL", ProtocolVersion.current().value()),
        Path.of(readSetting(
            "rsps.quic.certificateDirectory",
            "RSPS_QUIC_CERTIFICATE_DIR",
            workingDirectory.resolve("artifacts/quic").toString()
        )).toAbsolutePath().normalize()
    );
  }

  private static int readPort() {
    String configuredValue = readSetting("rsps.quic.port", "RSPS_QUIC_PORT", Integer.toString(DEFAULT_PORT));
    try {
      return Integer.parseInt(configuredValue);
    } catch (NumberFormatException exception) {
      throw new IllegalArgumentException("Invalid QUIC port: " + configuredValue, exception);
    }
  }

  private static String readSetting(String propertyKey, String environmentKey, String fallback) {
    String propertyValue = System.getProperty(propertyKey);
    if (propertyValue != null && !propertyValue.isBlank()) {
      return propertyValue;
    }
    String environmentValue = System.getenv(environmentKey);
    if (environmentValue != null && !environmentValue.isBlank()) {
      return environmentValue;
    }
    return fallback;
  }
}
