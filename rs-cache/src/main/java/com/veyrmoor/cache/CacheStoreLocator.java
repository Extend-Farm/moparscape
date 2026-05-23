package com.veyrmoor.cache;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CacheStoreLocator {

  private static final String SETTINGS_FILE = "settings.gradle.kts";
  private static final Path REFERENCE_CACHE_PATH =
      Path.of("moparscape-reference", "server", "moparscape", "cache", ".file_store_32");
  private static final Path LEGACY_CACHE_PATH =
      Path.of("server", "moparscape", "cache", ".file_store_32");
  private static final List<Path> CACHE_STORE_CANDIDATES =
      List.of(REFERENCE_CACHE_PATH, LEGACY_CACHE_PATH);
  private static final String DATA_FILE = "main_file_cache.dat";

  public Path locateRepositoryRoot(Path startDirectory) {
    Path current = startDirectory.toAbsolutePath().normalize();
    while (current != null) {
      if (Files.exists(current.resolve(SETTINGS_FILE))) {
        return current;
      }
      current = current.getParent();
    }
    throw new IllegalStateException("Could not locate repository root from " + startDirectory);
  }

  public CacheStoreLayout locateFromWorkingDirectory(Path workingDirectory) {
    return locateFromRepositoryRoot(locateRepositoryRoot(workingDirectory));
  }

  public CacheStoreLayout locateFromRepositoryRoot(Path repositoryRoot) {
    Path cacheRoot = locateExistingDirectory(repositoryRoot, CACHE_STORE_CANDIDATES, "cache store");
    Path dataFile = cacheRoot.resolve(DATA_FILE);
    List<Path> indexFiles = new ArrayList<>();
    for (int index = 0; index <= 4; index++) {
      indexFiles.add(cacheRoot.resolve("main_file_cache.idx" + index));
    }

    CacheStoreLayout layout = new CacheStoreLayout(cacheRoot, dataFile, indexFiles);
    validate(layout);
    return layout;
  }

  private Path locateExistingDirectory(
      Path repositoryRoot,
      List<Path> candidates,
      String description
  ) {
    for (Path candidate : candidates) {
      Path resolved = repositoryRoot.resolve(candidate);
      if (Files.isDirectory(resolved)) {
        return resolved;
      }
    }
    String checkedLocations = candidates.stream()
        .map(repositoryRoot::resolve)
        .map(Path::toString)
        .collect(Collectors.joining(", "));
    throw new IllegalStateException("Missing " + description + ". Checked: " + checkedLocations);
  }

  private void validate(CacheStoreLayout layout) {
    if (!Files.isDirectory(layout.rootDirectory())) {
      throw new IllegalStateException("Missing cache store directory: " + layout.rootDirectory());
    }
    if (!Files.isRegularFile(layout.dataFile())) {
      throw new IllegalStateException("Missing cache data file: " + layout.dataFile());
    }
    for (Path indexFile : layout.indexFiles()) {
      if (!Files.isRegularFile(indexFile)) {
        throw new IllegalStateException("Missing cache index file: " + indexFile);
      }
    }
  }
}
