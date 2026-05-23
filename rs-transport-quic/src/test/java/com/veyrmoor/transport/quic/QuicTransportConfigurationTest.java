package com.veyrmoor.transport.quic;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class QuicTransportConfigurationTest {

  @Test
  void defaultsUseLocalDesktopValues() {
    QuicTransportConfiguration configuration = QuicTransportConfiguration.defaults(Path.of("."));

    assertThat(configuration.host()).isEqualTo("localhost");
    assertThat(configuration.bindHost()).isEqualTo("127.0.0.1");
    assertThat(configuration.port()).isEqualTo(43_594);
    assertThat(configuration.applicationProtocol()).isEqualTo("rsps-modern-v1");
    assertThat(configuration.certificateDirectory().toString()).contains("artifacts/quic");
  }
}
