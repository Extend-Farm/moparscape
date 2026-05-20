package io.github.ffakira.rsps.client.desktop.core;

import io.github.ffakira.rsps.client.core.ClientCore;
import io.github.ffakira.rsps.client.core.GameplayClientSession;
import io.github.ffakira.rsps.cache.RawModelRepository;
import io.github.ffakira.rsps.client.desktop.character.CharacterModelAssembler;
import io.github.ffakira.rsps.client.desktop.gameplay.GameplayFrameAssetLoader;
import io.github.ffakira.rsps.client.desktop.gameplay.GameplayFrameAssets;
import io.github.ffakira.rsps.client.desktop.itemicon.ItemIconRenderer;
import io.github.ffakira.rsps.client.desktop.login.TitleScreenAssetLoader;
import io.github.ffakira.rsps.client.desktop.login.TitleScreenAssets;
import io.github.ffakira.rsps.client.desktop.world.CacheBackedWorldSceneLoader;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneTextureAssets;
import io.github.ffakira.rsps.content.ContentBootstrapService;
import io.github.ffakira.rsps.content.IdentityKitDefinitionCatalog;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import io.github.ffakira.rsps.persistence.AccountRepository;
import io.github.ffakira.rsps.persistence.CharacterRepository;
import io.github.ffakira.rsps.persistence.sql.PostgresAccountRepository;
import io.github.ffakira.rsps.persistence.sql.PostgresCharacterRepository;
import io.github.ffakira.rsps.protocol.ServerMessage;
import io.github.ffakira.rsps.persistence.sql.SqlPersistenceEnvironment;
import io.github.ffakira.rsps.server.runtime.CharacterFileRepository;
import io.github.ffakira.rsps.server.runtime.InProcessServerRuntime;
import io.github.ffakira.rsps.server.runtime.PlayerSessionActor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;

final class NativeClientBootstrap {

  private NativeClientBootstrap() {
  }

  static NativeClientAssets loadAssets(Path workingDirectory) {
    return loadAssets(workingDirectory, loadTitleScreenAssets(workingDirectory), null);
  }

  static NativeClientAssets loadAssets(
      Path workingDirectory,
      TitleScreenAssets titleScreenAssets,
      BiConsumer<Integer, String> progressListener
  ) {
    BiConsumer<Integer, String> progress = progressListener == null ? (percent, status) -> {
    } : progressListener;

    progress.accept(15, "Unpacking game frame");
    GameplayFrameAssets gameplayFrameAssets = loadGameplayFrameAssets(workingDirectory);
    progress.accept(30, "Loading scene textures");
    SceneTextureAssets sceneTextureAssets = loadSceneTextureAssets(workingDirectory);
    progress.accept(45, "Loading item definitions");
    ItemDefinitionCatalog itemDefinitionCatalog = loadItemDefinitionCatalog(workingDirectory);
    progress.accept(60, "Preparing item icons");
    ItemIconRenderer itemIconRenderer =
        createItemIconRenderer(workingDirectory, itemDefinitionCatalog, sceneTextureAssets);
    progress.accept(75, "Preparing character models");
    CharacterModelAssembler characterModelAssembler =
        createCharacterModelAssembler(workingDirectory, itemDefinitionCatalog);
    progress.accept(90, "Loading world scene cache");
    CacheBackedWorldSceneLoader worldSceneLoader = createWorldSceneLoader(workingDirectory);
    return new NativeClientAssets(
        titleScreenAssets,
        gameplayFrameAssets,
        sceneTextureAssets,
        itemDefinitionCatalog,
        itemIconRenderer,
        characterModelAssembler,
        worldSceneLoader
    );
  }

  static NativeClientRuntimeContext openRuntimeContext(Path workingDirectory, String clientDescriptor) {
    RepositoryPair repositories = createRepositories(workingDirectory);
    InProcessProtocolBridge protocolBridge = new InProcessProtocolBridge();
    ConcurrentLinkedQueue<ServerMessage> inboundMessages = new ConcurrentLinkedQueue<>();
    protocolBridge.bindInbound(inboundMessages::add);

    InProcessServerRuntime runtime =
        new InProcessServerRuntime(repositories.accountRepository(), repositories.characterRepository());
    GameplayClientSession gameplayClientSession = null;
    try {
      PlayerSessionActor playerSessionActor = runtime.openSession(protocolBridge);
      protocolBridge.bindOutbound(playerSessionActor::accept);

      gameplayClientSession = new GameplayClientSession(new ClientCore(), protocolBridge, clientDescriptor);
      gameplayClientSession.bootstrap();
      gameplayClientSession.connect();
      return new NativeClientRuntimeContext(runtime, gameplayClientSession, inboundMessages);
    } catch (RuntimeException | Error exception) {
      if (gameplayClientSession != null) {
        gameplayClientSession.close();
      }
      runtime.close();
      throw exception;
    }
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
