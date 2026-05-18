package io.github.ffakira.rsps.content;

import io.github.ffakira.rsps.cache.CacheStoreLayout;
import io.github.ffakira.rsps.cache.CacheStoreLocator;
import java.nio.file.Files;
import java.nio.file.Path;

public class ContentBootstrapService {

  private static final Path LEGACY_DATA_DIRECTORY = Path.of("client", "data");

  public ContentManifest bootstrapFromWorkingDirectory(Path workingDirectory) {
    CacheStoreLocator locator = new CacheStoreLocator();
    Path repositoryRoot = locator.locateRepositoryRoot(workingDirectory);
    CacheStoreLayout cacheStore = locator.locateFromRepositoryRoot(repositoryRoot);
    Path legacyDataDirectory = repositoryRoot.resolve(LEGACY_DATA_DIRECTORY);
    if (!Files.isDirectory(legacyDataDirectory)) {
      throw new IllegalStateException("Missing legacy data directory: " + legacyDataDirectory);
    }
    return new ContentManifest(cacheStore, legacyDataDirectory);
  }
}
