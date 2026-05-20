package io.github.ffakira.rsps.client.desktop.gameplay;

import java.util.Objects;
import java.util.function.LongSupplier;

public final class GameplayCameraController {

  static final float DEFAULT_YAW_OFFSET_DEGREES = 45.0f;
  static final float DEFAULT_PITCH_OFFSET_DEGREES = 3.0f;
  static final float MIN_PITCH_OFFSET_DEGREES = -6.0f;
  static final float MAX_PITCH_OFFSET_DEGREES = 8.0f;

  private static final float YAW_SMOOTH_DEGREES_PER_SECOND = 240.0f;
  private static final float PITCH_SMOOTH_DEGREES_PER_SECOND = 90.0f;

  private final LongSupplier nanoClock;
  private float currentYawOffsetDegrees = DEFAULT_YAW_OFFSET_DEGREES;
  private float currentPitchOffsetDegrees = DEFAULT_PITCH_OFFSET_DEGREES;
  private float targetYawOffsetDegrees = DEFAULT_YAW_OFFSET_DEGREES;
  private float targetPitchOffsetDegrees = DEFAULT_PITCH_OFFSET_DEGREES;
  private long lastUpdateNanos = Long.MIN_VALUE;

  public GameplayCameraController() {
    this(System::nanoTime);
  }

  public GameplayCameraController(LongSupplier nanoClock) {
    this.nanoClock = Objects.requireNonNull(nanoClock, "nanoClock");
  }

  public void adjust(float yawDeltaDegrees, float pitchDeltaDegrees) {
    targetYawOffsetDegrees = normalizeDegrees(targetYawOffsetDegrees + yawDeltaDegrees);
    targetPitchOffsetDegrees = clamp(
        targetPitchOffsetDegrees + pitchDeltaDegrees,
        MIN_PITCH_OFFSET_DEGREES,
        MAX_PITCH_OFFSET_DEGREES
    );
  }

  public CameraOrbitOffsets update() {
    long now = nanoClock.getAsLong();
    float elapsedSeconds = elapsedSeconds(now);
    currentYawOffsetDegrees = approachAngle(
        currentYawOffsetDegrees,
        targetYawOffsetDegrees,
        YAW_SMOOTH_DEGREES_PER_SECOND * elapsedSeconds
    );
    currentPitchOffsetDegrees = approach(
        currentPitchOffsetDegrees,
        targetPitchOffsetDegrees,
        PITCH_SMOOTH_DEGREES_PER_SECOND * elapsedSeconds
    );
    lastUpdateNanos = now;
    return new CameraOrbitOffsets(currentYawOffsetDegrees, currentPitchOffsetDegrees);
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

  public record CameraOrbitOffsets(float yawOffsetDegrees, float pitchOffsetDegrees) {
  }
}
