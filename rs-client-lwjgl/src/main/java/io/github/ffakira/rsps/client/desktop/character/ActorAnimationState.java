package io.github.ffakira.rsps.client.desktop.character;

public record ActorAnimationState(
    float strideWeight,
    float walkCycleRadians,
    float headingDegrees,
    float positionOffsetX,
    float positionOffsetY
) {

  private static final float MIN_STRIDE_WEIGHT = 0.001f;
  private static final ActorAnimationState IDLE = new ActorAnimationState(0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

  public ActorAnimationState(float strideWeight, float walkCycleRadians, float headingDegrees) {
    this(strideWeight, walkCycleRadians, headingDegrees, 0.0f, 0.0f);
  }

  public ActorAnimationState {
    strideWeight = clamp(strideWeight, 0.0f, 1.0f);
    headingDegrees = normalizeDegrees(headingDegrees);
    positionOffsetX = clamp(positionOffsetX, -1.5f, 1.5f);
    positionOffsetY = clamp(positionOffsetY, -1.5f, 1.5f);
  }

  public static ActorAnimationState idle() {
    return IDLE;
  }

  public static ActorAnimationState idle(float headingDegrees) {
    return new ActorAnimationState(0.0f, 0.0f, headingDegrees, 0.0f, 0.0f);
  }

  public boolean isIdle() {
    return strideWeight <= MIN_STRIDE_WEIGHT;
  }

  public float headingRadians() {
    return (float) Math.toRadians(headingDegrees);
  }

  public float forwardX() {
    return (float) Math.sin(headingRadians());
  }

  public float forwardY() {
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
