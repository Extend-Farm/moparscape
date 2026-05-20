package io.github.ffakira.rsps.client.desktop.core;

import io.github.ffakira.rsps.client.core.GameplayClientSession;
import io.github.ffakira.rsps.client.desktop.gameplay.GameplayClickResult;
import io.github.ffakira.rsps.client.desktop.gameplay.GameplayMouseButton;
import io.github.ffakira.rsps.model.MovementMode;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

final class GameplayInputHandler {

  private static final float CAMERA_YAW_STEP_DEGREES = 8.0f;
  private static final float CAMERA_PITCH_STEP_DEGREES = 1.0f;

  private final OpenGlTileRenderSystem renderSystem;
  private final GameplayClientSession gameplayClientSession;

  GameplayInputHandler(OpenGlTileRenderSystem renderSystem, GameplayClientSession gameplayClientSession) {
    this.renderSystem = renderSystem;
    this.gameplayClientSession = gameplayClientSession;
  }

  void handleMouseButton(int button, int action, double mouseX, double mouseY) {
    GameplayMouseButton gameplayMouseButton = gameplayMouseButton(button);
    if (gameplayMouseButton == null || action != GLFW_PRESS) {
      return;
    }
    GameplayClickResult clickResult = renderSystem.handleGameplayClick(mouseX, mouseY, gameplayMouseButton);
    if (!clickResult.hasMovementDelta()) {
      return;
    }
    GameplayClickResult.MovementDelta movementDelta = clickResult.movementDelta();
    gameplayClientSession.move(movementDelta.deltaX(), movementDelta.deltaY(), MovementMode.WALK);
  }

  void handleKey(long windowHandle, int key, int action) {
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

  private static GameplayMouseButton gameplayMouseButton(int button) {
    return switch (button) {
      case GLFW_MOUSE_BUTTON_LEFT -> GameplayMouseButton.LEFT;
      case GLFW_MOUSE_BUTTON_RIGHT -> GameplayMouseButton.RIGHT;
      default -> null;
    };
  }
}
