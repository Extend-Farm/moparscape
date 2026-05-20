package io.github.ffakira.rsps.client.desktop.core;

import io.github.ffakira.rsps.client.core.GameplayClientSession;
import io.github.ffakira.rsps.client.desktop.login.LoginScreenController;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.system.MemoryStack.stackPush;

final class DesktopInputRouter {

  private final long window;
  private final OpenGlTileRenderSystem renderSystem;
  private final GameplayClientSession gameplayClientSession;
  private final DesktopClientState state;
  private final LoginInputHandler loginInputHandler;
  private final GameplayInputHandler gameplayInputHandler;

  DesktopInputRouter(
      long window,
      OpenGlTileRenderSystem renderSystem,
      GameplayClientSession gameplayClientSession,
      LoginScreenController loginScreenController,
      DesktopClientState state,
      String welcomeStatus,
      String credentialsStatus,
      String authenticatingStatus
  ) {
    this.window = window;
    this.renderSystem = renderSystem;
    this.gameplayClientSession = gameplayClientSession;
    this.state = state;
    this.loginInputHandler = new LoginInputHandler(
        renderSystem,
        loginScreenController,
        gameplayClientSession,
        state,
        welcomeStatus,
        credentialsStatus,
        authenticatingStatus
    );
    this.gameplayInputHandler = new GameplayInputHandler(renderSystem, gameplayClientSession);
  }

  void bind() {
    // Input ownership is phase-based, not widget-based:
    // - before the authoritative runtime has produced a local player position, callbacks drive the
    //   native title/login flow
    // - once the runtime reports `loggedIn + localPlayerPosition`, the same window routes input to
    //   gameplay movement/tab interaction instead
    glfwSetFramebufferSizeCallback(window, (windowHandle, width, height) -> renderSystem.resize(width, height));
    glfwSetCursorPosCallback(window, (windowHandle, cursorX, cursorY) -> updatePointerPosition(cursorX, cursorY));
    glfwSetCharCallback(window, (windowHandle, codePoint) -> {
      if (isGameplayActive()) {
        return;
      }
      loginInputHandler.handleCharacter(codePoint);
    });
    glfwSetMouseButtonCallback(window, (windowHandle, button, action, mods) -> {
      syncMousePosition(windowHandle);
      if (isGameplayActive()) {
        gameplayInputHandler.handleMouseButton(button, action, state.mouseX(), state.mouseY());
        return;
      }
      loginInputHandler.handleMouseButton(button, action, state.mouseX(), state.mouseY());
    });
    glfwSetKeyCallback(window, (windowHandle, key, scancode, action, mods) -> {
      if (isGameplayActive()) {
        gameplayInputHandler.handleKey(windowHandle, key, action);
        return;
      }
      loginInputHandler.handleKey(windowHandle, key, action);
    });
  }

  private boolean isGameplayActive() {
    return NativeClientRuntimeCoordinator.isGameplayActive(gameplayClientSession.viewModel());
  }

  private void updatePointerPosition(double mouseX, double mouseY) {
    state.setMousePosition(mouseX, mouseY);
    renderSystem.setPointerPosition(mouseX, mouseY);
  }

  private void syncMousePosition(long windowHandle) {
    try (MemoryStack stack = stackPush()) {
      var cursorX = stack.mallocDouble(1);
      var cursorY = stack.mallocDouble(1);
      glfwGetCursorPos(windowHandle, cursorX, cursorY);
      updatePointerPosition(cursorX.get(0), cursorY.get(0));
    }
  }
}
