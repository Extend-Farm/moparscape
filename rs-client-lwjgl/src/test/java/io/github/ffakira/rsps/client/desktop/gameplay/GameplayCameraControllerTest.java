package io.github.ffakira.rsps.client.desktop.gameplay;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;

class GameplayCameraControllerTest {

  @Test
  void easesTowardAdjustedOrbitAnglesInsteadOfSnappingImmediately() {
    TestNanoClock clock = new TestNanoClock();
    GameplayCameraController controller = new GameplayCameraController(clock::now);

    GameplayCameraController.CameraOrbitAngles initial = controller.update();
    controller.adjust(24.0f, 5.0f);
    clock.advanceNanos(50_000_000L);
    GameplayCameraController.CameraOrbitAngles smoothed = controller.update();
    clock.advanceNanos(500_000_000L);
    GameplayCameraController.CameraOrbitAngles settled = controller.update();

    assertThat(initial.yawDegrees()).isEqualTo(-135.0f);
    assertThat(initial.pitchDegrees()).isEqualTo(GameplayCameraController.DEFAULT_PITCH_DEGREES);
    assertThat(smoothed.yawDegrees()).isBetween(-123.2f, -122.8f);
    assertThat(smoothed.pitchDegrees()).isBetween(35.3f, 35.5f);
    assertThat(settled.yawDegrees()).isEqualTo(-111.0f);
    assertThat(settled.pitchDegrees()).isBetween(35.8f, 36.0f);
  }

  @Test
  void clampsPitchTargetWithinLegacyOrbitBounds() {
    TestNanoClock clock = new TestNanoClock();
    GameplayCameraController controller = new GameplayCameraController(clock::now);

    controller.update();
    controller.adjust(0.0f, 50.0f);
    GameplayCameraController.CameraOrbitAngles raised = advanceToSettledPose(controller, clock);
    controller.adjust(0.0f, -90.0f);
    GameplayCameraController.CameraOrbitAngles lowered = advanceToSettledPose(controller, clock);

    assertThat(raised.pitchDegrees()).isEqualTo(GameplayCameraController.MAX_PITCH_DEGREES);
    assertThat(lowered.pitchDegrees()).isEqualTo(GameplayCameraController.MIN_PITCH_DEGREES);
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
    GameplayCameraController.CameraOrbitAngles wrapped = controller.update();

    assertThat(wrapped.yawDegrees()).isCloseTo(85.0f, within(0.001f));
  }

  private GameplayCameraController.CameraOrbitAngles advanceToSettledPose(
      GameplayCameraController controller,
      TestNanoClock clock
  ) {
    GameplayCameraController.CameraOrbitAngles pose = controller.update();
    for (int step = 0; step < 4; step++) {
      clock.advanceNanos(250_000_000L);
      pose = controller.update();
    }
    return pose;
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
