package io.github.ffakira.rsps.client.desktop.core;

import io.github.ffakira.rsps.client.core.GameplayClientSession;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneTextureAssets;
import io.github.ffakira.rsps.model.MovementMode;
import io.github.ffakira.rsps.protocol.ServerMessage;
import io.github.ffakira.rsps.server.runtime.InProcessServerRuntime;
import io.github.ffakira.rsps.server.runtime.PlayerSessionActor;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.GLFW_DEPTH_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public final class DesktopClientMain {

  private static final String WELCOME_STATUS = "Welcome to MoparScape";
  private static final String CREDENTIALS_STATUS = "Enter your username and password.";
  private static final String AUTHENTICATING_STATUS = "Connecting to server...";
  private static final String WORLD_BOOTSTRAP_STATUS = "Loading world...";
  private static final String CLIENT_DESCRIPTOR = "rs-client-lwjgl";
  private static final float CAMERA_YAW_STEP_DEGREES = 8.0f;
  private static final float CAMERA_PITCH_STEP_DEGREES = 1.0f;

  private DesktopClientMain() {
  }

  /**
   * Boots the native LWJGL shell, wires it to the in-process authoritative runtime, and keeps the
   * two client phases explicit: title/login first, then gameplay after server bootstrap.
   */
  public static void main(String[] args) {
    GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
    errorCallback.set();
    if (!glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }

    long window = 0L;
    InProcessServerRuntime runtime = null;
    GameplayClientSession gameplayClientSession = null;
    try {
      DesktopWindowConfig windowConfig = DesktopWindowConfig.defaults();
      glfwDefaultWindowHints();
      glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
      // The gameplay viewport now depends on explicit depth testing and benefits noticeably from
      // multisampling. Make the 3D surface contract explicit at window creation instead of
      // relying on platform defaults.
      glfwWindowHint(GLFW_DEPTH_BITS, 24);
      glfwWindowHint(GLFW_SAMPLES, 4);
      window = glfwCreateWindow(windowConfig.width(), windowConfig.height(), windowConfig.title(), 0L, 0L);
      if (window == 0L) {
        throw new IllegalStateException("Unable to create LWJGL window");
      }

      glfwMakeContextCurrent(window);
      glfwSwapInterval(1);
      GL.createCapabilities();

      Path workingDirectory = Path.of(".").toAbsolutePath().normalize();
      TitleScreenAssets titleScreenAssets = NativeClientBootstrap.loadTitleScreenAssets(workingDirectory);
      GameplayFrameAssets gameplayFrameAssets = NativeClientBootstrap.loadGameplayFrameAssets(workingDirectory);
      SceneTextureAssets sceneTextureAssets = NativeClientBootstrap.loadSceneTextureAssets(workingDirectory);
      var itemDefinitionCatalog = NativeClientBootstrap.loadItemDefinitionCatalog(workingDirectory);
      ItemIconRenderer itemIconRenderer =
          NativeClientBootstrap.createItemIconRenderer(workingDirectory, itemDefinitionCatalog, sceneTextureAssets);
      CharacterModelAssembler characterModelAssembler =
          NativeClientBootstrap.createCharacterModelAssembler(workingDirectory, itemDefinitionCatalog);
      CacheBackedWorldSceneLoader worldSceneLoader = NativeClientBootstrap.createWorldSceneLoader(workingDirectory);
      NativeClientBootstrap.RepositoryPair repositories = NativeClientBootstrap.createRepositories(workingDirectory);

      InProcessProtocolBridge protocolBridge = new InProcessProtocolBridge();
      ConcurrentLinkedQueue<ServerMessage> inboundMessages = new ConcurrentLinkedQueue<>();
      protocolBridge.bindInbound(inboundMessages::add);

      runtime = new InProcessServerRuntime(repositories.accountRepository(), repositories.characterRepository());
      PlayerSessionActor playerSessionActor = runtime.openSession(protocolBridge);
      protocolBridge.bindOutbound(playerSessionActor::accept);

      gameplayClientSession = new GameplayClientSession(new io.github.ffakira.rsps.client.core.ClientCore(), protocolBridge, CLIENT_DESCRIPTOR);
      gameplayClientSession.bootstrap();
      gameplayClientSession.connect();

      try (OpenGlTileRenderSystem renderSystem =
               new OpenGlTileRenderSystem(
                   windowConfig.width(),
                   windowConfig.height(),
                   titleScreenAssets,
                   gameplayFrameAssets,
                   sceneTextureAssets,
                   itemDefinitionCatalog,
                   itemIconRenderer,
                   characterModelAssembler
               );
           GameplayClientSession session = gameplayClientSession) {
        LoginScreenController loginScreenController = new LoginScreenController();
        String[] titleScreenStatus = {WELCOME_STATUS};
        double[] mouseX = {0.0};
        double[] mouseY = {0.0};
        String[] activeSceneKey = {null};
        boolean[] gameplayBootstrapApplied = {false};

        renderSystem.setLoginScreenState(loginScreenController.state());
        bindWindowCallbacks(
            window,
            renderSystem,
            session,
            loginScreenController,
            titleScreenStatus,
            mouseX,
            mouseY
        );

        glfwShowWindow(window);
        while (!glfwWindowShouldClose(window)) {
          NativeClientRuntimeCoordinator.drainServerMessages(
              inboundMessages,
              session,
              loginScreenController,
              renderSystem,
              titleScreenStatus,
              activeSceneKey,
              WORLD_BOOTSTRAP_STATUS
          );
          NativeClientRuntimeCoordinator.refreshWorldScene(
              session.viewModel(),
              worldSceneLoader,
              renderSystem,
              activeSceneKey
          );
          if (NativeClientRuntimeCoordinator.isGameplayActive(session.viewModel())) {
            if (!gameplayBootstrapApplied[0]) {
              // Comparison screenshots should land on the populated inventory tab every time a
              // fresh gameplay bootstrap takes ownership of the window.
              renderSystem.resetGameplayTabForBootstrap();
              gameplayBootstrapApplied[0] = true;
            }
          } else {
            gameplayBootstrapApplied[0] = false;
          }
          renderSystem.render(NativeClientRuntimeCoordinator.renderViewModel(titleScreenStatus[0], session.viewModel()));
          glfwSwapBuffers(window);
          glfwPollEvents();
          if (Thread.interrupted()) {
            glfwSetWindowShouldClose(window, true);
          }
        }
      }
      gameplayClientSession = null;
    } finally {
      if (gameplayClientSession != null) {
        gameplayClientSession.close();
      }
      if (runtime != null) {
        runtime.close();
      }
      if (window != 0L) {
        glfwDestroyWindow(window);
      }
      glfwTerminate();
      errorCallback.free();
    }
  }

  private static void bindWindowCallbacks(
      long window,
      OpenGlTileRenderSystem renderSystem,
      GameplayClientSession session,
      LoginScreenController loginScreenController,
      String[] titleScreenStatus,
      double[] mouseX,
      double[] mouseY
  ) {
    // Input ownership is phase-based, not widget-based:
    // - before the authoritative runtime has produced a local player position, callbacks drive
    //   the native title/login flow
    // - once the runtime reports `loggedIn + localPlayerPosition`, the same window routes input
    //   to gameplay movement/tab interaction instead
    glfwSetFramebufferSizeCallback(window, (windowHandle, width, height) -> renderSystem.resize(width, height));
    glfwSetCursorPosCallback(window, (windowHandle, cursorX, cursorY) -> {
      mouseX[0] = cursorX;
      mouseY[0] = cursorY;
    });
    glfwSetCharCallback(window, (windowHandle, codePoint) -> {
      if (NativeClientRuntimeCoordinator.isGameplayActive(session.viewModel())) {
        return;
      }
      if (loginScreenController.state().stage() != TitleScreenStage.CREDENTIALS) {
        return;
      }
      loginScreenController.append(codePoint);
      renderSystem.setLoginScreenState(loginScreenController.state());
    });
    glfwSetMouseButtonCallback(window, (windowHandle, button, action, mods) -> {
      if (NativeClientRuntimeCoordinator.isGameplayActive(session.viewModel())) {
        if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
          GameplayClickResult clickResult = renderSystem.handleGameplayClick(mouseX[0], mouseY[0]);
          if (clickResult.hasMovementDelta()) {
            GameplayClickResult.MovementDelta movementDelta = clickResult.movementDelta();
            session.move(movementDelta.deltaX(), movementDelta.deltaY(), MovementMode.WALK);
          }
        }
        return;
      }
      if (button != GLFW_MOUSE_BUTTON_LEFT || action != GLFW_PRESS) {
        return;
      }
      handleLoginClick(mouseX[0], mouseY[0], loginScreenController, renderSystem, titleScreenStatus, session);
    });
    glfwSetKeyCallback(window, (windowHandle, key, scancode, action, mods) -> {
      if (NativeClientRuntimeCoordinator.isGameplayActive(session.viewModel())) {
        handleGameplayKey(windowHandle, key, action, renderSystem);
        return;
      }
      handleLoginKey(windowHandle, key, action, loginScreenController, renderSystem, titleScreenStatus, session);
    });
  }

  private static void handleLoginKey(
      long windowHandle,
      int key,
      int action,
      LoginScreenController loginScreenController,
      OpenGlTileRenderSystem renderSystem,
      String[] titleScreenStatus,
      GameplayClientSession gameplayClientSession
  ) {
    if (action != GLFW_PRESS && action != GLFW_REPEAT) {
      return;
    }
    LoginScreenState loginScreenState = loginScreenController.state();
    // The title screen still owns three separate interaction contracts:
    // - welcome screen: Enter progresses, Esc exits
    // - private-server info: Enter/Esc returns to credentials
    // - credentials: field editing plus submit/cancel behavior
    if (loginScreenState.stage() == TitleScreenStage.WELCOME) {
      if (key == GLFW_KEY_ENTER || key == GLFW_KEY_KP_ENTER) {
        loginScreenController.showCredentials();
        titleScreenStatus[0] = CREDENTIALS_STATUS;
        renderSystem.setLoginScreenState(loginScreenController.state());
      } else if (key == GLFW_KEY_ESCAPE) {
        glfwSetWindowShouldClose(windowHandle, true);
      }
      return;
    }
    if (loginScreenState.stage() == TitleScreenStage.PRIVATE_SERVER_INFO) {
      if (key == GLFW_KEY_ENTER || key == GLFW_KEY_KP_ENTER || key == GLFW_KEY_ESCAPE) {
        loginScreenController.showCredentials();
        titleScreenStatus[0] = CREDENTIALS_STATUS;
        renderSystem.setLoginScreenState(loginScreenController.state());
      }
      return;
    }
    switch (key) {
      case GLFW_KEY_ESCAPE -> {
        if (action == GLFW_PRESS) {
          loginScreenController.clear();
          loginScreenController.showWelcome();
          titleScreenStatus[0] = WELCOME_STATUS;
          renderSystem.setLoginScreenState(loginScreenController.state());
        }
      }
      case GLFW_KEY_TAB -> {
        if (action == GLFW_PRESS) {
          loginScreenController.focusNext();
          renderSystem.setLoginScreenState(loginScreenController.state());
        }
      }
      case GLFW_KEY_BACKSPACE -> {
        loginScreenController.backspace();
        renderSystem.setLoginScreenState(loginScreenController.state());
      }
      case GLFW_KEY_ENTER, GLFW_KEY_KP_ENTER -> {
        if (action != GLFW_PRESS) {
          return;
        }
        if (loginScreenController.advanceOrSubmitOnEnter()) {
          submitNativeLogin(loginScreenController.state(), titleScreenStatus, gameplayClientSession);
        }
        renderSystem.setLoginScreenState(loginScreenController.state());
      }
      default -> {
      }
    }
  }

  private static void handleLoginClick(
      double mouseX,
      double mouseY,
      LoginScreenController loginScreenController,
      OpenGlTileRenderSystem renderSystem,
      String[] titleScreenStatus,
      GameplayClientSession gameplayClientSession
  ) {
    TitleScreenLayout layout = renderSystem.currentTitleScreenLayout();
    LoginScreenState loginScreenState = loginScreenController.state();
    switch (loginScreenState.stage()) {
      case WELCOME -> {
        if (layout.infoButton().contains(mouseX, mouseY)) {
          loginScreenController.showPrivateServerInfo();
        } else if (layout.playNowButton().contains(mouseX, mouseY)) {
          loginScreenController.showCredentials();
          titleScreenStatus[0] = CREDENTIALS_STATUS;
        }
      }
      case CREDENTIALS -> {
        if (layout.usernameField().contains(mouseX, mouseY)) {
          loginScreenController.focus(LoginField.USERNAME);
        } else if (layout.passwordField().contains(mouseX, mouseY)) {
          loginScreenController.focus(LoginField.PASSWORD);
        } else if (layout.enterButton().contains(mouseX, mouseY) && loginScreenController.canSubmit()) {
          submitNativeLogin(loginScreenState, titleScreenStatus, gameplayClientSession);
        } else if (layout.cancelButton().contains(mouseX, mouseY)) {
          loginScreenController.clear();
          loginScreenController.showWelcome();
          titleScreenStatus[0] = WELCOME_STATUS;
        }
      }
      case PRIVATE_SERVER_INFO -> {
        if (layout.exitButton().contains(mouseX, mouseY)) {
          loginScreenController.showCredentials();
          titleScreenStatus[0] = CREDENTIALS_STATUS;
        }
      }
    }
    renderSystem.setLoginScreenState(loginScreenController.state());
  }

  private static void submitNativeLogin(
      LoginScreenState loginScreenState,
      String[] titleScreenStatus,
      GameplayClientSession gameplayClientSession
  ) {
    gameplayClientSession.login(loginScreenState.username(), loginScreenState.password());
    titleScreenStatus[0] = AUTHENTICATING_STATUS;
  }

  private static void handleGameplayKey(
      long windowHandle,
      int key,
      int action,
      OpenGlTileRenderSystem renderSystem
  ) {
    if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
      glfwSetWindowShouldClose(windowHandle, true);
      return;
    }
    if (action != GLFW_PRESS && action != GLFW_REPEAT) {
      return;
    }
    switch (key) {
      case GLFW_KEY_LEFT -> renderSystem.adjustGameplayCamera(-CAMERA_YAW_STEP_DEGREES, 0.0f);
      case GLFW_KEY_RIGHT -> renderSystem.adjustGameplayCamera(CAMERA_YAW_STEP_DEGREES, 0.0f);
      case GLFW_KEY_UP -> renderSystem.adjustGameplayCamera(0.0f, CAMERA_PITCH_STEP_DEGREES);
      case GLFW_KEY_DOWN -> renderSystem.adjustGameplayCamera(0.0f, -CAMERA_PITCH_STEP_DEGREES);
      default -> {
      }
    }
  }
}
