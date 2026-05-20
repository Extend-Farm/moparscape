package io.github.ffakira.rsps.client.desktop.character;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ActorAnimationStateTest {

  @Test
  void derivesDiscreteWalkFramesFromTheWrappedWalkCycle() {
    assertWalkFrame(new ActorAnimationState(1.0f, 0.0f, 0.0f), 0, 0.0f);
    assertWalkFrame(new ActorAnimationState(1.0f, (float) (Math.PI * 0.5), 0.0f), 1, 0.0f);
    assertWalkFrame(new ActorAnimationState(1.0f, (float) Math.PI, 0.0f), 2, 0.0f);
    assertWalkFrame(new ActorAnimationState(1.0f, (float) (Math.PI * 1.5), 0.0f), 3, 0.0f);
    assertWalkFrame(new ActorAnimationState(1.0f, (float) (Math.PI * 2.5), 0.0f), 1, 0.0f);
  }

  @Test
  void exposesFractionalProgressInsideTheCurrentWalkFrame() {
    ActorAnimationState animationState = new ActorAnimationState(1.0f, (float) (Math.PI / 4.0), 0.0f);

    assertThat(animationState.walkFrameIndex()).isEqualTo(0);
    assertThat(animationState.walkFrameProgress()).isBetween(0.49f, 0.51f);
  }

  @Test
  void normalizesRelativeTravelHeadingForLocomotionSelection() {
    ActorAnimationState animationState = new ActorAnimationState(
        1.0f,
        0.0f,
        90.0f,
        0.0f,
        ActorAnimationState.LocomotionMode.STRAFE_LEFT,
        822,
        -1,
        0.0f,
        0.0f
    );

    assertThat(animationState.relativeTravelDegrees()).isBetween(-91.0f, -89.0f);
    assertThat(animationState.locomotionMode()).isEqualTo(ActorAnimationState.LocomotionMode.STRAFE_LEFT);
    assertThat(animationState.activeSequenceId()).isEqualTo(822);
  }

  @Test
  void prioritizesActionFramesWhenBothMovementAndActionStateArePresent() {
    ActorAnimationState animationState = new ActorAnimationState(
        1.0f,
        0.0f,
        0.0f,
        0.0f,
        ActorAnimationState.LocomotionMode.WALK_FORWARD,
        819,
        100,
        866,
        200,
        0.0f,
        0.0f
    );

    assertThat(animationState.hasMovementFrame()).isTrue();
    assertThat(animationState.hasActionFrame()).isTrue();
    assertThat(animationState.activeSequenceId()).isEqualTo(866);
    assertThat(animationState.activeFrameId()).isEqualTo(200);
  }

  private void assertWalkFrame(ActorAnimationState animationState, int expectedFrameIndex, float expectedProgress) {
    assertThat(animationState.walkFrameIndex()).isEqualTo(expectedFrameIndex);
    assertThat(animationState.walkFrameProgress()).isEqualTo(expectedProgress);
  }
}
