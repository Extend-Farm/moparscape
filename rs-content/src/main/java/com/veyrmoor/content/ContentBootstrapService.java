package com.veyrmoor.content;

import com.veyrmoor.cache.CacheStoreLayout;
import com.veyrmoor.cache.CacheStoreLocator;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class ContentBootstrapService {

  private static final Path REFERENCE_DATA_DIRECTORY =
      Path.of("moparscape-reference", "client", "data");
  private static final Path LEGACY_DATA_DIRECTORY = Path.of("client", "data");
  private static final List<Path> CLIENT_DATA_DIRECTORY_CANDIDATES =
      List.of(REFERENCE_DATA_DIRECTORY, LEGACY_DATA_DIRECTORY);

  public ContentManifest bootstrapFromWorkingDirectory(Path workingDirectory) {
    CacheStoreLocator locator = new CacheStoreLocator();
    Path repositoryRoot = locator.locateRepositoryRoot(workingDirectory);
    CacheStoreLayout cacheStore = locator.locateFromRepositoryRoot(repositoryRoot);
    Path legacyDataDirectory = locateClientDataDirectory(repositoryRoot);
    return new ContentManifest(cacheStore, legacyDataDirectory);
  }

  private Path locateClientDataDirectory(Path repositoryRoot) {
    for (Path candidate : CLIENT_DATA_DIRECTORY_CANDIDATES) {
      Path resolved = repositoryRoot.resolve(candidate);
      if (Files.isDirectory(resolved)) {
        return resolved;
      }
    }
    String checkedLocations = CLIENT_DATA_DIRECTORY_CANDIDATES.stream()
        .map(repositoryRoot::resolve)
        .map(Path::toString)
        .collect(Collectors.joining(", "));
    throw new IllegalStateException("Missing client data directory. Checked: " + checkedLocations);
  }
}
