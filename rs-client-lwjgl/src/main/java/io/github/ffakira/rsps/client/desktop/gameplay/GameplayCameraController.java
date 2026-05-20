package io.github.ffakira.rsps.client.desktop.gameplay;

import java.util.Objects;
import java.util.function.LongSupplier;

public final class GameplayCameraController {

  private static final float CAMERA_UNITS_PER_DEGREE = 2048.0f / 360.0f;
  private static final float DEGREES_PER_CAMERA_UNIT = 360.0f / 2048.0f;
  private static final float FULL_CAMERA_CIRCLE_UNITS = 2048.0f;
  private static final float DEFAULT_YAW_UNITS = 1280.0f;
  private static final float DEFAULT_PITCH_UNITS = 176.0f;
  private static final float MIN_PITCH_UNITS = 128.0f;
  private static final float MAX_PITCH_UNITS = 383.0f;

  static final float DEFAULT_YAW_DEGREES = degreesFromUnits(DEFAULT_YAW_UNITS);
  static final float DEFAULT_PITCH_DEGREES = degreesFromUnits(DEFAULT_PITCH_UNITS);
  static final float MIN_PITCH_DEGREES = degreesFromUnits(MIN_PITCH_UNITS);
  static final float MAX_PITCH_DEGREES = degreesFromUnits(MAX_PITCH_UNITS);

  private static final float YAW_SMOOTH_UNITS_PER_SECOND = 240.0f * CAMERA_UNITS_PER_DEGREE;
  private static final float PITCH_SMOOTH_UNITS_PER_SECOND = 90.0f * CAMERA_UNITS_PER_DEGREE;

  private final LongSupplier nanoClock;
  private float currentYawUnits = DEFAULT_YAW_UNITS;
  private float currentPitchUnits = DEFAULT_PITCH_UNITS;
  private float targetYawUnits = DEFAULT_YAW_UNITS;
  private float targetPitchUnits = DEFAULT_PITCH_UNITS;
  private long lastUpdateNanos = Long.MIN_VALUE;

  public GameplayCameraController() {
    this(System::nanoTime);
  }

  public GameplayCameraController(LongSupplier nanoClock) {
    this.nanoClock = Objects.requireNonNull(nanoClock, "nanoClock");
  }

  public void adjust(float yawDeltaDegrees, float pitchDeltaDegrees) {
    targetYawUnits = normalizeUnits(targetYawUnits + yawDeltaDegrees * CAMERA_UNITS_PER_DEGREE);
    targetPitchUnits = clamp(
        targetPitchUnits + pitchDeltaDegrees * CAMERA_UNITS_PER_DEGREE,
        MIN_PITCH_UNITS,
        MAX_PITCH_UNITS
    );
  }

  public CameraOrbitAngles update() {
    long now = nanoClock.getAsLong();
    float elapsedSeconds = elapsedSeconds(now);
    currentYawUnits = approachWrappedUnits(
        currentYawUnits,
        targetYawUnits,
        YAW_SMOOTH_UNITS_PER_SECOND * elapsedSeconds
    );
    currentPitchUnits = approach(
        currentPitchUnits,
        targetPitchUnits,
        PITCH_SMOOTH_UNITS_PER_SECOND * elapsedSeconds
    );
    lastUpdateNanos = now;
    return new CameraOrbitAngles(
        normalizeDegrees(degreesFromUnits(currentYawUnits)),
        degreesFromUnits(currentPitchUnits)
    );
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

  private float approachWrappedUnits(float current, float target, float maximumDeltaUnits) {
    float delta = wrappedUnitDelta(current, target);
    if (Math.abs(delta) <= maximumDeltaUnits) {
      return normalizeUnits(target);
    }
    return normalizeUnits(current + Math.copySign(maximumDeltaUnits, delta));
  }

  private static float wrappedUnitDelta(float current, float target) {
    float delta = normalizeUnits(target) - normalizeUnits(current);
    if (delta > FULL_CAMERA_CIRCLE_UNITS * 0.5f) {
      delta -= FULL_CAMERA_CIRCLE_UNITS;
    } else if (delta < -(FULL_CAMERA_CIRCLE_UNITS * 0.5f)) {
      delta += FULL_CAMERA_CIRCLE_UNITS;
    }
    return delta;
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

  private static float normalizeUnits(float units) {
    float normalized = units % FULL_CAMERA_CIRCLE_UNITS;
    return normalized < 0.0f ? normalized + FULL_CAMERA_CIRCLE_UNITS : normalized;
  }

  private static float degreesFromUnits(float units) {
    return units * DEGREES_PER_CAMERA_UNIT;
  }

  private float clamp(float value, float minimum, float maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }

  public record CameraOrbitAngles(float yawDegrees, float pitchDegrees) {
  }
}
