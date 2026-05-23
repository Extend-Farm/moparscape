package com.veyrmoor.client.desktop.app;

import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.desktop.assets.NativeClientAssets;
import com.veyrmoor.client.desktop.login.LoginScreenController;
import com.veyrmoor.client.desktop.login.TitleScreenAssets;
import com.veyrmoor.client.desktop.platform.input.DesktopInputRouter;
import com.veyrmoor.client.desktop.platform.window.DesktopWindowConfig;
import com.veyrmoor.client.desktop.render.opengl.OpenGlTileRenderSystem;
import com.veyrmoor.client.desktop.world.CacheBackedWorldSceneLoader;
import java.nio.file.Path;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.GLFW_DEPTH_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

final class DesktopClientApplication implements AutoCloseable {

  private static final String LOADING_STATUS = "Preparing login frame";
  private static final String WELCOME_STATUS = "Welcome to Veyrmoor";
  private static final String CREDENTIALS_STATUS = "Enter your username and password.";
  private static final String AUTHENTICATING_STATUS = "Connecting to server...";
  private static final String WORLD_BOOTSTRAP_STATUS = "Loading world...";
  private static final String CLIENT_DESCRIPTOR = "rs-client-lwjgl";

  private GLFWErrorCallback errorCallback;
  private long window;
  private boolean glfwInitialized;
  private NativeClientRuntimeContext runtimeContext;

  void run() {
    initializeGlfw();

    DesktopWindowConfig windowConfig = DesktopWindowConfig.defaults();
    createWindow(windowConfig);

    Path workingDirectory = Path.of(".").toAbsolutePath().normalize();
    TitleScreenAssets titleScreenAssets = NativeClientBootstrap.loadTitleScreenAssets(workingDirectory);
    LoginScreenController loginScreenController = new LoginScreenController();
    NativeClientAssets assets;
    try (OpenGlTileRenderSystem loadingRenderSystem = createLoadingRenderSystem(windowConfig, titleScreenAssets)) {
      DesktopClientState loadingState = new DesktopClientState(LOADING_STATUS);
      glfwShowWindow(window);
      updateLoadingScreen(loadingRenderSystem, loginScreenController, loadingState, 5, LOADING_STATUS);
      if (glfwWindowShouldClose(window)) {
        return;
      }
      assets = NativeClientBootstrap.loadAssets(
          workingDirectory,
          titleScreenAssets,
          (progressPercent, statusText) -> updateLoadingScreen(
              loadingRenderSystem,
              loginScreenController,
              loadingState,
              progressPercent,
              statusText
          )
      );
      if (glfwWindowShouldClose(window)) {
        return;
      }
      updateLoadingScreen(loadingRenderSystem, loginScreenController, loadingState, 95, "Connecting to fileserver");
      runtimeContext = NativeClientBootstrap.openRuntimeContext(workingDirectory, CLIENT_DESCRIPTOR);
      updateLoadingScreen(loadingRenderSystem, loginScreenController, loadingState, 100, "Login frame ready");
    }
    if (glfwWindowShouldClose(window)) {
      return;
    }

    try (OpenGlTileRenderSystem renderSystem = assets.createRenderSystem(windowConfig)) {
      loginScreenController.showWelcome();
      DesktopClientState state = new DesktopClientState(WELCOME_STATUS);

      renderSystem.setLoginScreenState(loginScreenController.state());
      new DesktopInputRouter(
          window,
          renderSystem,
          () -> requireRuntimeContext().session(),
          () -> restartRuntimeForLogin(workingDirectory, loginScreenController, renderSystem, state),
          loginScreenController,
          state,
          WELCOME_STATUS,
          CREDENTIALS_STATUS,
          AUTHENTICATING_STATUS
      ).bind();

      runLoop(renderSystem, loginScreenController, state, assets.worldSceneLoader());
    }
  }

  @Override
  public void close() {
    if (runtimeContext != null) {
      runtimeContext.close();
      runtimeContext = null;
    }
    if (window != 0L) {
      glfwDestroyWindow(window);
      window = 0L;
    }
    if (glfwInitialized) {
      glfwTerminate();
      glfwInitialized = false;
    }
    if (errorCallback != null) {
      errorCallback.free();
      errorCallback = null;
    }
  }

  private void initializeGlfw() {
    errorCallback = GLFWErrorCallback.createPrint(System.err);
    errorCallback.set();
    if (!glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }
    glfwInitialized = true;
  }

  private void createWindow(DesktopWindowConfig windowConfig) {
    glfwDefaultWindowHints();
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
    // The gameplay viewport now depends on explicit depth testing and benefits noticeably from
    // multisampling. Make the 3D surface contract explicit at window creation instead of relying
    // on platform defaults.
    glfwWindowHint(GLFW_DEPTH_BITS, 24);
    glfwWindowHint(GLFW_SAMPLES, 4);
    window = glfwCreateWindow(windowConfig.width(), windowConfig.height(), windowConfig.title(), 0L, 0L);
    if (window == 0L) {
      throw new IllegalStateException("Unable to create LWJGL window");
    }
    glfwMakeContextCurrent(window);
    glfwSwapInterval(1);
    GL.createCapabilities();
  }

  private OpenGlTileRenderSystem createLoadingRenderSystem(
      DesktopWindowConfig windowConfig,
      TitleScreenAssets titleScreenAssets
  ) {
    return new OpenGlTileRenderSystem(
        windowConfig.width(),
        windowConfig.height(),
        titleScreenAssets,
        null,
        null,
        null,
        null,
        null,
        null
    );
  }

  private void updateLoadingScreen(
      OpenGlTileRenderSystem renderSystem,
      LoginScreenController loginScreenController,
      DesktopClientState state,
      int progressPercent,
      String statusText
  ) {
    loginScreenController.showLoading(progressPercent);
    renderSystem.setLoginScreenState(loginScreenController.state());
    state.setTitleScreenStatus(statusText);
    renderSystem.render(new ClientViewModel(state.titleScreenStatus(), false, null));
    glfwSwapBuffers(window);
    glfwPollEvents();
  }

  private void runLoop(
      OpenGlTileRenderSystem renderSystem,
      LoginScreenController loginScreenController,
      DesktopClientState state,
      CacheBackedWorldSceneLoader worldSceneLoader
  ) {
    while (!glfwWindowShouldClose(window)) {
      // Poll before the heavier world/update/render path so queued key presses land in the same
      // frame instead of waiting behind a full render pass.
      glfwPollEvents();
      if (glfwWindowShouldClose(window)) {
        return;
      }
      NativeClientRuntimeContext activeRuntimeContext = requireRuntimeContext();
      var session = activeRuntimeContext.session();
      NativeClientRuntimeCoordinator.drainServerMessages(
          activeRuntimeContext.inboundMessages(),
          session,
          loginScreenController,
          renderSystem,
          state,
          WORLD_BOOTSTRAP_STATUS
      );
      NativeClientRuntimeCoordinator.refreshWorldScene(
          session.viewModel(),
          worldSceneLoader,
          renderSystem,
          state
      );
      updateGameplayBootstrapState(renderSystem, session.viewModel(), state);
      session.pumpMovement();
      renderSystem.render(NativeClientRuntimeCoordinator.renderViewModel(state.titleScreenStatus(), session.viewModel()));
      glfwSwapBuffers(window);
      glfwPollEvents();
      if (Thread.currentThread().isInterrupted()) {
        glfwSetWindowShouldClose(window, true);
      }
    }
  }

  private NativeClientRuntimeContext requireRuntimeContext() {
    if (runtimeContext == null) {
      throw new IllegalStateException("Native runtime context is not available");
    }
    return runtimeContext;
  }

  private void restartRuntimeForLogin(
      Path workingDirectory,
      LoginScreenController loginScreenController,
      OpenGlTileRenderSystem renderSystem,
      DesktopClientState state
  ) {
    if (runtimeContext != null) {
      runtimeContext.close();
    }
    runtimeContext = NativeClientBootstrap.openRuntimeContext(workingDirectory, CLIENT_DESCRIPTOR);
    loginScreenController.showCredentials();
    renderSystem.setLoginScreenState(loginScreenController.state());
    renderSystem.clearWorldScene();
    state.setActiveSceneKey(null);
    state.setGameplayBootstrapApplied(false);
    state.setTitleScreenStatus(CREDENTIALS_STATUS);
  }

  private void updateGameplayBootstrapState(
      OpenGlTileRenderSystem renderSystem,
      ClientViewModel viewModel,
      DesktopClientState state
  ) {
    if (NativeClientRuntimeCoordinator.isGameplayActive(viewModel)) {
      if (!state.gameplayBootstrapApplied()) {
        // Comparison screenshots should land on the populated inventory tab every time a fresh
        // gameplay bootstrap takes ownership of the window.
        renderSystem.resetGameplayTabForBootstrap();
        state.setGameplayBootstrapApplied(true);
      }
      return;
    }
    state.setGameplayBootstrapApplied(false);
  }
}
