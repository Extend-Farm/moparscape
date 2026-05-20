package io.github.ffakira.rsps.client.desktop.character;

public record ActorAnimationState(
    float strideWeight,
    float walkCycleRadians,
    float headingDegrees,
    float travelHeadingDegrees,
    LocomotionMode locomotionMode,
    int movementSequenceId,
    int movementFrameId,
    int actionSequenceId,
    int actionFrameId,
    float positionOffsetX,
    float positionOffsetY
) {

  private static final float TAU = (float) (Math.PI * 2.0);
  private static final int WALK_FRAME_COUNT = 4;
  private static final float WALK_FRAME_EPSILON = 0.0001f;
  private static final float MIN_STRIDE_WEIGHT = 0.001f;
  private static final ActorAnimationState IDLE = new ActorAnimationState(
      0.0f,
      0.0f,
      0.0f,
      0.0f,
      LocomotionMode.IDLE,
      -1,
      -1,
      -1,
      -1,
      0.0f,
      0.0f
  );

  public ActorAnimationState(float strideWeight, float walkCycleRadians, float headingDegrees) {
    this(
        strideWeight,
        walkCycleRadians,
        headingDegrees,
        headingDegrees,
        strideWeight <= MIN_STRIDE_WEIGHT ? LocomotionMode.IDLE : LocomotionMode.WALK_FORWARD,
        -1,
        -1,
        -1,
        -1,
        0.0f,
        0.0f
    );
  }

  public ActorAnimationState(float strideWeight, float walkCycleRadians, float headingDegrees, float positionOffsetX, float positionOffsetY) {
    this(
        strideWeight,
        walkCycleRadians,
        headingDegrees,
        headingDegrees,
        strideWeight <= MIN_STRIDE_WEIGHT ? LocomotionMode.IDLE : LocomotionMode.WALK_FORWARD,
        -1,
        -1,
        -1,
        -1,
        positionOffsetX,
        positionOffsetY
    );
  }

  public ActorAnimationState(
      float strideWeight,
      float walkCycleRadians,
      float headingDegrees,
      float travelHeadingDegrees,
      float positionOffsetX,
      float positionOffsetY
  ) {
    this(
        strideWeight,
        walkCycleRadians,
        headingDegrees,
        travelHeadingDegrees,
        strideWeight <= MIN_STRIDE_WEIGHT ? LocomotionMode.IDLE : LocomotionMode.WALK_FORWARD,
        -1,
        -1,
        -1,
        -1,
        positionOffsetX,
        positionOffsetY
    );
  }

  public ActorAnimationState(
      float strideWeight,
      float walkCycleRadians,
      float headingDegrees,
      float travelHeadingDegrees,
      LocomotionMode locomotionMode,
      int movementSequenceId,
      int movementFrameId,
      float positionOffsetX,
      float positionOffsetY
  ) {
    this(
        strideWeight,
        walkCycleRadians,
        headingDegrees,
        travelHeadingDegrees,
        locomotionMode,
        movementSequenceId,
        movementFrameId,
        -1,
        -1,
        positionOffsetX,
        positionOffsetY
    );
  }

  public ActorAnimationState {
    strideWeight = clamp(strideWeight, 0.0f, 1.0f);
    walkCycleRadians = normalizeRadians(walkCycleRadians);
    headingDegrees = normalizeDegrees(headingDegrees);
    travelHeadingDegrees = normalizeDegrees(travelHeadingDegrees);
    locomotionMode = locomotionMode == null ? LocomotionMode.IDLE : locomotionMode;
    movementSequenceId = Math.max(-1, movementSequenceId);
    movementFrameId = Math.max(-1, movementFrameId);
    actionSequenceId = Math.max(-1, actionSequenceId);
    actionFrameId = Math.max(-1, actionFrameId);
    positionOffsetX = clamp(positionOffsetX, -1.5f, 1.5f);
    positionOffsetY = clamp(positionOffsetY, -1.5f, 1.5f);
  }

  public static ActorAnimationState idle() {
    return IDLE;
  }

  public static ActorAnimationState idle(float headingDegrees) {
    return idle(headingDegrees, -1, -1);
  }

  public static ActorAnimationState idle(float headingDegrees, int activeSequenceId) {
    return idle(headingDegrees, activeSequenceId, -1);
  }

  public static ActorAnimationState idle(float headingDegrees, int activeSequenceId, int activeFrameId) {
    return new ActorAnimationState(
        0.0f,
        0.0f,
        headingDegrees,
        headingDegrees,
        LocomotionMode.IDLE,
        activeSequenceId,
        activeFrameId,
        -1,
        -1,
        0.0f,
        0.0f
    );
  }

  public boolean isIdle() {
    return strideWeight <= MIN_STRIDE_WEIGHT || locomotionMode == LocomotionMode.IDLE;
  }

  public int activeSequenceId() {
    return hasActionFrame() ? actionSequenceId : movementSequenceId;
  }

  public int activeFrameId() {
    return hasActionFrame() ? actionFrameId : movementFrameId;
  }

  public boolean hasMovementFrame() {
    return movementFrameId >= 0;
  }

  public boolean hasActionFrame() {
    return actionFrameId >= 0;
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

  public float relativeTravelDegrees() {
    return normalizeDegrees(travelHeadingDegrees - headingDegrees);
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

  public enum LocomotionMode {
    IDLE,
    WALK_FORWARD,
    WALK_BACKWARD,
    STRAFE_LEFT,
    STRAFE_RIGHT,
    RUN_FORWARD
  }
}
