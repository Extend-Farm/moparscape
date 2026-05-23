package com.veyrmoor.client.desktop.app;

import com.veyrmoor.client.core.ClientCore;
import com.veyrmoor.cache.AnimationFrameCatalog;
import com.veyrmoor.client.core.GameplayClientSession;
import com.veyrmoor.cache.RawModelRepository;
import com.veyrmoor.client.desktop.assets.NativeClientAssets;
import com.veyrmoor.client.desktop.assets.texture.TextureArchiveAssetLoader;
import com.veyrmoor.client.desktop.character.CharacterModelAssembler;
import com.veyrmoor.client.desktop.character.NpcModelAssembler;
import com.veyrmoor.client.desktop.gameplay.GameplayFrameAssetLoader;
import com.veyrmoor.client.desktop.gameplay.GameplayFrameAssets;
import com.veyrmoor.client.desktop.itemicon.ItemIconRenderer;
import com.veyrmoor.client.desktop.login.TitleScreenAssetLoader;
import com.veyrmoor.client.desktop.login.TitleScreenAssets;
import com.veyrmoor.client.desktop.platform.protocol.InProcessProtocolBridge;
import com.veyrmoor.client.desktop.world.CacheBackedWorldSceneLoader;
import com.veyrmoor.client.desktop.world.raster.SceneTextureAssets;
import com.veyrmoor.content.AnimationSequenceCatalog;
import com.veyrmoor.content.ContentBootstrapService;
import com.veyrmoor.content.IdentityKitDefinitionCatalog;
import com.veyrmoor.content.ItemDefinitionCatalog;
import com.veyrmoor.content.NpcDefinitionCatalog;
import com.veyrmoor.persistence.AccountRepository;
import com.veyrmoor.persistence.CharacterRepository;
import com.veyrmoor.persistence.sql.PostgresAccountProvisioner;
import com.veyrmoor.persistence.sql.PostgresAccountRepository;
import com.veyrmoor.persistence.sql.PostgresCharacterRepository;
import com.veyrmoor.persistence.sql.SqlPersistenceEnvironment;
import com.veyrmoor.protocol.ProtocolVersion;
import com.veyrmoor.protocol.ServerMessage;
import com.veyrmoor.server.runtime.CredentialVerifier;
import com.veyrmoor.server.runtime.InProcessServerRuntime;
import com.veyrmoor.server.runtime.LoginAccountProvisioner;
import com.veyrmoor.server.runtime.PlayerSessionActor;
import com.veyrmoor.transport.quic.QuicClientTransport;
import com.veyrmoor.transport.quic.QuicTransportConfiguration;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;

final class NativeClientBootstrap {

  private static final String IN_PROCESS_RUNTIME_MODE = "inprocess";
  private static final String QUIC_RUNTIME_MODE = "quic";

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
    progress.accept(82, "Preparing npc models");
    NpcModelAssembler npcModelAssembler =
        createNpcModelAssembler(workingDirectory);
    progress.accept(90, "Loading world scene cache");
    CacheBackedWorldSceneLoader worldSceneLoader = createWorldSceneLoader(workingDirectory);
    return new NativeClientAssets(
        titleScreenAssets,
        gameplayFrameAssets,
        sceneTextureAssets,
        itemDefinitionCatalog,
        itemIconRenderer,
        characterModelAssembler,
        npcModelAssembler,
        worldSceneLoader
    );
  }

  static NativeClientRuntimeContext openRuntimeContext(Path workingDirectory, String clientDescriptor) {
    String runtimeMode = readRuntimeMode();
    if (QUIC_RUNTIME_MODE.equalsIgnoreCase(runtimeMode)) {
      return openQuicRuntimeContext(workingDirectory, clientDescriptor);
    }
    return openInProcessRuntimeContext(workingDirectory, clientDescriptor);
  }

  private static NativeClientRuntimeContext openQuicRuntimeContext(Path workingDirectory, String clientDescriptor) {
    ConcurrentLinkedQueue<ServerMessage> inboundMessages = new ConcurrentLinkedQueue<>();
    QuicTransportConfiguration transportConfiguration = QuicTransportConfiguration.defaults(workingDirectory);
    QuicClientTransport transport = QuicClientTransport.connect(
        transportConfiguration,
        inboundMessages::add
    );
    GameplayClientSession gameplayClientSession = new GameplayClientSession(new ClientCore(), transport, clientDescriptor);
    try {
      gameplayClientSession.bootstrap();
      gameplayClientSession.connect();
      return new NativeClientRuntimeContext(() -> {
      }, gameplayClientSession, inboundMessages);
    } catch (RuntimeException | Error exception) {
      gameplayClientSession.close();
      throw exception;
    }
  }

  private static NativeClientRuntimeContext openInProcessRuntimeContext(Path workingDirectory, String clientDescriptor) {
    RepositoryPair repositories = createRepositories();
    InProcessProtocolBridge protocolBridge = new InProcessProtocolBridge();
    ConcurrentLinkedQueue<ServerMessage> inboundMessages = new ConcurrentLinkedQueue<>();
    protocolBridge.bindInbound(inboundMessages::add);

    InProcessServerRuntime runtime =
        new InProcessServerRuntime(
            repositories.accountRepository(),
            repositories.characterRepository(),
            CredentialVerifier.compatible(),
            repositories.loginAccountProvisioner(),
            ProtocolVersion.current(),
            "Welcome to the modern RSPS runtime"
        );
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

  private static String readRuntimeMode() {
    String propertyValue = System.getProperty("rsps.runtimeMode");
    if (propertyValue != null && !propertyValue.isBlank()) {
      return propertyValue;
    }
    String environmentValue = System.getenv("RSPS_RUNTIME_MODE");
    if (environmentValue != null && !environmentValue.isBlank()) {
      return environmentValue;
    }
    return IN_PROCESS_RUNTIME_MODE;
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
      AnimationFrameCatalog animationFrames = AnimationFrameCatalog.load(manifest.cacheStore());
      AnimationSequenceCatalog animationSequences = AnimationSequenceCatalog.load(manifest, animationFrames);
      return new CharacterModelAssembler(
          itemDefinitionCatalog,
          identityKitDefinitionCatalog,
          rawModelRepository,
          animationSequences,
          animationFrames
      );
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

  static NpcModelAssembler createNpcModelAssembler(Path workingDirectory) {
    try {
      var manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(workingDirectory);
      return new NpcModelAssembler(
          NpcDefinitionCatalog.load(manifest),
          new RawModelRepository(manifest.cacheStore())
      );
    } catch (RuntimeException runtimeException) {
      System.err.println("Using player-only scene actors: " + runtimeException.getMessage());
      return null;
    }
  }

  static RepositoryPair createRepositories() {
    var configuration = SqlPersistenceEnvironment.load();
    PostgresAccountProvisioner accountProvisioner = new PostgresAccountProvisioner(configuration);
    return new RepositoryPair(
        new PostgresAccountRepository(configuration),
        new PostgresCharacterRepository(configuration),
        accountProvisioner::provisionForLogin
    );
  }

  record RepositoryPair(
      AccountRepository accountRepository,
      CharacterRepository characterRepository,
      LoginAccountProvisioner loginAccountProvisioner
  ) {
  }
}
