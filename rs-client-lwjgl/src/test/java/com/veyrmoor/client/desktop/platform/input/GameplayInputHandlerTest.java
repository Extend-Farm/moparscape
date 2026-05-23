package com.veyrmoor.client.desktop.platform.input;

import static org.assertj.core.api.Assertions.assertThat;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import com.veyrmoor.client.desktop.gameplay.GameplayChatController;
import com.veyrmoor.client.desktop.gameplay.GameplayClickResult;
import com.veyrmoor.client.desktop.gameplay.GameplayMouseButton;
import com.veyrmoor.model.MovementMode;
import java.util.ArrayList;
import java.util.List;
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

    inputHandler.handleMouseButton(0L, GLFW_MOUSE_BUTTON_LEFT, GLFW_PRESS, 120.0, 80.0);

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
    inputHandler.handleMouseButton(0L, GLFW_MOUSE_BUTTON_LEFT, GLFW_PRESS, 120.0, 80.0);
    inputHandler.handleKey(0L, GLFW_KEY_LEFT_SHIFT, GLFW_RELEASE);

    assertThat(movementPort.lastDeltaX).isEqualTo(2);
    assertThat(movementPort.lastDeltaY).isEqualTo(1);
    assertThat(movementPort.lastMovementMode).isEqualTo(MovementMode.RUN);
  }

  @Test
  void requestsLogoutFromSuccessfulLogoutClicks() {
    TestGameplayClickPort clickPort = new TestGameplayClickPort();
    clickPort.nextResult = GameplayClickResult.logout();
    TestWindowClosePort windowClosePort = new TestWindowClosePort();
    TestLogoutPort logoutPort = new TestLogoutPort();
    GameplayInputHandler inputHandler = new GameplayInputHandler(
        clickPort,
        new TestGameplayCameraInputPort(),
        (x, y, mode) -> {
        },
        windowClosePort,
        logoutPort
    );

    inputHandler.handleMouseButton(42L, GLFW_MOUSE_BUTTON_LEFT, GLFW_PRESS, 120.0, 80.0);

    assertThat(logoutPort.requested).isTrue();
    assertThat(windowClosePort.lastWindowHandle).isEqualTo(-1L);
  }

  @Test
  void routesGameplayScrollToTheScrollPort() {
    TestGameplayScrollPort scrollPort = new TestGameplayScrollPort();
    GameplayInputHandler inputHandler = new GameplayInputHandler(
        (mouseX, mouseY, button) -> GameplayClickResult.handledClick(),
        scrollPort,
        (mouseX, mouseY) -> false,
        () -> {
        },
        new TestGameplayCameraInputPort(),
        (x, y, mode) -> {
        },
        windowHandle -> {
        },
        () -> {
        }
    );

    inputHandler.handleScroll(180.0, 96.0, -1.0);

    assertThat(scrollPort.calls).isEqualTo(1);
    assertThat(scrollPort.lastMouseX).isEqualTo(180.0);
    assertThat(scrollPort.lastMouseY).isEqualTo(96.0);
    assertThat(scrollPort.lastYOffset).isEqualTo(-1.0);
  }

  @Test
  void routesPointerDragByPixelWhileTheLeftButtonIsHeld() {
    TestGameplayPointerMovePort pointerMovePort = new TestGameplayPointerMovePort();
    GameplayInputHandler inputHandler = new GameplayInputHandler(
        (mouseX, mouseY, button) -> GameplayClickResult.handledClick(),
        (mouseX, mouseY, yOffset) -> false,
        pointerMovePort,
        () -> {
        },
        new TestGameplayCameraInputPort(),
        (x, y, mode) -> {
        },
        windowHandle -> {
        },
        () -> {
        }
    );

    inputHandler.handleMouseButton(0L, GLFW_MOUSE_BUTTON_LEFT, GLFW_PRESS, 120.0, 80.0);
    inputHandler.handlePointerMove(146.0, 101.0);
    inputHandler.handleMouseButton(0L, GLFW_MOUSE_BUTTON_LEFT, GLFW_RELEASE, 146.0, 101.0);
    inputHandler.handlePointerMove(160.0, 110.0);

    assertThat(pointerMovePort.calls).isEqualTo(1);
    assertThat(pointerMovePort.lastMouseX).isEqualTo(146.0);
    assertThat(pointerMovePort.lastMouseY).isEqualTo(101.0);
  }

  @Test
  void opensTypingModeAndSubmitsPublicChatFromEnter() {
    GameplayChatController chatController = new GameplayChatController();
    TestPublicChatPort publicChatPort = new TestPublicChatPort();
    GameplayInputHandler inputHandler = new GameplayInputHandler(
        (mouseX, mouseY, button) -> GameplayClickResult.handledClick(),
        new TestGameplayCameraInputPort(),
        (deltaX, deltaY, movementMode) -> {
        },
        publicChatPort,
        chatController,
        windowHandle -> {
        },
        () -> {
        }
    );

    inputHandler.handleKey(0L, GLFW_KEY_ENTER, GLFW_PRESS);
    inputHandler.handleCharacter('h');
    inputHandler.handleCharacter('i');
    inputHandler.handleKey(0L, GLFW_KEY_BACKSPACE, GLFW_PRESS);
    inputHandler.handleCharacter('!');
    inputHandler.handleKey(0L, GLFW_KEY_ENTER, GLFW_PRESS);

    assertThat(publicChatPort.messages).containsExactly("H!");
    assertThat(chatController.isTyping()).isFalse();
    assertThat(chatController.state().draftText()).isEmpty();
  }

  @Test
  void startsTypingOnTheFirstPrintableCharacterWithoutClickingOrPressingEnter() {
    GameplayChatController chatController = new GameplayChatController();
    TestPublicChatPort publicChatPort = new TestPublicChatPort();
    GameplayInputHandler inputHandler = new GameplayInputHandler(
        (mouseX, mouseY, button) -> GameplayClickResult.handledClick(),
        new TestGameplayCameraInputPort(),
        (deltaX, deltaY, movementMode) -> {
        },
        publicChatPort,
        chatController,
        windowHandle -> {
        },
        () -> {
        }
    );

    inputHandler.handleCharacter('h');
    inputHandler.handleCharacter('i');
    inputHandler.handleKey(0L, GLFW_KEY_ENTER, GLFW_PRESS);

    assertThat(publicChatPort.messages).containsExactly("Hi");
    assertThat(chatController.isTyping()).isFalse();
  }

  @Test
  void escapeCancelsTypingWithoutClosingTheWindow() {
    GameplayChatController chatController = new GameplayChatController();
    TestPublicChatPort publicChatPort = new TestPublicChatPort();
    TestWindowClosePort windowClosePort = new TestWindowClosePort();
    GameplayInputHandler inputHandler = new GameplayInputHandler(
        (mouseX, mouseY, button) -> GameplayClickResult.handledClick(),
        new TestGameplayCameraInputPort(),
        (deltaX, deltaY, movementMode) -> {
        },
        publicChatPort,
        chatController,
        windowClosePort,
        () -> {
        }
    );

    inputHandler.handleKey(0L, GLFW_KEY_ENTER, GLFW_PRESS);
    inputHandler.handleCharacter('x');
    inputHandler.handleKey(99L, GLFW_KEY_ESCAPE, GLFW_PRESS);

    assertThat(publicChatPort.messages).isEmpty();
    assertThat(chatController.isTyping()).isFalse();
    assertThat(windowClosePort.lastWindowHandle).isEqualTo(-1L);
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

  private static final class TestGameplayScrollPort implements GameplayInputHandler.GameplayScrollPort {

    private int calls;
    private double lastMouseX;
    private double lastMouseY;
    private double lastYOffset;

    @Override
    public boolean handleGameplayScroll(double mouseX, double mouseY, double yOffset) {
      calls++;
      lastMouseX = mouseX;
      lastMouseY = mouseY;
      lastYOffset = yOffset;
      return true;
    }
  }

  private static final class TestGameplayPointerMovePort implements GameplayInputHandler.GameplayPointerMovePort {

    private int calls;
    private double lastMouseX;
    private double lastMouseY;

    @Override
    public boolean handleGameplayPointerMove(double mouseX, double mouseY) {
      calls++;
      lastMouseX = mouseX;
      lastMouseY = mouseY;
      return true;
    }
  }

  private static final class TestWindowClosePort implements GameplayInputHandler.WindowClosePort {

    private long lastWindowHandle = -1L;

    @Override
    public void requestClose(long windowHandle) {
      lastWindowHandle = windowHandle;
    }
  }

  private static final class TestPublicChatPort implements GameplayInputHandler.PublicChatPort {

    private final List<String> messages = new ArrayList<>();

    @Override
    public void sendPublicChat(String text) {
      messages.add(text);
    }
  }

  private static final class TestLogoutPort implements GameplayInputHandler.LogoutPort {

    private boolean requested;

    @Override
    public void requestLogout() {
      requested = true;
    }
  }
}
