package io.github.ffakira.rsps.client.desktop.login;

public record LoginScreenState(
    TitleScreenStage stage,
    String username,
    String password,
    LoginField focusedField
) {

  public boolean canSubmit() {
    return !username.isBlank() && !password.isBlank();
  }

  public String maskedPassword() {
    return "*".repeat(password.length());
  }
}
