package io.github.ffakira.rsps.client.desktop.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GameplayCameraControllerTest {

  @Test
  void easesTowardAdjustedOrbitOffsetsInsteadOfSnappingImmediately() {
    TestNanoClock clock = new TestNanoClock();
    GameplayCameraController controller = new GameplayCameraController(clock::now);

    GameplayCameraController.CameraOrbitOffsets initial = controller.update();
    controller.adjust(24.0f, 5.0f);
    clock.advanceNanos(50_000_000L);
    GameplayCameraController.CameraOrbitOffsets smoothed = controller.update();
    clock.advanceNanos(500_000_000L);
    GameplayCameraController.CameraOrbitOffsets settled = controller.update();

    assertThat(initial.yawOffsetDegrees()).isEqualTo(GameplayCameraController.DEFAULT_YAW_OFFSET_DEGREES);
    assertThat(initial.pitchOffsetDegrees()).isEqualTo(GameplayCameraController.DEFAULT_PITCH_OFFSET_DEGREES);
    assertThat(smoothed.yawOffsetDegrees()).isBetween(-34.0f, -32.0f);
    assertThat(smoothed.pitchOffsetDegrees()).isBetween(7.4f, 7.6f);
    assertThat(settled.yawOffsetDegrees()).isEqualTo(-21.0f);
    assertThat(settled.pitchOffsetDegrees()).isEqualTo(8.0f);
  }

  @Test
  void clampsPitchTargetWithinSafeBounds() {
    TestNanoClock clock = new TestNanoClock();
    GameplayCameraController controller = new GameplayCameraController(clock::now);

    controller.update();
    controller.adjust(0.0f, 50.0f);
    clock.advanceNanos(1_000_000_000L);
    GameplayCameraController.CameraOrbitOffsets raised = controller.update();
    controller.adjust(0.0f, -50.0f);
    clock.advanceNanos(1_000_000_000L);
    GameplayCameraController.CameraOrbitOffsets lowered = controller.update();

    assertThat(raised.pitchOffsetDegrees()).isEqualTo(GameplayCameraController.MAX_PITCH_OFFSET_DEGREES);
    assertThat(lowered.pitchOffsetDegrees()).isEqualTo(GameplayCameraController.MIN_PITCH_OFFSET_DEGREES);
  }

  @Test
  void normalizesYawTargetAcrossTheWrapBoundary() {
    TestNanoClock clock = new TestNanoClock();
    GameplayCameraController controller = new GameplayCameraController(clock::now);

    controller.update();
    controller.adjust(220.0f, 0.0f);
    for (int step = 0; step < 4; step++) {
      clock.advanceNanos(250_000_000L);
      controller.update();
    }
    GameplayCameraController.CameraOrbitOffsets wrapped = controller.update();

    assertThat(wrapped.yawOffsetDegrees()).isEqualTo(175.0f);
  }

  private static final class TestNanoClock {

    private long now;

    long now() {
      return now;
    }

    void advanceNanos(long nanos) {
      now += nanos;
    }
  }
}
