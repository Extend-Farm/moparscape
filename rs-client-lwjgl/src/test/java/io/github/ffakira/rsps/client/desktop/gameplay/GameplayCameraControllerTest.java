package io.github.ffakira.rsps.client.desktop.gameplay;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;

class GameplayCameraControllerTest {

  @Test
  void acceleratesHeldYawInputUsingLegacyVelocitySteps() {
    TestNanoClock clock = new TestNanoClock();
    GameplayCameraController controller = new GameplayCameraController(clock::now);

    assertThat(controller.update().yawDegrees()).isCloseTo(0.0f, within(0.0001f));

    controller.setHeldInputs(true, false, false, false);
    GameplayCameraController.CameraOrbitAngles firstTick = advanceTick(controller, clock);
    GameplayCameraController.CameraOrbitAngles fourthTick = advanceTicks(controller, clock, 3);

    assertThat(firstTick.yawDegrees()).isCloseTo(-1.05469f, within(0.0001f));
    assertThat(fourthTick.yawDegrees()).isCloseTo(-6.32812f, within(0.0001f));
  }

  @Test
  void keepsTurningBrieflyAfterTheHeldKeyIsReleased() {
    TestNanoClock clock = new TestNanoClock();
    GameplayCameraController controller = new GameplayCameraController(clock::now);

    controller.update();
    controller.setHeldInputs(true, false, false, false);
    GameplayCameraController.CameraOrbitAngles heldPose = advanceTicks(controller, clock, 4);

    controller.clearInputs();
    GameplayCameraController.CameraOrbitAngles releasedPose = advanceTick(controller, clock);
    GameplayCameraController.CameraOrbitAngles settledPose = advanceTicks(controller, clock, 4);

    assertThat(releasedPose.yawDegrees()).isLessThan(heldPose.yawDegrees());
    assertThat(settledPose.yawDegrees()).isCloseTo(-7.73438f, within(0.0001f));
  }

  @Test
  void givesLeftAndUpPriorityWhenOppositeDirectionsAreHeldTogether() {
    TestNanoClock clock = new TestNanoClock();
    GameplayCameraController controller = new GameplayCameraController(clock::now);

    controller.update();
    controller.setHeldInputs(true, true, true, true);
    GameplayCameraController.CameraOrbitAngles pose = advanceTick(controller, clock);

    assertThat(pose.yawDegrees()).isLessThan(GameplayCameraController.DEFAULT_YAW_DEGREES);
    assertThat(pose.pitchDegrees()).isGreaterThan(GameplayCameraController.DEFAULT_PITCH_DEGREES);
  }

  @Test
  void clampsPitchWithinLegacyOrbitBounds() {
    TestNanoClock clock = new TestNanoClock();
    GameplayCameraController controller = new GameplayCameraController(clock::now);

    controller.update();
    controller.setHeldInputs(false, false, true, false);
    GameplayCameraController.CameraOrbitAngles raisedPose = advanceTicks(controller, clock, 80);
    controller.setHeldInputs(false, false, false, true);
    GameplayCameraController.CameraOrbitAngles loweredPose = advanceTicks(controller, clock, 120);

    assertThat(raisedPose.pitchDegrees()).isEqualTo(GameplayCameraController.MAX_PITCH_DEGREES);
    assertThat(loweredPose.pitchDegrees()).isEqualTo(GameplayCameraController.MIN_PITCH_DEGREES);
  }

  @Test
  void manualAdjustStillWrapsYawAndClampsPitch() {
    TestNanoClock clock = new TestNanoClock();
    GameplayCameraController controller = new GameplayCameraController(clock::now);

    controller.update();
    controller.adjust(220.0f, 80.0f);
    GameplayCameraController.CameraOrbitAngles adjusted = controller.update();

    assertThat(adjusted.yawDegrees()).isCloseTo(-139.921875f, within(0.0001f));
    assertThat(adjusted.pitchDegrees()).isEqualTo(GameplayCameraController.MAX_PITCH_DEGREES);
  }

  private GameplayCameraController.CameraOrbitAngles advanceTick(
      GameplayCameraController controller,
      TestNanoClock clock
  ) {
    return advanceTicks(controller, clock, 1);
  }

  private GameplayCameraController.CameraOrbitAngles advanceTicks(
      GameplayCameraController controller,
      TestNanoClock clock,
      int tickCount
  ) {
    GameplayCameraController.CameraOrbitAngles pose = controller.update();
    for (int step = 0; step < tickCount; step++) {
      clock.advanceNanos(20_000_000L);
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
