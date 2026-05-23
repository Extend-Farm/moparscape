package com.veyrmoor.client.desktop.platform.input;

import com.veyrmoor.client.core.GameplayClientSession;
import com.veyrmoor.client.desktop.gameplay.GameplayChatController;
import com.veyrmoor.client.desktop.gameplay.GameplayClickResult;
import com.veyrmoor.client.desktop.gameplay.GameplayMouseButton;
import com.veyrmoor.client.desktop.render.opengl.OpenGlTileRenderSystem;
import com.veyrmoor.model.MovementMode;
import java.util.function.Supplier;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

final class GameplayInputHandler {

  private final GameplayInputPort inputPort;
  private final MovementPort movementPort;
  private final PublicChatPort publicChatPort;
  private final GameplayChatController chatController;
  private final WindowClosePort windowClosePort;
  private final LogoutPort logoutPort;
  private boolean rotateLeftPressed;
  private boolean rotateRightPressed;
  private boolean pitchUpPressed;
  private boolean pitchDownPressed;
  private boolean runModifierPressed;
  private boolean leftMouseButtonPressed;

  GameplayInputHandler(
      OpenGlTileRenderSystem renderSystem,
      Supplier<GameplayClientSession> gameplayClientSessionSupplier,
      LogoutPort logoutPort
  ) {
    this(
        renderSystem::handleGameplayClick,
        renderSystem::handleGameplayScroll,
        renderSystem::handleGameplayPointerMove,
        renderSystem::endGameplayPointerDrag,
        renderSystem::setGameplayCameraInputs,
        (deltaX, deltaY, movementMode) -> gameplayClientSessionSupplier.get().move(deltaX, deltaY, movementMode),
        text -> gameplayClientSessionSupplier.get().sendPublicChat(text),
        renderSystem.gameplayChatController(),
        windowHandle -> glfwSetWindowShouldClose(windowHandle, true),
        logoutPort
    );
  }

  GameplayInputHandler(
      GameplayClickPort gameplayClickPort,
      GameplayCameraInputPort gameplayCameraInputPort,
      MovementPort movementPort
  ) {
    this(
        gameplayClickPort,
        (mouseX, mouseY, yOffset) -> false,
        (mouseX, mouseY) -> false,
        () -> {
        },
        gameplayCameraInputPort,
        movementPort,
        text -> {
        },
        new GameplayChatController(),
        windowHandle -> glfwSetWindowShouldClose(windowHandle, true),
        () -> {
        }
    );
  }

  GameplayInputHandler(
      GameplayClickPort gameplayClickPort,
      GameplayCameraInputPort gameplayCameraInputPort,
      MovementPort movementPort,
      WindowClosePort windowClosePort
  ) {
    this(
        gameplayClickPort,
        (mouseX, mouseY, yOffset) -> false,
        (mouseX, mouseY) -> false,
        () -> {
        },
        gameplayCameraInputPort,
        movementPort,
        text -> {
        },
        new GameplayChatController(),
        windowClosePort,
        () -> {
        }
    );
  }

  GameplayInputHandler(
      GameplayClickPort gameplayClickPort,
      GameplayScrollPort gameplayScrollPort,
      GameplayPointerMovePort gameplayPointerMovePort,
      PointerDragEndPort pointerDragEndPort,
      GameplayCameraInputPort gameplayCameraInputPort,
      MovementPort movementPort,
      PublicChatPort publicChatPort,
      GameplayChatController chatController,
      WindowClosePort windowClosePort,
      LogoutPort logoutPort
  ) {
    this.inputPort = new GameplayInputPort(
        gameplayClickPort,
        gameplayScrollPort,
        gameplayPointerMovePort,
        pointerDragEndPort,
        gameplayCameraInputPort
    );
    this.movementPort = movementPort;
    this.publicChatPort = publicChatPort;
    this.chatController = chatController;
    this.windowClosePort = windowClosePort;
    this.logoutPort = logoutPort;
  }

  GameplayInputHandler(
      GameplayClickPort gameplayClickPort,
      GameplayScrollPort gameplayScrollPort,
      GameplayPointerMovePort gameplayPointerMovePort,
      PointerDragEndPort pointerDragEndPort,
      GameplayCameraInputPort gameplayCameraInputPort,
      MovementPort movementPort,
      WindowClosePort windowClosePort,
      LogoutPort logoutPort
  ) {
    this(
        gameplayClickPort,
        gameplayScrollPort,
        gameplayPointerMovePort,
        pointerDragEndPort,
        gameplayCameraInputPort,
        movementPort,
        text -> {
        },
        new GameplayChatController(),
        windowClosePort,
        logoutPort
    );
  }

  GameplayInputHandler(
      GameplayClickPort gameplayClickPort,
      GameplayCameraInputPort gameplayCameraInputPort,
      MovementPort movementPort,
      WindowClosePort windowClosePort,
      LogoutPort logoutPort
  ) {
    this(
        gameplayClickPort,
        (mouseX, mouseY, yOffset) -> false,
        (mouseX, mouseY) -> false,
        () -> {
        },
        gameplayCameraInputPort,
        movementPort,
        text -> {
        },
        new GameplayChatController(),
        windowClosePort,
        logoutPort
    );
  }

  GameplayInputHandler(
      GameplayClickPort gameplayClickPort,
      GameplayCameraInputPort gameplayCameraInputPort,
      MovementPort movementPort,
      PublicChatPort publicChatPort,
      GameplayChatController chatController,
      WindowClosePort windowClosePort,
      LogoutPort logoutPort
  ) {
    this(
        gameplayClickPort,
        (mouseX, mouseY, yOffset) -> false,
        (mouseX, mouseY) -> false,
        () -> {
        },
        gameplayCameraInputPort,
        movementPort,
        publicChatPort,
        chatController,
        windowClosePort,
        logoutPort
    );
  }

  void handleMouseButton(long windowHandle, int button, int action, double mouseX, double mouseY) {
    GameplayMouseButton gameplayMouseButton = gameplayMouseButton(button);
    if (gameplayMouseButton == null) {
      return;
    }
    if (gameplayMouseButton == GameplayMouseButton.LEFT) {
      if (action == GLFW_PRESS) {
        leftMouseButtonPressed = true;
      } else if (action == GLFW_RELEASE) {
        leftMouseButtonPressed = false;
        inputPort.endPointerDrag();
      }
    }
    if (action != GLFW_PRESS) {
      return;
    }
    GameplayClickResult clickResult = inputPort.handleGameplayClick(mouseX, mouseY, gameplayMouseButton);
    if (clickResult.requestsLogout()) {
      clearCameraInputs();
      logoutPort.requestLogout();
      return;
    }
    if (clickResult.requestsClientClose()) {
      windowClosePort.requestClose(windowHandle);
      return;
    }
    if (!clickResult.hasMovementDelta()) {
      return;
    }
    GameplayClickResult.MovementDelta movementDelta = clickResult.movementDelta();
    movementPort.move(
        movementDelta.deltaX(),
        movementDelta.deltaY(),
        runModifierPressed ? MovementMode.RUN : MovementMode.WALK
    );
  }

  void handleKey(long windowHandle, int key, int action) {
    if (action != GLFW_PRESS && action != GLFW_REPEAT && action != GLFW_RELEASE) {
      return;
    }
    if (handleChatKey(key, action)) {
      return;
    }
    if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
      windowClosePort.requestClose(windowHandle);
      return;
    }
    if (isEnterKey(key) && action == GLFW_PRESS) {
      chatController.activateTyping();
      return;
    }
    switch (key) {
      case GLFW_KEY_LEFT -> {
        rotateLeftPressed = action != GLFW_RELEASE;
        applyCameraInputs();
      }
      case GLFW_KEY_RIGHT -> {
        rotateRightPressed = action != GLFW_RELEASE;
        applyCameraInputs();
      }
      case GLFW_KEY_UP -> {
        pitchUpPressed = action != GLFW_RELEASE;
        applyCameraInputs();
      }
      case GLFW_KEY_DOWN -> {
        pitchDownPressed = action != GLFW_RELEASE;
        applyCameraInputs();
      }
      case GLFW_KEY_LEFT_SHIFT, GLFW_KEY_RIGHT_SHIFT -> runModifierPressed = action != GLFW_RELEASE;
      default -> {
      }
    }
  }

  void handleCharacter(int codePoint) {
    if (!chatController.isTyping()) {
      chatController.activateTyping();
    }
    chatController.appendCodePoint(codePoint);
  }

  void clearCameraInputs() {
    rotateLeftPressed = false;
    rotateRightPressed = false;
    pitchUpPressed = false;
    pitchDownPressed = false;
    runModifierPressed = false;
    leftMouseButtonPressed = false;
    inputPort.endPointerDrag();
    applyCameraInputs();
  }

  void handleScroll(double mouseX, double mouseY, double yOffset) {
    inputPort.handleGameplayScroll(mouseX, mouseY, yOffset);
  }

  void handlePointerMove(double mouseX, double mouseY) {
    if (!leftMouseButtonPressed) {
      return;
    }
    inputPort.handleGameplayPointerMove(mouseX, mouseY);
  }

  private void applyCameraInputs() {
    inputPort.setGameplayCameraInputs(rotateLeftPressed, rotateRightPressed, pitchUpPressed, pitchDownPressed);
  }

  private boolean handleChatKey(int key, int action) {
    if (!chatController.isTyping()) {
      return false;
    }
    if (isCameraControlKey(key) || isModifierKey(key)) {
      return false;
    }
    if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
      chatController.cancelTyping();
      return true;
    }
    if (isEnterKey(key) && action == GLFW_PRESS) {
      String submittedText = chatController.submitDraft();
      if (submittedText != null) {
        publicChatPort.sendPublicChat(submittedText);
      }
      return true;
    }
    if (key == GLFW_KEY_BACKSPACE && (action == GLFW_PRESS || action == GLFW_REPEAT)) {
      chatController.backspace();
      return true;
    }
    return action == GLFW_PRESS || action == GLFW_REPEAT;
  }

  private static boolean isEnterKey(int key) {
    return key == GLFW_KEY_ENTER || key == GLFW_KEY_KP_ENTER;
  }

  private static boolean isCameraControlKey(int key) {
    return key == GLFW_KEY_LEFT
        || key == GLFW_KEY_RIGHT
        || key == GLFW_KEY_UP
        || key == GLFW_KEY_DOWN;
  }

  private static boolean isModifierKey(int key) {
    return key == GLFW_KEY_LEFT_SHIFT || key == GLFW_KEY_RIGHT_SHIFT;
  }

  private static GameplayMouseButton gameplayMouseButton(int button) {
    return switch (button) {
      case GLFW_MOUSE_BUTTON_LEFT -> GameplayMouseButton.LEFT;
      case GLFW_MOUSE_BUTTON_RIGHT -> GameplayMouseButton.RIGHT;
      default -> null;
    };
  }

  @FunctionalInterface
  interface GameplayClickPort {
    GameplayClickResult handleGameplayClick(double mouseX, double mouseY, GameplayMouseButton button);
  }

  @FunctionalInterface
  interface GameplayCameraInputPort {
    void setGameplayCameraInputs(
        boolean rotateLeftPressed,
        boolean rotateRightPressed,
        boolean pitchUpPressed,
        boolean pitchDownPressed
    );
  }

  @FunctionalInterface
  interface GameplayScrollPort {
    boolean handleGameplayScroll(double mouseX, double mouseY, double yOffset);
  }

  @FunctionalInterface
  interface GameplayPointerMovePort {
    boolean handleGameplayPointerMove(double mouseX, double mouseY);
  }

  @FunctionalInterface
  interface PointerDragEndPort {
    void endPointerDrag();
  }

  @FunctionalInterface
  interface MovementPort {
    void move(int deltaX, int deltaY, MovementMode movementMode);
  }

  @FunctionalInterface
  interface PublicChatPort {
    void sendPublicChat(String text);
  }

  @FunctionalInterface
  interface WindowClosePort {
    void requestClose(long windowHandle);
  }

  @FunctionalInterface
  interface LogoutPort {
    void requestLogout();
  }

  private record GameplayInputPort(
      GameplayClickPort clickPort,
      GameplayScrollPort scrollPort,
      GameplayPointerMovePort pointerMovePort,
      PointerDragEndPort pointerDragEndPort,
      GameplayCameraInputPort cameraInputPort
  ) {

    private GameplayClickResult handleGameplayClick(double mouseX, double mouseY, GameplayMouseButton button) {
      return clickPort.handleGameplayClick(mouseX, mouseY, button);
    }

    private void setGameplayCameraInputs(
        boolean rotateLeftPressed,
        boolean rotateRightPressed,
        boolean pitchUpPressed,
        boolean pitchDownPressed
    ) {
      cameraInputPort.setGameplayCameraInputs(rotateLeftPressed, rotateRightPressed, pitchUpPressed, pitchDownPressed);
    }

    private boolean handleGameplayScroll(double mouseX, double mouseY, double yOffset) {
      return scrollPort.handleGameplayScroll(mouseX, mouseY, yOffset);
    }

    private boolean handleGameplayPointerMove(double mouseX, double mouseY) {
      return pointerMovePort.handleGameplayPointerMove(mouseX, mouseY);
    }

    private void endPointerDrag() {
      pointerDragEndPort.endPointerDrag();
    }
  }
}
