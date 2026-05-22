package io.github.ffakira.rsps.client.desktop.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import io.github.ffakira.rsps.client.desktop.gameplay.GameplayClickResult;
import io.github.ffakira.rsps.client.desktop.gameplay.GameplayMouseButton;
import io.github.ffakira.rsps.model.MovementMode;
import org.junit.jupiter.api.Test;

class GameplayInputHandlerTest {

  @Test
  void updatesHeldCameraInputsOnArrowPressAndRelease() {
    TestGameplayClickPort clickPort = new TestGameplayClickPort();
    TestGameplayCameraInputPort cameraInputPort = new TestGameplayCameraInputPort();
    GameplayInputHandler inputHandler = new GameplayInputHandler(clickPort, cameraInputPort, (x, y, mode) -> {
    });

    inputHandler.handleKey(0L, GLFW_KEY_LEFT, GLFW_PRESS);
    assertThat(cameraInputPort.lastInputs).containsExactly(true, false, false, false);

    inputHandler.handleKey(0L, GLFW_KEY_RIGHT, GLFW_PRESS);
    assertThat(cameraInputPort.lastInputs).containsExactly(true, true, false, false);

    inputHandler.handleKey(0L, GLFW_KEY_LEFT, GLFW_RELEASE);
    assertThat(cameraInputPort.lastInputs).containsExactly(false, true, false, false);

    inputHandler.handleKey(0L, GLFW_KEY_UP, GLFW_PRESS);
    assertThat(cameraInputPort.lastInputs).containsExactly(false, true, true, false);
  }

  @Test
  void clearsHeldCameraInputsExplicitly() {
    TestGameplayCameraInputPort cameraInputPort = new TestGameplayCameraInputPort();
    GameplayInputHandler inputHandler = new GameplayInputHandler(
        new TestGameplayClickPort(),
        cameraInputPort,
        (x, y, mode) -> {
        }
    );

    inputHandler.handleKey(0L, GLFW_KEY_LEFT, GLFW_PRESS);
    inputHandler.handleKey(0L, GLFW_KEY_UP, GLFW_PRESS);
    inputHandler.clearCameraInputs();

    assertThat(cameraInputPort.lastInputs).containsExactly(false, false, false, false);
  }

  @Test
  void dispatchesWalkMovementFromSuccessfulViewportClicks() {
    TestGameplayClickPort clickPort = new TestGameplayClickPort();
    clickPort.nextResult = GameplayClickResult.move(3, -2);
    TestMovementPort movementPort = new TestMovementPort();
    GameplayInputHandler inputHandler = new GameplayInputHandler(
        clickPort,
        new TestGameplayCameraInputPort(),
        movementPort
    );

    inputHandler.handleMouseButton(GLFW_MOUSE_BUTTON_LEFT, GLFW_PRESS, 120.0, 80.0);

    assertThat(clickPort.lastButton).isEqualTo(GameplayMouseButton.LEFT);
    assertThat(movementPort.lastDeltaX).isEqualTo(3);
    assertThat(movementPort.lastDeltaY).isEqualTo(-2);
    assertThat(movementPort.lastMovementMode).isEqualTo(MovementMode.WALK);
  }

  @Test
  void dispatchesRunMovementWhenShiftIsHeldDuringViewportClicks() {
    TestGameplayClickPort clickPort = new TestGameplayClickPort();
    clickPort.nextResult = GameplayClickResult.move(2, 1);
    TestMovementPort movementPort = new TestMovementPort();
    GameplayInputHandler inputHandler = new GameplayInputHandler(
        clickPort,
        new TestGameplayCameraInputPort(),
        movementPort
    );

    inputHandler.handleKey(0L, GLFW_KEY_LEFT_SHIFT, GLFW_PRESS);
    inputHandler.handleMouseButton(GLFW_MOUSE_BUTTON_LEFT, GLFW_PRESS, 120.0, 80.0);
    inputHandler.handleKey(0L, GLFW_KEY_LEFT_SHIFT, GLFW_RELEASE);

    assertThat(movementPort.lastDeltaX).isEqualTo(2);
    assertThat(movementPort.lastDeltaY).isEqualTo(1);
    assertThat(movementPort.lastMovementMode).isEqualTo(MovementMode.RUN);
  }

  private static final class TestGameplayClickPort implements GameplayInputHandler.GameplayClickPort {

    private GameplayClickResult nextResult = GameplayClickResult.handledClick();
    private GameplayMouseButton lastButton;

    @Override
    public GameplayClickResult handleGameplayClick(double mouseX, double mouseY, GameplayMouseButton button) {
      lastButton = button;
      return nextResult;
    }
  }

  private static final class TestGameplayCameraInputPort implements GameplayInputHandler.GameplayCameraInputPort {

    private boolean[] lastInputs = new boolean[4];

    @Override
    public void setGameplayCameraInputs(
        boolean rotateLeftPressed,
        boolean rotateRightPressed,
        boolean pitchUpPressed,
        boolean pitchDownPressed
    ) {
      lastInputs = new boolean[]{rotateLeftPressed, rotateRightPressed, pitchUpPressed, pitchDownPressed};
    }
  }

  private static final class TestMovementPort implements GameplayInputHandler.MovementPort {

    private int lastDeltaX;
    private int lastDeltaY;
    private MovementMode lastMovementMode;

    @Override
    public void move(int deltaX, int deltaY, MovementMode movementMode) {
      lastDeltaX = deltaX;
      lastDeltaY = deltaY;
      lastMovementMode = movementMode;
    }
  }
}
