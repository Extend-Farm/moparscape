package com.veyrmoor.client.desktop.platform.input;

import static org.assertj.core.api.Assertions.assertThat;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import com.veyrmoor.client.desktop.app.DesktopClientState;
import com.veyrmoor.client.desktop.gameplay.GameplayChatController;
import com.veyrmoor.client.desktop.gameplay.GameplayClickResult;
import com.veyrmoor.client.desktop.gameplay.GameplayMouseButton;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

class DesktopInputRouterTest {

  @Test
  void routesGameplayKeysToTheGameplayHandlerWhenGameplayIsActive() {
    AtomicBoolean gameplayActive = new AtomicBoolean(true);
    TestGameplayCameraInputPort cameraInputPort = new TestGameplayCameraInputPort();
    GameplayInputHandler gameplayInputHandler = new GameplayInputHandler(
        (mouseX, mouseY, button) -> GameplayClickResult.handledClick(),
        cameraInputPort,
        (deltaX, deltaY, movementMode) -> {
        }
    );
    TestLoginInputPort loginInputPort = new TestLoginInputPort();
    DesktopInputRouter inputRouter = new DesktopInputRouter(
        0L,
        null,
        new DesktopClientState("status"),
        loginInputPort,
        gameplayInputHandler,
        gameplayActive::get
    );

    inputRouter.routeKey(0L, GLFW_KEY_LEFT, GLFW_PRESS);

    assertThat(cameraInputPort.lastInputs).containsExactly(true, false, false, false);
    assertThat(loginInputPort.keyCalls).isZero();
  }

  @Test
  void clearsHeldGameplayCameraInputsBeforeRoutingKeysBackToLogin() {
    AtomicBoolean gameplayActive = new AtomicBoolean(true);
    TestGameplayCameraInputPort cameraInputPort = new TestGameplayCameraInputPort();
    GameplayInputHandler gameplayInputHandler = new GameplayInputHandler(
        (mouseX, mouseY, button) -> GameplayClickResult.handledClick(),
        cameraInputPort,
        (deltaX, deltaY, movementMode) -> {
        }
    );
    TestLoginInputPort loginInputPort = new TestLoginInputPort();
    DesktopInputRouter inputRouter = new DesktopInputRouter(
        0L,
        null,
        new DesktopClientState("status"),
        loginInputPort,
        gameplayInputHandler,
        gameplayActive::get
    );

    inputRouter.routeKey(0L, GLFW_KEY_LEFT, GLFW_PRESS);
    gameplayActive.set(false);
    inputRouter.routeKey(0L, GLFW_KEY_TAB, GLFW_PRESS);

    assertThat(cameraInputPort.lastInputs).containsExactly(false, false, false, false);
    assertThat(loginInputPort.keyCalls).isEqualTo(1);
    assertThat(loginInputPort.lastKey).isEqualTo(GLFW_KEY_TAB);
  }

  @Test
  void routesGameplayScrollToTheGameplayHandlerWhenGameplayIsActive() {
    AtomicBoolean gameplayActive = new AtomicBoolean(true);
    DesktopClientState state = new DesktopClientState("status");
    state.setMousePosition(140.0, 88.0);
    TestGameplayCameraInputPort cameraInputPort = new TestGameplayCameraInputPort();
    TestGameplayScrollPort scrollPort = new TestGameplayScrollPort();
    GameplayInputHandler gameplayInputHandler = new GameplayInputHandler(
        (mouseX, mouseY, button) -> GameplayClickResult.handledClick(),
        scrollPort,
        (mouseX, mouseY) -> false,
        () -> {
        },
        cameraInputPort,
        (deltaX, deltaY, movementMode) -> {
        },
        windowHandle -> {
        },
        () -> {
        }
    );
    DesktopInputRouter inputRouter = new DesktopInputRouter(
        0L,
        null,
        state,
        new TestLoginInputPort(),
        gameplayInputHandler,
        gameplayActive::get
    );

    inputRouter.routeScroll(-1.0);

    assertThat(scrollPort.calls).isEqualTo(1);
    assertThat(scrollPort.lastMouseX).isEqualTo(140.0);
    assertThat(scrollPort.lastMouseY).isEqualTo(88.0);
    assertThat(scrollPort.lastYOffset).isEqualTo(-1.0);
  }

  @Test
  void routesGameplayCharactersToTheGameplayHandlerWhenGameplayIsActive() {
    AtomicBoolean gameplayActive = new AtomicBoolean(true);
    GameplayChatController chatController = new GameplayChatController();
    TestLoginInputPort loginInputPort = new TestLoginInputPort();
    GameplayInputHandler gameplayInputHandler = new GameplayInputHandler(
        (mouseX, mouseY, button) -> GameplayClickResult.handledClick(),
        new TestGameplayCameraInputPort(),
        (deltaX, deltaY, movementMode) -> {
        },
        text -> {
        },
        chatController,
        windowHandle -> {
        },
        () -> {
        }
    );
    DesktopInputRouter inputRouter = new DesktopInputRouter(
        0L,
        null,
        new DesktopClientState("status"),
        loginInputPort,
        gameplayInputHandler,
        gameplayActive::get
    );

    inputRouter.routeKey(0L, GLFW_KEY_ENTER, GLFW_PRESS);
    inputRouter.routeCharacter('h');

    assertThat(chatController.state().draftText()).isEqualTo("h");
    assertThat(loginInputPort.characterCalls).isZero();
  }

  @Test
  void repeatsHeldGameplayCharactersOnTheFrameClock() {
    AtomicBoolean gameplayActive = new AtomicBoolean(true);
    GameplayChatController chatController = new GameplayChatController();
    DesktopInputRouter inputRouter = new DesktopInputRouter(
        0L,
        null,
        new DesktopClientState("status"),
        new TestLoginInputPort(),
        new GameplayInputHandler(
            (mouseX, mouseY, button) -> GameplayClickResult.handledClick(),
            new TestGameplayCameraInputPort(),
            (deltaX, deltaY, movementMode) -> {
            },
            text -> {
            },
            chatController,
            windowHandle -> {
            },
            () -> {
            }
        ),
        gameplayActive::get
    );

    inputRouter.routeKey(0L, GLFW_KEY_A, 0, GLFW_PRESS, 0);
    inputRouter.routeCharacter('a');

    long firstRepeatAt = System.nanoTime()
        + DesktopInputRouter.GAMEPLAY_CHAT_REPEAT_INITIAL_DELAY_NANOS
        + DesktopInputRouter.GAMEPLAY_CHAT_REPEAT_INTERVAL_NANOS;
    inputRouter.advanceFrame(firstRepeatAt);
    inputRouter.routeCharacter('a');
    inputRouter.advanceFrame(firstRepeatAt + DesktopInputRouter.GAMEPLAY_CHAT_REPEAT_INTERVAL_NANOS);

    assertThat(chatController.state().draftText()).isEqualTo("aaa");

    inputRouter.routeKey(0L, GLFW_KEY_A, 0, GLFW_RELEASE, 0);
    inputRouter.advanceFrame(firstRepeatAt + DesktopInputRouter.GAMEPLAY_CHAT_REPEAT_INTERVAL_NANOS * 4);

    assertThat(chatController.state().draftText()).isEqualTo("aaa");
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

  private static final class TestLoginInputPort implements DesktopInputRouter.LoginInputPort {

    private int characterCalls;
    private int keyCalls;
    private int lastKey = Integer.MIN_VALUE;

    @Override
    public void handleCharacter(int codePoint) {
      characterCalls++;
    }

    @Override
    public void handleMouseButton(int button, int action, double mouseX, double mouseY) {
    }

    @Override
    public void handleKey(long windowHandle, int key, int action) {
      keyCalls++;
      lastKey = key;
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
}
