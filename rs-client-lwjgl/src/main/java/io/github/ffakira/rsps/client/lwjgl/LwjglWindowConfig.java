package io.github.ffakira.rsps.client.lwjgl;

public record LwjglWindowConfig(int width, int height, String title) {

  public static LwjglWindowConfig defaults() {
    return new LwjglWindowConfig(765, 503, "MoparScape");
  }
}
