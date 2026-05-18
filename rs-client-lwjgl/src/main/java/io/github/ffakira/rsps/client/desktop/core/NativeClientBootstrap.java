package io.github.ffakira.rsps.client.desktop.core;

import io.github.ffakira.rsps.cache.RawModelRepository;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneTextureAssets;
import io.github.ffakira.rsps.content.ContentBootstrapService;
import io.github.ffakira.rsps.content.IdentityKitDefinitionCatalog;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import io.github.ffakira.rsps.persistence.AccountRepository;
import io.github.ffakira.rsps.persistence.CharacterRepository;
import io.github.ffakira.rsps.persistence.sql.PostgresAccountRepository;
import io.github.ffakira.rsps.persistence.sql.PostgresCharacterRepository;
import io.github.ffakira.rsps.persistence.sql.SqlPersistenceEnvironment;
import io.github.ffakira.rsps.server.runtime.CharacterFileRepository;
import java.nio.file.Files;
import java.nio.file.Path;

final class NativeClientBootstrap {

  private NativeClientBootstrap() {
  }

  static TitleScreenAssets loadTitleScreenAssets(Path workingDirectory) {
    try {
      return TitleScreenAssetLoader.loadFromWorkingDirectory(workingDirectory);
    } catch (RuntimeException runtimeException) {
      System.err.println("Using native fallback login UI: " + runtimeException.getMessage());
      return null;
    }
  }

  static GameplayFrameAssets loadGameplayFrameAssets(Path workingDirectory) {
    try {
      return GameplayFrameAssetLoader.loadFromWorkingDirectory(workingDirectory);
    } catch (RuntimeException runtimeException) {
      System.err.println("Using synthetic gameplay frame: " + runtimeException.getMessage());
      return null;
    }
  }

  static SceneTextureAssets loadSceneTextureAssets(Path workingDirectory) {
    try {
      return TextureArchiveAssetLoader.loadFromWorkingDirectory(workingDirectory);
    } catch (RuntimeException runtimeException) {
      System.err.println("Using flat-color textured-face fallback: " + runtimeException.getMessage());
      return null;
    }
  }

  static ItemDefinitionCatalog loadItemDefinitionCatalog(Path workingDirectory) {
    try {
      return ItemDefinitionCatalog.load(new ContentBootstrapService().bootstrapFromWorkingDirectory(workingDirectory));
    } catch (RuntimeException runtimeException) {
      System.err.println("Using unresolved item names in gameplay UI: " + runtimeException.getMessage());
      return null;
    }
  }

  static CharacterModelAssembler createCharacterModelAssembler(Path workingDirectory, ItemDefinitionCatalog itemDefinitionCatalog) {
    if (itemDefinitionCatalog == null) {
      return null;
    }
    try {
      var manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(workingDirectory);
      IdentityKitDefinitionCatalog identityKitDefinitionCatalog = IdentityKitDefinitionCatalog.load(manifest);
      RawModelRepository rawModelRepository = new RawModelRepository(manifest.cacheStore());
      return new CharacterModelAssembler(itemDefinitionCatalog, identityKitDefinitionCatalog, rawModelRepository);
    } catch (RuntimeException runtimeException) {
      System.err.println("Using proxy character renderer: " + runtimeException.getMessage());
      return null;
    }
  }

  static ItemIconRenderer createItemIconRenderer(
      Path workingDirectory,
      ItemDefinitionCatalog itemDefinitionCatalog,
      SceneTextureAssets sceneTextureAssets
  ) {
    if (itemDefinitionCatalog == null) {
      return null;
    }
    try {
      var manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(workingDirectory);
      return new ItemIconRenderer(itemDefinitionCatalog, new RawModelRepository(manifest.cacheStore()), sceneTextureAssets);
    } catch (RuntimeException runtimeException) {
      System.err.println("Using text-only inventory UI: " + runtimeException.getMessage());
      return null;
    }
  }

  static CacheBackedWorldSceneLoader createWorldSceneLoader(Path workingDirectory) {
    try {
      return new CacheBackedWorldSceneLoader(workingDirectory);
    } catch (RuntimeException runtimeException) {
      System.err.println("Using placeholder world view: " + runtimeException.getMessage());
      return null;
    }
  }

  static RepositoryPair createRepositories(Path workingDirectory) {
    String persistenceMode = System.getenv("RSPS_PERSISTENCE_MODE");
    if ("postgres".equalsIgnoreCase(persistenceMode)) {
      var configuration = SqlPersistenceEnvironment.load();
      return new RepositoryPair(
          new PostgresAccountRepository(configuration),
          new PostgresCharacterRepository(configuration)
      );
    }
    CharacterFileRepository repository = new CharacterFileRepository(resolveCharactersDirectory(workingDirectory));
    return new RepositoryPair(repository, repository);
  }

  private static Path resolveCharactersDirectory(Path workingDirectory) {
    Path[] candidates = {
        workingDirectory.resolve("client/characters"),
        workingDirectory.resolve("../client/characters").normalize()
    };
    for (Path candidate : candidates) {
      if (Files.isDirectory(candidate)) {
        return candidate;
      }
    }
    throw new IllegalStateException("Unable to locate client/characters from " + workingDirectory);
  }

  record RepositoryPair(AccountRepository accountRepository, CharacterRepository characterRepository) {
  }
}
