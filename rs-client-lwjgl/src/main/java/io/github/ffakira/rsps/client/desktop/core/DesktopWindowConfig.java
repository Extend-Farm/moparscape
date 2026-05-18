package io.github.ffakira.rsps.client.desktop.core;

public record DesktopWindowConfig(int width, int height, String title) {

  public static DesktopWindowConfig defaults() {
    return new DesktopWindowConfig(765, 503, "MoparScape");
  }
}
