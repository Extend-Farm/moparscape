package io.github.ffakira.rsps.client.desktop.core;

public final class DesktopClientMain {

  private DesktopClientMain() {
  }

  public static void main(String[] args) {
    try (DesktopClientApplication application = new DesktopClientApplication()) {
      application.run();
    }
  }
}
