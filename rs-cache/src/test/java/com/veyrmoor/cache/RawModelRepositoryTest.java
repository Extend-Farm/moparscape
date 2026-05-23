package com.veyrmoor.cache;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class RawModelRepositoryTest {

  @Test
  void decodesLiveCacheModelArchivesFromModelStore() {
    CacheStoreLayout layout = new CacheStoreLocator().locateFromWorkingDirectory(Path.of("."));
    RawModelRepository repository = new RawModelRepository(layout);

    RawModelData rawModelData = repository.loadModel(0);

    assertThat(rawModelData.vertexCount()).isPositive();
    assertThat(rawModelData.faceCount()).isPositive();
    assertThat(rawModelData.vertexX()).hasSize(rawModelData.vertexCount());
    assertThat(rawModelData.faceVertexA()).hasSize(rawModelData.faceCount());
  }

  @Test
  void decodesMultipleLiveCacheModelsConsistently() {
    CacheStoreLayout layout = new CacheStoreLocator().locateFromWorkingDirectory(Path.of("."));
    RawModelRepository repository = new RawModelRepository(layout);

    RawModelData rawModelData = repository.loadModel(1);

    assertThat(rawModelData.vertexCount()).isGreaterThan(10);
    assertThat(rawModelData.faceCount()).isGreaterThan(10);
  }
}
