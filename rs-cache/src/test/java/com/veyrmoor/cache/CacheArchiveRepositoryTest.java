package com.veyrmoor.cache;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class CacheArchiveRepositoryTest {

  @Test
  void loadsTopLevelArchiveContainerThroughRepository() {
    CacheStoreLayout layout = new CacheStoreLocator().locateFromWorkingDirectory(Path.of("."));
    CacheArchiveRepository repository = new CacheArchiveRepository(layout);

    CacheArchiveContainer archive = repository.loadArchive(0, 1);

    assertThat(archive.entryReferences()).hasSize(10);
    assertThat(archive.readEntry("title.dat")).isNotEmpty();
  }
}
