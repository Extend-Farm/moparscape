package io.github.ffakira.rsps.test.fixtures;

import io.github.ffakira.rsps.cache.CacheStoreLayout;
import io.github.ffakira.rsps.cache.CacheStoreLocator;
import java.nio.file.Path;

public final class LegacyRepositoryLayout {

  private LegacyRepositoryLayout() {
  }

  public static Path repositoryRoot(Path workingDirectory) {
    return new CacheStoreLocator().locateRepositoryRoot(workingDirectory);
  }

  public static CacheStoreLayout cacheStore(Path workingDirectory) {
    return new CacheStoreLocator().locateFromWorkingDirectory(workingDirectory);
  }

  public static Path clientDataDirectory(Path workingDirectory) {
    return repositoryRoot(workingDirectory).resolve("client").resolve("data");
  }
}
