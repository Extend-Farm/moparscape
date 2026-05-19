package io.github.ffakira.rsps.client.desktop.core;

import io.github.ffakira.rsps.client.core.ClientDisconnectedEvent;
import io.github.ffakira.rsps.client.core.ClientEvent;
import io.github.ffakira.rsps.client.core.ClientViewModel;
import io.github.ffakira.rsps.client.core.GameplayClientSession;
import io.github.ffakira.rsps.client.desktop.login.LoginScreenController;
import io.github.ffakira.rsps.client.desktop.world.CacheBackedWorldSceneLoader;
import io.github.ffakira.rsps.protocol.ServerMessage;
import java.util.concurrent.ConcurrentLinkedQueue;

final class NativeClientRuntimeCoordinator {

  private NativeClientRuntimeCoordinator() {
  }

  static void drainServerMessages(
      ConcurrentLinkedQueue<ServerMessage> inboundMessages,
      GameplayClientSession gameplayClientSession,
      LoginScreenController loginScreenController,
      OpenGlTileRenderSystem renderSystem,
      String[] titleScreenStatus,
      String[] activeSceneKey,
      String worldBootstrapStatus
  ) {
    // This coordinator is the narrow bridge between protocol progress and renderer mode:
    // disconnects push the client back to credentials, while any successful post-login event keeps
    // the title shell in "loading world" mode until a cache scene can be built.
    ServerMessage message;
    while ((message = inboundMessages.poll()) != null) {
      for (ClientEvent clientEvent : gameplayClientSession.accept(message)) {
        if (clientEvent instanceof ClientDisconnectedEvent clientDisconnectedEvent) {
          loginScreenController.showCredentials();
          renderSystem.setLoginScreenState(loginScreenController.state());
          renderSystem.clearWorldScene();
          activeSceneKey[0] = null;
          titleScreenStatus[0] = clientDisconnectedEvent.reason();
          continue;
        }
        titleScreenStatus[0] = worldBootstrapStatus;
      }
    }
  }

  static void refreshWorldScene(
      ClientViewModel viewModel,
      CacheBackedWorldSceneLoader worldSceneLoader,
      OpenGlTileRenderSystem renderSystem,
      String[] activeSceneKey
  ) {
    // Cache scenes are keyed by a 2x2 region window. The coordinator only rebuilds when the
    // player crosses into a different window, which keeps the temporary native world path cheap
    // without hiding that scene assembly is still authoritative data flowing from the runtime.
    if (worldSceneLoader == null || !viewModel.loggedIn() || viewModel.localPlayerPosition() == null) {
      return;
    }
    String targetSceneKey = CacheBackedWorldSceneLoader.sceneKeyFor(viewModel.localPlayerPosition());
    if (targetSceneKey.equals(activeSceneKey[0])) {
      return;
    }
    renderSystem.setWorldScene(worldSceneLoader.load(viewModel.localPlayerPosition()));
    activeSceneKey[0] = targetSceneKey;
  }

  static ClientViewModel renderViewModel(String titleScreenStatus, ClientViewModel gameplayViewModel) {
    if (gameplayViewModel.loggedIn()) {
      return gameplayViewModel;
    }
    return new ClientViewModel(titleScreenStatus, false, null);
  }

  static boolean isGameplayActive(ClientViewModel viewModel) {
    return viewModel.loggedIn() && viewModel.localPlayerPosition() != null;
  }
}
