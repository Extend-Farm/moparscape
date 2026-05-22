package io.github.ffakira.rsps.client.desktop.core;

import io.github.ffakira.rsps.client.core.GameplayClientSession;
import io.github.ffakira.rsps.client.desktop.login.LoginField;
import io.github.ffakira.rsps.client.desktop.login.LoginScreenController;
import io.github.ffakira.rsps.client.desktop.login.LoginScreenState;
import io.github.ffakira.rsps.client.desktop.login.TitleScreenLayout;
import io.github.ffakira.rsps.client.desktop.login.TitleScreenStage;
import java.util.function.Supplier;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

final class LoginInputHandler implements DesktopInputRouter.LoginInputPort {

  private final OpenGlTileRenderSystem renderSystem;
  private final LoginScreenController loginScreenController;
  private final Supplier<GameplayClientSession> gameplayClientSessionSupplier;
  private final DesktopClientState state;
  private final String welcomeStatus;
  private final String credentialsStatus;
  private final String authenticatingStatus;

  LoginInputHandler(
      OpenGlTileRenderSystem renderSystem,
      LoginScreenController loginScreenController,
      Supplier<GameplayClientSession> gameplayClientSessionSupplier,
      DesktopClientState state,
      String welcomeStatus,
      String credentialsStatus,
      String authenticatingStatus
  ) {
    this.renderSystem = renderSystem;
    this.loginScreenController = loginScreenController;
    this.gameplayClientSessionSupplier = gameplayClientSessionSupplier;
    this.state = state;
    this.welcomeStatus = welcomeStatus;
    this.credentialsStatus = credentialsStatus;
    this.authenticatingStatus = authenticatingStatus;
  }

  @Override
  public void handleCharacter(int codePoint) {
    if (loginScreenController.state().stage() != TitleScreenStage.CREDENTIALS) {
      return;
    }
    loginScreenController.append(codePoint);
    syncLoginScreenState();
  }

  @Override
  public void handleMouseButton(int button, int action, double mouseX, double mouseY) {
    if (button != GLFW_MOUSE_BUTTON_LEFT || action != GLFW_PRESS) {
      return;
    }
    TitleScreenLayout layout = renderSystem.currentTitleScreenLayout();
    LoginScreenState loginScreenState = loginScreenController.state();
    switch (loginScreenState.stage()) {
      case LOADING -> {
      }
      case WELCOME -> {
        if (layout.infoButton().contains(mouseX, mouseY)) {
          loginScreenController.showPrivateServerInfo();
        } else if (layout.playNowButton().contains(mouseX, mouseY)) {
          showCredentials();
        }
      }
      case CREDENTIALS -> {
        if (layout.usernameField().contains(mouseX, mouseY)) {
          loginScreenController.focus(LoginField.USERNAME);
        } else if (layout.passwordField().contains(mouseX, mouseY)) {
          loginScreenController.focus(LoginField.PASSWORD);
        } else if (layout.enterButton().contains(mouseX, mouseY) && loginScreenController.canSubmit()) {
          submitNativeLogin(loginScreenState);
        } else if (layout.cancelButton().contains(mouseX, mouseY)) {
          loginScreenController.clear();
          showWelcome();
        }
      }
      case PRIVATE_SERVER_INFO -> {
        if (layout.exitButton().contains(mouseX, mouseY)) {
          showCredentials();
        }
      }
    }
    syncLoginScreenState();
  }

  @Override
  public void handleKey(long windowHandle, int key, int action) {
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
        showCredentials();
        syncLoginScreenState();
      } else if (key == GLFW_KEY_ESCAPE) {
        glfwSetWindowShouldClose(windowHandle, true);
      }
      return;
    }
    if (loginScreenState.stage() == TitleScreenStage.LOADING) {
      if (key == GLFW_KEY_ESCAPE) {
        glfwSetWindowShouldClose(windowHandle, true);
      }
      return;
    }
    if (loginScreenState.stage() == TitleScreenStage.PRIVATE_SERVER_INFO) {
      if (key == GLFW_KEY_ENTER || key == GLFW_KEY_KP_ENTER || key == GLFW_KEY_ESCAPE) {
        showCredentials();
        syncLoginScreenState();
      }
      return;
    }
    switch (key) {
      case GLFW_KEY_ESCAPE -> {
        if (action == GLFW_PRESS) {
          loginScreenController.clear();
          showWelcome();
          syncLoginScreenState();
        }
      }
      case GLFW_KEY_TAB -> {
        if (action == GLFW_PRESS) {
          loginScreenController.focusNext();
          syncLoginScreenState();
        }
      }
      case GLFW_KEY_BACKSPACE -> {
        loginScreenController.backspace();
        syncLoginScreenState();
      }
      case GLFW_KEY_ENTER, GLFW_KEY_KP_ENTER -> {
        if (action != GLFW_PRESS) {
          return;
        }
        if (loginScreenController.advanceOrSubmitOnEnter()) {
          submitNativeLogin(loginScreenController.state());
        }
        syncLoginScreenState();
      }
      default -> {
      }
    }
  }

  private void showWelcome() {
    loginScreenController.showWelcome();
    state.setTitleScreenStatus(welcomeStatus);
  }

  private void showCredentials() {
    loginScreenController.showCredentials();
    state.setTitleScreenStatus(credentialsStatus);
  }

  private void submitNativeLogin(LoginScreenState loginScreenState) {
    gameplayClientSessionSupplier.get().login(loginScreenState.username(), loginScreenState.password());
    state.setTitleScreenStatus(authenticatingStatus);
  }

  private void syncLoginScreenState() {
    renderSystem.setLoginScreenState(loginScreenController.state());
  }
}
