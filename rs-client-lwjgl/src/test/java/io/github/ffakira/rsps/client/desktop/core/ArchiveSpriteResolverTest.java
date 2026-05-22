package io.github.ffakira.rsps.client.desktop.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class ArchiveSpriteResolverTest {

  @Test
  void cachesMissingSpritesAfterTheFirstFailedDecode() {
    AtomicInteger decodeCalls = new AtomicInteger();
    ArchiveSpriteResolver resolver = new ArchiveSpriteResolver((entryName, frameIndex) -> {
      decodeCalls.incrementAndGet();
      throw new IllegalStateException("missing sprite");
    });

    assertThat(resolver.resolve("missing", 0)).isNull();
    assertThat(resolver.resolve("missing", 0)).isNull();

    assertThat(decodeCalls).hasValue(1);
  }
}
