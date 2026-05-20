package io.github.ffakira.rsps.client.desktop.gameplay;

import java.util.Objects;
import java.util.function.LongSupplier;

public final class GameplayCameraController {

  private static final float DEGREES_PER_CAMERA_UNIT = 360.0f / 2048.0f;
  private static final int FULL_CAMERA_CIRCLE_UNITS = 2048;
  private static final int DEFAULT_YAW_UNITS = 1280;
  private static final int DEFAULT_PITCH_UNITS = 176;
  private static final int MIN_PITCH_UNITS = 128;
  private static final int MAX_PITCH_UNITS = 383;
  private static final long LEGACY_TICK_NANOS = 20_000_000L;
  private static final long MAX_ACCUMULATED_NANOS = LEGACY_TICK_NANOS * 12L;
  private static final int LEFT_YAW_TARGET_VELOCITY_UNITS = -24;
  private static final int RIGHT_YAW_TARGET_VELOCITY_UNITS = 24;
  private static final int UP_PITCH_TARGET_VELOCITY_UNITS = 12;
  private static final int DOWN_PITCH_TARGET_VELOCITY_UNITS = -12;

  static final float DEFAULT_YAW_DEGREES = degreesFromUnits(DEFAULT_YAW_UNITS);
  static final float DEFAULT_PITCH_DEGREES = degreesFromUnits(DEFAULT_PITCH_UNITS);
  static final float MIN_PITCH_DEGREES = degreesFromUnits(MIN_PITCH_UNITS);
  static final float MAX_PITCH_DEGREES = degreesFromUnits(MAX_PITCH_UNITS);

  private final LongSupplier nanoClock;
  private int currentYawUnits = DEFAULT_YAW_UNITS;
  private int currentPitchUnits = DEFAULT_PITCH_UNITS;
  private int yawVelocityUnits;
  private int pitchVelocityUnits;
  private boolean rotateLeftPressed;
  private boolean rotateRightPressed;
  private boolean pitchUpPressed;
  private boolean pitchDownPressed;
  private long lastUpdateNanos = Long.MIN_VALUE;
  private long accumulatedNanos;

  public GameplayCameraController() {
    this(System::nanoTime);
  }

  public GameplayCameraController(LongSupplier nanoClock) {
    this.nanoClock = Objects.requireNonNull(nanoClock, "nanoClock");
  }

  public void adjust(float yawDeltaDegrees, float pitchDeltaDegrees) {
    currentYawUnits = normalizeUnits(currentYawUnits + Math.round(yawDeltaDegrees / DEGREES_PER_CAMERA_UNIT));
    currentPitchUnits = clamp(
        currentPitchUnits + Math.round(pitchDeltaDegrees / DEGREES_PER_CAMERA_UNIT),
        MIN_PITCH_UNITS,
        MAX_PITCH_UNITS
    );
  }

  public void setHeldInputs(
      boolean rotateLeftPressed,
      boolean rotateRightPressed,
      boolean pitchUpPressed,
      boolean pitchDownPressed
  ) {
    this.rotateLeftPressed = rotateLeftPressed;
    this.rotateRightPressed = rotateRightPressed;
    this.pitchUpPressed = pitchUpPressed;
    this.pitchDownPressed = pitchDownPressed;
  }

  public void clearInputs() {
    setHeldInputs(false, false, false, false);
  }

  public CameraOrbitAngles update() {
    long now = nanoClock.getAsLong();
    if (lastUpdateNanos == Long.MIN_VALUE) {
      lastUpdateNanos = now;
      return currentAngles();
    }
    accumulatedNanos = Math.min(
        MAX_ACCUMULATED_NANOS,
        accumulatedNanos + Math.max(0L, now - lastUpdateNanos)
    );
    lastUpdateNanos = now;
    while (accumulatedNanos >= LEGACY_TICK_NANOS) {
      applyLegacyTick();
      accumulatedNanos -= LEGACY_TICK_NANOS;
    }
    return currentAngles();
  }

  private void applyLegacyTick() {
    // The reference client updates camera orbit from held arrow-key state on the 20 ms game tick,
    // not directly from keypress events. The integer velocity math below mirrors anInt1186/1187.
    yawVelocityUnits = advanceVelocity(
        rotateLeftPressed,
        rotateRightPressed,
        yawVelocityUnits,
        LEFT_YAW_TARGET_VELOCITY_UNITS,
        RIGHT_YAW_TARGET_VELOCITY_UNITS
    );
    pitchVelocityUnits = advanceVelocity(
        pitchUpPressed,
        pitchDownPressed,
        pitchVelocityUnits,
        UP_PITCH_TARGET_VELOCITY_UNITS,
        DOWN_PITCH_TARGET_VELOCITY_UNITS
    );
    currentYawUnits = normalizeUnits(currentYawUnits + yawVelocityUnits / 2);
    currentPitchUnits = clamp(currentPitchUnits + pitchVelocityUnits / 2, MIN_PITCH_UNITS, MAX_PITCH_UNITS);
  }

  private int advanceVelocity(
      boolean negativeDirectionPressed,
      boolean positiveDirectionPressed,
      int currentVelocityUnits,
      int negativeTargetVelocityUnits,
      int positiveTargetVelocityUnits
  ) {
    if (negativeDirectionPressed) {
      return currentVelocityUnits + (negativeTargetVelocityUnits - currentVelocityUnits) / 2;
    }
    if (positiveDirectionPressed) {
      return currentVelocityUnits + (positiveTargetVelocityUnits - currentVelocityUnits) / 2;
    }
    return currentVelocityUnits / 2;
  }

  private CameraOrbitAngles currentAngles() {
    return new CameraOrbitAngles(
        normalizeDegrees(degreesFromUnits(currentYawUnits)),
        degreesFromUnits(currentPitchUnits)
    );
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

  private static int normalizeUnits(int units) {
    int normalized = units % FULL_CAMERA_CIRCLE_UNITS;
    return normalized < 0 ? normalized + FULL_CAMERA_CIRCLE_UNITS : normalized;
  }

  private static float degreesFromUnits(int units) {
    return units * DEGREES_PER_CAMERA_UNIT;
  }

  private static int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }

  public record CameraOrbitAngles(float yawDegrees, float pitchDegrees) {
  }
}
