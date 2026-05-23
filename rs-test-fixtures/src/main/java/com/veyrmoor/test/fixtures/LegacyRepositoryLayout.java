package com.veyrmoor.test.fixtures;

import com.veyrmoor.cache.CacheStoreLayout;
import com.veyrmoor.cache.CacheStoreLocator;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class LegacyRepositoryLayout {

  private static final List<Path> CLIENT_DATA_DIRECTORY_CANDIDATES = List.of(
      Path.of("moparscape-reference", "client", "data"),
      Path.of("client", "data")
  );

  private LegacyRepositoryLayout() {
  }

  public static Path repositoryRoot(Path workingDirectory) {
    return new CacheStoreLocator().locateRepositoryRoot(workingDirectory);
  }

  public static CacheStoreLayout cacheStore(Path workingDirectory) {
    return new CacheStoreLocator().locateFromWorkingDirectory(workingDirectory);
  }

  public static Path clientDataDirectory(Path workingDirectory) {
    Path repositoryRoot = repositoryRoot(workingDirectory);
    for (Path candidate : CLIENT_DATA_DIRECTORY_CANDIDATES) {
      Path resolved = repositoryRoot.resolve(candidate);
      if (Files.isDirectory(resolved)) {
        return resolved;
      }
    }
    throw new IllegalStateException("Missing client data directory under " + repositoryRoot);
  }
}
