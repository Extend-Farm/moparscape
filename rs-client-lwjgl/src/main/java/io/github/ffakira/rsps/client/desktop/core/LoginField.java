package io.github.ffakira.rsps.client.desktop.core;

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
