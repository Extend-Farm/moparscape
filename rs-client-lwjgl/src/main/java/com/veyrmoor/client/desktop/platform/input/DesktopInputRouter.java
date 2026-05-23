package com.veyrmoor.client.desktop.platform.input;

import com.veyrmoor.client.core.GameplayClientSession;
import com.veyrmoor.client.desktop.app.DesktopClientState;
import com.veyrmoor.client.desktop.login.LoginScreenController;
import com.veyrmoor.client.desktop.render.opengl.OpenGlTileRenderSystem;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_WORLD_2;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.system.MemoryStack.stackPush;

public final class DesktopInputRouter {

  static final long GAMEPLAY_CHAT_REPEAT_INITIAL_DELAY_NANOS = 120_000_000L;
  static final long GAMEPLAY_CHAT_REPEAT_INTERVAL_NANOS = 33_000_000L;

  private final long window;
  private final OpenGlTileRenderSystem renderSystem;
  private final DesktopClientState state;
  private final LoginInputPort loginInputHandler;
  private final GameplayInputHandler gameplayInputHandler;
  private final BooleanSupplier gameplayActiveSupplier;
  private PendingGameplayCharacterKey pendingGameplayCharacterKey;
  private ActiveGameplayCharacterRepeat activeGameplayCharacterRepeat;

  public DesktopInputRouter(
      long window,
      OpenGlTileRenderSystem renderSystem,
      Supplier<GameplayClientSession> gameplayClientSessionSupplier,
      Runnable logoutAction,
      LoginScreenController loginScreenController,
      DesktopClientState state,
      String welcomeStatus,
      String credentialsStatus,
      String authenticatingStatus
  ) {
    this.window = window;
    this.renderSystem = renderSystem;
    this.state = state;
    this.loginInputHandler = new LoginInputHandler(
        renderSystem,
        loginScreenController,
        gameplayClientSessionSupplier,
        state,
        welcomeStatus,
        credentialsStatus,
        authenticatingStatus
    );
    this.gameplayInputHandler = new GameplayInputHandler(
        renderSystem,
        gameplayClientSessionSupplier,
        logoutAction::run
    );
    this.gameplayActiveSupplier = () ->
        isGameplayActive(gameplayClientSessionSupplier.get());
  }

  DesktopInputRouter(
      long window,
      OpenGlTileRenderSystem renderSystem,
      DesktopClientState state,
      LoginInputPort loginInputHandler,
      GameplayInputHandler gameplayInputHandler,
      BooleanSupplier gameplayActiveSupplier
  ) {
    this.window = window;
    this.renderSystem = renderSystem;
    this.state = state;
    this.loginInputHandler = loginInputHandler;
    this.gameplayInputHandler = gameplayInputHandler;
    this.gameplayActiveSupplier = gameplayActiveSupplier;
  }

  public void bind() {
    // Input ownership is phase-based, not widget-based:
    // - before the authoritative runtime has produced a local player position, callbacks drive the
    //   native title/login flow
    // - once the runtime reports `loggedIn + localPlayerPosition`, the same window routes input to
    //   gameplay movement/tab interaction instead
    glfwSetFramebufferSizeCallback(window, (windowHandle, width, height) -> renderSystem.resize(width, height));
    glfwSetCursorPosCallback(window, (windowHandle, cursorX, cursorY) -> updatePointerPosition(cursorX, cursorY));
    glfwSetCharCallback(window, (windowHandle, codePoint) -> {
      routeCharacter(codePoint);
    });
    glfwSetMouseButtonCallback(window, (windowHandle, button, action, mods) -> {
      syncMousePosition(windowHandle);
      routeMouseButton(windowHandle, button, action);
    });
    glfwSetScrollCallback(window, (windowHandle, xOffset, yOffset) -> {
      syncMousePosition(windowHandle);
      routeScroll(yOffset);
    });
    glfwSetKeyCallback(window, (windowHandle, key, scancode, action, mods) -> {
      routeKey(windowHandle, key, scancode, action, mods);
    });
  }

  void routeCharacter(int codePoint) {
    if (isGameplayActive()) {
      if (shouldSuppressGameplayCharacter(codePoint)) {
        return;
      }
      gameplayInputHandler.handleCharacter(codePoint);
      startGameplayCharacterRepeat(codePoint, System.nanoTime());
      return;
    }
    clearGameplayCharacterRepeatState();
    loginInputHandler.handleCharacter(codePoint);
  }

  void routeMouseButton(long windowHandle, int button, int action) {
    if (isGameplayActive()) {
      gameplayInputHandler.handleMouseButton(windowHandle, button, action, state.mouseX(), state.mouseY());
      return;
    }
    loginInputHandler.handleMouseButton(button, action, state.mouseX(), state.mouseY());
  }

  void routeKey(long windowHandle, int key, int action) {
    routeKey(windowHandle, key, 0, action, 0);
  }

  void routeKey(long windowHandle, int key, int scancode, int action, int mods) {
    if (isGameplayActive()) {
      trackGameplayCharacterRepeatKey(key, action);
      gameplayInputHandler.handleKey(windowHandle, key, action);
      return;
    }
    clearGameplayCharacterRepeatState();
    gameplayInputHandler.clearCameraInputs();
    loginInputHandler.handleKey(windowHandle, key, action);
  }

  void routeScroll(double yOffset) {
    if (!isGameplayActive()) {
      return;
    }
    gameplayInputHandler.handleScroll(state.mouseX(), state.mouseY(), yOffset);
  }

  public void advanceFrame(long nowNanos) {
    if (!isGameplayActive()) {
      clearGameplayCharacterRepeatState();
      return;
    }
    if (activeGameplayCharacterRepeat == null || nowNanos < activeGameplayCharacterRepeat.nextRepeatAtNanos()) {
      return;
    }
    gameplayInputHandler.handleCharacter(activeGameplayCharacterRepeat.codePoint());
    activeGameplayCharacterRepeat =
        activeGameplayCharacterRepeat.withNextRepeatAtNanos(nowNanos + GAMEPLAY_CHAT_REPEAT_INTERVAL_NANOS);
  }

  private boolean isGameplayActive() {
    return gameplayActiveSupplier.getAsBoolean();
  }

  private void trackGameplayCharacterRepeatKey(int key, int action) {
    if (action == GLFW_PRESS && isPrintableGameplayKey(key)) {
      pendingGameplayCharacterKey = new PendingGameplayCharacterKey(key);
      return;
    }
    if (action != GLFW_RELEASE) {
      return;
    }
    if (pendingGameplayCharacterKey != null && pendingGameplayCharacterKey.key() == key) {
      pendingGameplayCharacterKey = null;
    }
    if (activeGameplayCharacterRepeat != null && activeGameplayCharacterRepeat.key() == key) {
      activeGameplayCharacterRepeat = null;
    }
  }

  private void startGameplayCharacterRepeat(int codePoint, long nowNanos) {
    if (pendingGameplayCharacterKey == null) {
      return;
    }
    activeGameplayCharacterRepeat = new ActiveGameplayCharacterRepeat(
        pendingGameplayCharacterKey.key(),
        codePoint,
        nowNanos + GAMEPLAY_CHAT_REPEAT_INITIAL_DELAY_NANOS
    );
    pendingGameplayCharacterKey = null;
  }

  private boolean shouldSuppressGameplayCharacter(int codePoint) {
    return activeGameplayCharacterRepeat != null && activeGameplayCharacterRepeat.codePoint() == codePoint;
  }

  private void clearGameplayCharacterRepeatState() {
    pendingGameplayCharacterKey = null;
    activeGameplayCharacterRepeat = null;
  }

  private static boolean isPrintableGameplayKey(int key) {
    return key >= GLFW_KEY_SPACE
        && key <= GLFW_KEY_WORLD_2
        && key != GLFW_KEY_ENTER
        && key != GLFW_KEY_KP_ENTER
        && key != GLFW_KEY_BACKSPACE
        && key != GLFW_KEY_ESCAPE
        && key != GLFW_KEY_LEFT
        && key != GLFW_KEY_RIGHT
        && key != GLFW_KEY_UP
        && key != GLFW_KEY_DOWN
        && key != GLFW_KEY_LEFT_SHIFT
        && key != GLFW_KEY_RIGHT_SHIFT;
  }

  private static boolean isGameplayActive(GameplayClientSession gameplayClientSession) {
    var viewModel = gameplayClientSession.viewModel();
    return viewModel.loggedIn() && viewModel.localPlayerPosition() != null;
  }

  private void updatePointerPosition(double mouseX, double mouseY) {
    state.setMousePosition(mouseX, mouseY);
    renderSystem.setPointerPosition(mouseX, mouseY);
    if (isGameplayActive()) {
      gameplayInputHandler.handlePointerMove(mouseX, mouseY);
    }
  }

  private void syncMousePosition(long windowHandle) {
    try (MemoryStack stack = stackPush()) {
      var cursorX = stack.mallocDouble(1);
      var cursorY = stack.mallocDouble(1);
      glfwGetCursorPos(windowHandle, cursorX, cursorY);
      updatePointerPosition(cursorX.get(0), cursorY.get(0));
    }
  }

  interface LoginInputPort {
    void handleCharacter(int codePoint);

    void handleMouseButton(int button, int action, double mouseX, double mouseY);

    void handleKey(long windowHandle, int key, int action);
  }

  private record PendingGameplayCharacterKey(int key) {
  }

  private record ActiveGameplayCharacterRepeat(int key, int codePoint, long nextRepeatAtNanos) {

    private ActiveGameplayCharacterRepeat withNextRepeatAtNanos(long nextRepeatAtNanos) {
      return new ActiveGameplayCharacterRepeat(key, codePoint, nextRepeatAtNanos);
    }
  }
}
