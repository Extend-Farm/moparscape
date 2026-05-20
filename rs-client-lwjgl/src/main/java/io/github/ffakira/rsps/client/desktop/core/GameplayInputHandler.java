package io.github.ffakira.rsps.client.desktop.core;

import io.github.ffakira.rsps.client.core.GameplayClientSession;
import io.github.ffakira.rsps.client.desktop.gameplay.GameplayClickResult;
import io.github.ffakira.rsps.client.desktop.gameplay.GameplayMouseButton;
import io.github.ffakira.rsps.model.MovementMode;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
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
  private boolean rotateLeftPressed;
  private boolean rotateRightPressed;
  private boolean pitchUpPressed;
  private boolean pitchDownPressed;

  GameplayInputHandler(OpenGlTileRenderSystem renderSystem, GameplayClientSession gameplayClientSession) {
    this(
        renderSystem::handleGameplayClick,
        renderSystem::setGameplayCameraInputs,
        gameplayClientSession::move
    );
  }

  GameplayInputHandler(
      GameplayClickPort gameplayClickPort,
      GameplayCameraInputPort gameplayCameraInputPort,
      MovementPort movementPort
  ) {
    this.inputPort = new GameplayInputPort(gameplayClickPort, gameplayCameraInputPort);
    this.movementPort = movementPort;
  }

  void handleMouseButton(int button, int action, double mouseX, double mouseY) {
    GameplayMouseButton gameplayMouseButton = gameplayMouseButton(button);
    if (gameplayMouseButton == null || action != GLFW_PRESS) {
      return;
    }
    GameplayClickResult clickResult = inputPort.handleGameplayClick(mouseX, mouseY, gameplayMouseButton);
    if (!clickResult.hasMovementDelta()) {
      return;
    }
    GameplayClickResult.MovementDelta movementDelta = clickResult.movementDelta();
    movementPort.move(movementDelta.deltaX(), movementDelta.deltaY(), MovementMode.WALK);
  }

  void handleKey(long windowHandle, int key, int action) {
    if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
      glfwSetWindowShouldClose(windowHandle, true);
      return;
    }
    if (action != GLFW_PRESS && action != GLFW_REPEAT && action != GLFW_RELEASE) {
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
      default -> {
      }
    }
  }

  void clearCameraInputs() {
    rotateLeftPressed = false;
    rotateRightPressed = false;
    pitchUpPressed = false;
    pitchDownPressed = false;
    applyCameraInputs();
  }

  private void applyCameraInputs() {
    inputPort.setGameplayCameraInputs(rotateLeftPressed, rotateRightPressed, pitchUpPressed, pitchDownPressed);
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
  interface MovementPort {
    void move(int deltaX, int deltaY, MovementMode movementMode);
  }

  private record GameplayInputPort(
      GameplayClickPort clickPort,
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
  }
}
