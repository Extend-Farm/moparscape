package io.github.ffakira.rsps.client.desktop.character;

public record ActorAnimationState(
    float strideWeight,
    float walkCycleRadians,
    float headingDegrees,
    float positionOffsetX,
    float positionOffsetY
) {

  private static final float TAU = (float) (Math.PI * 2.0);
  private static final int WALK_FRAME_COUNT = 4;
  private static final float WALK_FRAME_EPSILON = 0.0001f;
  private static final float MIN_STRIDE_WEIGHT = 0.001f;
  private static final ActorAnimationState IDLE = new ActorAnimationState(0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

  public ActorAnimationState(float strideWeight, float walkCycleRadians, float headingDegrees) {
    this(strideWeight, walkCycleRadians, headingDegrees, 0.0f, 0.0f);
  }

  public ActorAnimationState {
    strideWeight = clamp(strideWeight, 0.0f, 1.0f);
    walkCycleRadians = normalizeRadians(walkCycleRadians);
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

  public float walkCycleNormalized() {
    return walkCycleRadians / TAU;
  }

  public int walkFrameIndex() {
    float framePosition = walkCycleNormalized() * WALK_FRAME_COUNT;
    int frameIndex = (int) Math.floor(framePosition + WALK_FRAME_EPSILON);
    return Math.floorMod(frameIndex, WALK_FRAME_COUNT);
  }

  public float walkFrameProgress() {
    float framePosition = walkCycleNormalized() * WALK_FRAME_COUNT;
    float progressedFrames = (float) Math.floor(framePosition + WALK_FRAME_EPSILON);
    float progress = framePosition - progressedFrames;
    if (Math.abs(progress) <= WALK_FRAME_EPSILON || 1.0f - progress <= WALK_FRAME_EPSILON) {
      return 0.0f;
    }
    return progress;
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

  private static float normalizeRadians(float radians) {
    float normalized = radians % TAU;
    return normalized < 0.0f ? normalized + TAU : normalized;
  }
}
