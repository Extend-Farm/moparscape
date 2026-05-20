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

  private void assertWalkFrame(ActorAnimationState animationState, int expectedFrameIndex, float expectedProgress) {
    assertThat(animationState.walkFrameIndex()).isEqualTo(expectedFrameIndex);
    assertThat(animationState.walkFrameProgress()).isEqualTo(expectedProgress);
  }
}
