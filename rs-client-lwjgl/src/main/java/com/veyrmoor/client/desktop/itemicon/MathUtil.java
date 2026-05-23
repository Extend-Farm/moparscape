package com.veyrmoor.client.desktop.itemicon;

final class MathUtil {

  private MathUtil() {
  }

  static int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }

  static float clamp(float value, float minimum, float maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }

  static float lerp(float from, float to, float interpolation) {
    return from + (to - from) * interpolation;
  }
}
