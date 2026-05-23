package com.veyrmoor.client.desktop.login;

public record LoginScreenState(
    TitleScreenStage stage,
    String username,
    String password,
    LoginField focusedField,
    int loadingPercent
) {

  public LoginScreenState(
      TitleScreenStage stage,
      String username,
      String password,
      LoginField focusedField
  ) {
    this(stage, username, password, focusedField, 0);
  }

  public boolean canSubmit() {
    return !username.isBlank() && !password.isBlank();
  }

  public String maskedPassword() {
    return "*".repeat(password.length());
  }
}
