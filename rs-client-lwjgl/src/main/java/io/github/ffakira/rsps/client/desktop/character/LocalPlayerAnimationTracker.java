package io.github.ffakira.rsps.client.desktop.character;

import io.github.ffakira.rsps.model.WorldPoint;
import java.util.Objects;
import java.util.function.LongSupplier;

public final class LocalPlayerAnimationTracker {

  private static final long MOVEMENT_POSE_TAIL_NANOS = 220_000_000L;
  private static final float BASE_WALK_CYCLES_PER_SECOND = 1.8f;
  private static final float EXTRA_WALK_CYCLES_PER_SECOND = 1.1f;
  private static final float STRIDE_BLEND_PER_SECOND = 8.0f;
  private static final float HEADING_TURN_DEGREES_PER_SECOND = 320.0f;
  private static final float MOVEMENT_TILES_PER_SECOND = 8.0f;
  private static final float MAX_STEP_DISTANCE = 1.5f;
  private static final float MAX_RENDER_LAG = 2.0f;
  private static final float POSITION_EPSILON = 0.01f;
  private static final float TURNING_STRIDE_SUPPRESSION = 0.45f;
  private static final float TAU = (float) (Math.PI * 2.0);

  private final LongSupplier nanoClock;
  private WorldPoint lastWorldPoint;
  private long lastUpdateNanos = Long.MIN_VALUE;
  private long lastMovementNanos = Long.MIN_VALUE;
  private float displayedWorldX;
  private float displayedWorldY;
  private float targetHeadingDegrees;
  private float displayedHeadingDegrees;
  private float lastStepDistance;
  private float walkCycleRadians;
  private float strideWeight;

  public LocalPlayerAnimationTracker() {
    this(System::nanoTime);
  }

  public LocalPlayerAnimationTracker(LongSupplier nanoClock) {
    this.nanoClock = Objects.requireNonNull(nanoClock, "nanoClock");
  }

  public ActorAnimationState update(WorldPoint worldPoint) {
    long now = nanoClock.getAsLong();
    if (worldPoint == null) {
      reset();
      return ActorAnimationState.idle();
    }
    if (lastWorldPoint == null) {
      snapDisplayedPosition(worldPoint);
      lastWorldPoint = worldPoint;
      lastUpdateNanos = now;
      return ActorAnimationState.idle();
    }
    float elapsedSeconds = elapsedSeconds(now);
    if (hasMoved(lastWorldPoint, worldPoint)) {
      int deltaX = worldPoint.x() - lastWorldPoint.x();
      int deltaY = worldPoint.y() - lastWorldPoint.y();
      if (requiresPositionSnap(lastWorldPoint, worldPoint)) {
        snapDisplayedPosition(lastWorldPoint);
      }
      targetHeadingDegrees = headingDegrees(deltaX, deltaY);
      if (lastMovementNanos == Long.MIN_VALUE) {
        displayedHeadingDegrees = targetHeadingDegrees;
      }
      lastStepDistance = Math.min(MAX_STEP_DISTANCE, (float) Math.hypot(deltaX, deltaY));
      lastMovementNanos = now;
    }
    displayedHeadingDegrees = approachAngle(
        displayedHeadingDegrees,
        targetHeadingDegrees,
        HEADING_TURN_DEGREES_PER_SECOND * elapsedSeconds
    );
    moveDisplayedPositionToward(worldPoint, elapsedSeconds);
    lastWorldPoint = worldPoint;
    lastUpdateNanos = now;
    if (lastMovementNanos == Long.MIN_VALUE) {
      return ActorAnimationState.idle(displayedHeadingDegrees);
    }
    float distanceRemaining = (float) Math.hypot(worldPoint.x() - displayedWorldX, worldPoint.y() - displayedWorldY);
    boolean stillInterpolating = distanceRemaining > POSITION_EPSILON;
    long elapsedNanos = now - lastMovementNanos;
    float normalizedTail = elapsedNanos >= MOVEMENT_POSE_TAIL_NANOS
        ? 0.0f
        : 1.0f - elapsedNanos / (float) MOVEMENT_POSE_TAIL_NANOS;
    float stepWeight = clamp(lastStepDistance / MAX_STEP_DISTANCE, 0.48f, 1.0f);
    float turningWeight = 1.0f - clamp(Math.abs(normalizeDegrees(targetHeadingDegrees - displayedHeadingDegrees)) / 180.0f, 0.0f, 1.0f)
        * TURNING_STRIDE_SUPPRESSION;
    float targetStrideWeight = (stillInterpolating ? 1.0f : smoothstep(normalizedTail)) * stepWeight * turningWeight;
    strideWeight = approach(strideWeight, targetStrideWeight, STRIDE_BLEND_PER_SECOND * elapsedSeconds);
    if (strideWeight <= 0.001f && normalizedTail <= 0.0f) {
      strideWeight = 0.0f;
      return ActorAnimationState.idle(displayedHeadingDegrees);
    }
    float cadence = BASE_WALK_CYCLES_PER_SECOND + EXTRA_WALK_CYCLES_PER_SECOND * stepWeight;
    walkCycleRadians = wrapRadians(walkCycleRadians + elapsedSeconds * cadence * TAU * Math.max(0.20f, strideWeight));
    return new ActorAnimationState(
        strideWeight,
        walkCycleRadians,
        displayedHeadingDegrees,
        displayedWorldX - worldPoint.x(),
        displayedWorldY - worldPoint.y()
    );
  }

  public void reset() {
    lastWorldPoint = null;
    lastUpdateNanos = Long.MIN_VALUE;
    lastMovementNanos = Long.MIN_VALUE;
    displayedWorldX = 0.0f;
    displayedWorldY = 0.0f;
    targetHeadingDegrees = 0.0f;
    displayedHeadingDegrees = 0.0f;
    lastStepDistance = 0.0f;
    walkCycleRadians = 0.0f;
    strideWeight = 0.0f;
  }

  private boolean hasMoved(WorldPoint previousWorldPoint, WorldPoint currentWorldPoint) {
    return previousWorldPoint.x() != currentWorldPoint.x()
        || previousWorldPoint.y() != currentWorldPoint.y()
        || previousWorldPoint.plane() != currentWorldPoint.plane();
  }

  private boolean requiresPositionSnap(WorldPoint previousWorldPoint, WorldPoint currentWorldPoint) {
    if (previousWorldPoint.plane() != currentWorldPoint.plane()) {
      return true;
    }
    return Math.hypot(currentWorldPoint.x() - previousWorldPoint.x(), currentWorldPoint.y() - previousWorldPoint.y()) > MAX_RENDER_LAG;
  }

  private void moveDisplayedPositionToward(WorldPoint worldPoint, float elapsedSeconds) {
    if (elapsedSeconds <= 0.0f) {
      return;
    }
    float targetWorldX = worldPoint.x();
    float targetWorldY = worldPoint.y();
    float remainingX = targetWorldX - displayedWorldX;
    float remainingY = targetWorldY - displayedWorldY;
    float remainingDistance = (float) Math.hypot(remainingX, remainingY);
    if (remainingDistance <= POSITION_EPSILON) {
      displayedWorldX = targetWorldX;
      displayedWorldY = targetWorldY;
      return;
    }
    float stepRateScale = clamp(lastStepDistance / MAX_STEP_DISTANCE, 0.85f, 1.0f);
    float maximumTravel = MOVEMENT_TILES_PER_SECOND * stepRateScale * elapsedSeconds;
    if (remainingDistance <= maximumTravel) {
      displayedWorldX = targetWorldX;
      displayedWorldY = targetWorldY;
      return;
    }
    float travelScale = maximumTravel / remainingDistance;
    displayedWorldX += remainingX * travelScale;
    displayedWorldY += remainingY * travelScale;
  }

  private void snapDisplayedPosition(WorldPoint worldPoint) {
    displayedWorldX = worldPoint.x();
    displayedWorldY = worldPoint.y();
  }

  private float smoothstep(float value) {
    float clamped = clamp(value, 0.0f, 1.0f);
    return clamped * clamped * (3.0f - 2.0f * clamped);
  }

  private float elapsedSeconds(long now) {
    if (lastUpdateNanos == Long.MIN_VALUE) {
      return 0.0f;
    }
    return clamp((now - lastUpdateNanos) / 1_000_000_000.0f, 0.0f, 0.25f);
  }

  private float approach(float current, float target, float maximumDelta) {
    if (maximumDelta <= 0.0f) {
      return current;
    }
    if (current < target) {
      return Math.min(target, current + maximumDelta);
    }
    return Math.max(target, current - maximumDelta);
  }

  private float approachAngle(float current, float target, float maximumDeltaDegrees) {
    float delta = normalizeDegrees(target - current);
    if (Math.abs(delta) <= maximumDeltaDegrees) {
      return normalizeDegrees(target);
    }
    return normalizeDegrees(current + Math.copySign(maximumDeltaDegrees, delta));
  }

  private float headingDegrees(int deltaX, int deltaY) {
    return (float) Math.toDegrees(Math.atan2(deltaX, deltaY));
  }

  private float wrapRadians(float radians) {
    float wrapped = radians % TAU;
    return wrapped < 0.0f ? wrapped + TAU : wrapped;
  }

  private float normalizeDegrees(float degrees) {
    float normalized = degrees % 360.0f;
    if (normalized > 180.0f) {
      normalized -= 360.0f;
    } else if (normalized <= -180.0f) {
      normalized += 360.0f;
    }
    return normalized;
  }

  private float clamp(float value, float minimum, float maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }
}
