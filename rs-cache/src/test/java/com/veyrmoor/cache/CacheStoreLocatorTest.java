package com.veyrmoor.cache;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class CacheStoreLocatorTest {

  @Test
  void findsLegacyCacheStoreFromModuleWorkingDirectory() {
    CacheStoreLocator locator = new CacheStoreLocator();

    CacheStoreLayout layout = locator.locateFromWorkingDirectory(Path.of("."));

    assertThat(layout.rootDirectory()).exists();
    assertThat(layout.dataFile()).exists();
    assertThat(layout.indexFiles()).hasSize(5);
  }
}
