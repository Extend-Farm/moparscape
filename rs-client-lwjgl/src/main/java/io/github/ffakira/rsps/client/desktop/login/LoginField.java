package io.github.ffakira.rsps.client.desktop.login;

public enum LoginField {
  USERNAME,
  PASSWORD;

  public LoginField next() {
    return switch (this) {
      case USERNAME -> PASSWORD;
      case PASSWORD -> USERNAME;
    };
  }
}
