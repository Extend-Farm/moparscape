package com.veyrmoor.client.desktop.platform.window;

public record DesktopWindowConfig(int width, int height, String title) {

  public static DesktopWindowConfig defaults() {
    return new DesktopWindowConfig(765, 503, "Veyrmoor");
  }
}
