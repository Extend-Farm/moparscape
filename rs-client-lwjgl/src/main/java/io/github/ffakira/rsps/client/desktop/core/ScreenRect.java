package io.github.ffakira.rsps.client.desktop.core;

public record ScreenRect(float left, float top, float width, float height) {

  public boolean contains(double x, double y) {
    return x >= left && x <= left + width && y >= top && y <= top + height;
  }
}
