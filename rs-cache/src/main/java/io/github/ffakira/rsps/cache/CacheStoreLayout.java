package io.github.ffakira.rsps.cache;

import java.nio.file.Path;
import java.util.List;

public record CacheStoreLayout(Path rootDirectory, Path dataFile, List<Path> indexFiles) {

  public CacheStoreLayout {
    indexFiles = List.copyOf(indexFiles);
  }
}
