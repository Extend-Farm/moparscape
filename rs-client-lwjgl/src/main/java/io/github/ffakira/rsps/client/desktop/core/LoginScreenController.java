package io.github.ffakira.rsps.client.desktop.core;

public final class LoginScreenController {

  private static final int MAX_USERNAME_LENGTH = 12;
  private static final int MAX_PASSWORD_LENGTH = 20;

  private TitleScreenStage stage = TitleScreenStage.WELCOME;
  private String username = "";
  private String password = "";
  private LoginField focusedField = LoginField.USERNAME;

  public LoginScreenState state() {
    return new LoginScreenState(stage, username, password, focusedField);
  }

  public void focus(LoginField loginField) {
    focusedField = loginField;
  }

  public void focusNext() {
    focusedField = focusedField.next();
  }

  public boolean advanceOrSubmitOnEnter() {
    if (focusedField == LoginField.USERNAME) {
      focusedField = LoginField.PASSWORD;
      return false;
    }
    return canSubmit();
  }

  public void append(int codePoint) {
    if (codePoint < 32 || codePoint > 126) {
      return;
    }
    if (focusedField == LoginField.USERNAME) {
      if (username.length() >= MAX_USERNAME_LENGTH) {
        return;
      }
      username += String.valueOf((char) codePoint);
      return;
    }
    if (password.length() >= MAX_PASSWORD_LENGTH) {
      return;
    }
    password += String.valueOf((char) codePoint);
  }

  public void backspace() {
    if (focusedField == LoginField.USERNAME) {
      username = trimLast(username);
      return;
    }
    password = trimLast(password);
  }

  public void clear() {
    username = "";
    password = "";
    focusedField = LoginField.USERNAME;
  }

  public void showWelcome() {
    stage = TitleScreenStage.WELCOME;
  }

  public void showCredentials() {
    stage = TitleScreenStage.CREDENTIALS;
    focusedField = LoginField.USERNAME;
  }

  public void showPrivateServerInfo() {
    stage = TitleScreenStage.PRIVATE_SERVER_INFO;
  }

  public boolean canSubmit() {
    return !username.isBlank() && !password.isBlank();
  }

  private String trimLast(String value) {
    if (value.isEmpty()) {
      return value;
    }
    return value.substring(0, value.length() - 1);
  }
}
