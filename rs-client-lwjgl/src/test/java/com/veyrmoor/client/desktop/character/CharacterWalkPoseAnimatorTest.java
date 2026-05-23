package com.veyrmoor.client.desktop.character;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CharacterWalkPoseAnimatorTest {

  @Test
  void keepsHeadVerticesOutOfTheSyntheticArmSwingBand() {
    float[][] transformedVertices = {
        {0.24f, 0.24f},
        {1.18f, 1.79f},
        {0.02f, 0.02f}
    };
    float[][] originalVertices = copyOf(transformedVertices);

    CharacterWalkPoseAnimator.apply(
        transformedVertices,
        new CharacterActorBounds(-0.30f, 0.0f, -0.15f, 0.30f, 1.80f, 0.15f),
        new ActorAnimationState(1.0f, 0.0f, 180.0f)
    );

    float armDisplacement = displacementMagnitude(originalVertices, transformedVertices, 0);
    float headDisplacement = displacementMagnitude(originalVertices, transformedVertices, 1);

    assertThat(armDisplacement).isGreaterThan(0.025f);
    assertThat(headDisplacement).isLessThan(0.012f);
    assertThat(armDisplacement).isGreaterThan(headDisplacement * 2.5f);
  }

  @Test
  void sidestepPoseShiftsLowerBodyLaterallyMoreThanForwardWalk() {
    float[][] forwardVertices = {
        {-0.10f, 0.10f},
        {0.22f, 0.22f},
        {0.00f, 0.00f}
    };
    float[][] sidestepVertices = copyOf(forwardVertices);
    float[][] baseVertices = copyOf(forwardVertices);

    CharacterWalkPoseAnimator.apply(
        forwardVertices,
        new CharacterActorBounds(-0.30f, 0.0f, -0.15f, 0.30f, 1.80f, 0.15f),
        new ActorAnimationState(1.0f, 0.0f, 0.0f, 0.0f, ActorAnimationState.LocomotionMode.WALK_FORWARD, -1, -1, 0.0f, 0.0f)
    );
    CharacterWalkPoseAnimator.apply(
        sidestepVertices,
        new CharacterActorBounds(-0.30f, 0.0f, -0.15f, 0.30f, 1.80f, 0.15f),
        new ActorAnimationState(1.0f, 0.0f, 90.0f, 0.0f, ActorAnimationState.LocomotionMode.STRAFE_LEFT, -1, -1, 0.0f, 0.0f)
    );

    float forwardXDisplacement = Math.abs(forwardVertices[0][0] - baseVertices[0][0]);
    float sidestepXDisplacement = Math.abs(sidestepVertices[0][0] - baseVertices[0][0]);

    assertThat(sidestepXDisplacement).isGreaterThan(forwardXDisplacement + 0.01f);
  }

  private float[][] copyOf(float[][] vertices) {
    return new float[][]{
        vertices[0].clone(),
        vertices[1].clone(),
        vertices[2].clone()
    };
  }

  private float displacementMagnitude(float[][] before, float[][] after, int index) {
    float deltaX = after[0][index] - before[0][index];
    float deltaY = after[1][index] - before[1][index];
    float deltaZ = after[2][index] - before[2][index];
    return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
  }
}
