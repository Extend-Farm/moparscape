package com.veyrmoor.client.desktop.app;

public final class DesktopClientMain {

  private DesktopClientMain() {
  }

  public static void main(String[] args) {
    try (DesktopClientApplication application = new DesktopClientApplication()) {
      application.run();
    }
  }
}
