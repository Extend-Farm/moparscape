package io.github.ffakira.rsps.client.desktop.core;

record ActorAnimationState(float strideWeight, float walkCycleRadians, float headingDegrees) {

  private static final float MIN_STRIDE_WEIGHT = 0.001f;
  private static final ActorAnimationState IDLE = new ActorAnimationState(0.0f, 0.0f, 0.0f);

  ActorAnimationState {
    strideWeight = clamp(strideWeight, 0.0f, 1.0f);
    headingDegrees = normalizeDegrees(headingDegrees);
  }

  static ActorAnimationState idle() {
    return IDLE;
  }

  static ActorAnimationState idle(float headingDegrees) {
    return new ActorAnimationState(0.0f, 0.0f, headingDegrees);
  }

  boolean isIdle() {
    return strideWeight <= MIN_STRIDE_WEIGHT;
  }

  float headingRadians() {
    return (float) Math.toRadians(headingDegrees);
  }

  float forwardX() {
    return (float) Math.sin(headingRadians());
  }

  float forwardY() {
    return (float) Math.cos(headingRadians());
  }

  private static float clamp(float value, float minimum, float maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }

  private static float normalizeDegrees(float degrees) {
    float normalized = degrees % 360.0f;
    if (normalized > 180.0f) {
      normalized -= 360.0f;
    } else if (normalized <= -180.0f) {
      normalized += 360.0f;
    }
    return normalized;
  }
}
