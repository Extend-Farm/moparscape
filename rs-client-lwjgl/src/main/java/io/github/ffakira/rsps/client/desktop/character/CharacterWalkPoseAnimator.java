package io.github.ffakira.rsps.client.desktop.character;

final class CharacterWalkPoseAnimator {

  private static final float WALK_ARM_SWING_DEGREES = 14.0f;
  private static final float WALK_LEG_SWING_DEGREES = 18.0f;
  private static final float WALK_BODY_BOB = 0.010f;
  private static final float WALK_TORSO_TWIST_DEGREES = 2.2f;
  private static final float WALK_FORWARD_LEAN_DEGREES = 1.6f;
  private static final float WALK_FOOT_LIFT = 0.028f;
  private static final float WALK_PELVIS_SWAY = 0.008f;
  private static final float SIDE_STEP_LATERAL_SHIFT = 0.026f;
  private static final float SIDE_STEP_TILT_DEGREES = 3.8f;
  private static final float ARM_SWING_TOP_PORTION = 0.84f;
  private static final float TORSO_TWIST_TOP_PORTION = 0.90f;
  private static final float[] WALK_FRAME_SWING = {1.0f, 0.18f, -1.0f, -0.18f, 1.0f};
  private static final float[] WALK_FRAME_COUNTER_SWING = {0.0f, 1.0f, 0.0f, -1.0f, 0.0f};
  private static final float[] WALK_FRAME_BOB = {0.20f, 1.0f, 0.20f, 1.0f, 0.20f};
  private static final float[] WALK_FRAME_FORWARD_LEAN = {0.72f, 1.0f, 0.72f, 1.0f, 0.72f};

  private CharacterWalkPoseAnimator() {
  }

  static void apply(
      float[][] transformedVertices,
      CharacterActorBounds actorBounds,
      ActorAnimationState animationState
  ) {
    float strideWeight = animationState.strideWeight();
    LocomotionProfile locomotionProfile = locomotionProfile(animationState.locomotionMode());
    // This four-phase pose loop only runs when no decoded cache frame is active for the current
    // movement/action state. It keeps locomotion readable while preserving the frame-shaped timing
    // that the reference-client sequence path uses when cache animation is available.
    int walkFrameIndex = animationState.walkFrameIndex();
    float walkFrameProgress = animationState.walkFrameProgress();
    float swing = interpolateWalkFrame(WALK_FRAME_SWING, walkFrameIndex, walkFrameProgress);
    float counterSwing = interpolateWalkFrame(WALK_FRAME_COUNTER_SWING, walkFrameIndex, walkFrameProgress);
    float bob = interpolateWalkFrame(WALK_FRAME_BOB, walkFrameIndex, walkFrameProgress)
        * WALK_BODY_BOB
        * locomotionProfile.bodyBobScale()
        * strideWeight;
    float torsoTwist = swing * WALK_TORSO_TWIST_DEGREES * locomotionProfile.torsoTwistScale() * strideWeight;
    float pelvisSway = counterSwing * WALK_PELVIS_SWAY * locomotionProfile.pelvisSwayScale() * strideWeight;
    float forwardLean = interpolateWalkFrame(WALK_FRAME_FORWARD_LEAN, walkFrameIndex, walkFrameProgress)
        * WALK_FORWARD_LEAN_DEGREES
        * locomotionProfile.forwardLeanScale()
        * strideWeight;
    float centerX = actorBounds.centerX();
    float centerZ = actorBounds.centerZ();
    float width = Math.max(0.001f, actorBounds.maxX() - actorBounds.minX());
    float height = Math.max(0.001f, actorBounds.maxY() - actorBounds.minY());
    float kneeY = actorBounds.minY() + height * 0.28f;
    float hipY = actorBounds.minY() + height * 0.49f;
    float shoulderY = actorBounds.minY() + height * 0.77f;
    float armSwingTopY = actorBounds.minY() + height * ARM_SWING_TOP_PORTION;
    float torsoTwistTopY = actorBounds.minY() + height * TORSO_TWIST_TOP_PORTION;
    float armBlendStart = width * 0.14f;
    float armBlendRange = Math.max(0.001f, width * 0.20f);
    for (int vertexIndex = 0; vertexIndex < transformedVertices[0].length; vertexIndex++) {
      float x = transformedVertices[0][vertexIndex];
      float y = transformedVertices[1][vertexIndex] - bob;
      float z = transformedVertices[2][vertexIndex];
      float xOffset = x - centerX;
      float side = xOffset < 0.0f ? -1.0f : 1.0f;

      if (y < hipY) {
        float legBlend = smoothstep((hipY - y) / Math.max(0.001f, hipY - actorBounds.minY()));
        float[] rotated = rotateAroundX(
            y,
            z,
            hipY,
            centerZ,
            side * swing * WALK_LEG_SWING_DEGREES * locomotionProfile.legSwingScale() * strideWeight
        );
        y = lerp(y, rotated[0], legBlend);
        z = lerp(z, rotated[1], legBlend);
        if (y < kneeY) {
          float liftBlend = smoothstep((kneeY - y) / Math.max(0.001f, kneeY - actorBounds.minY()));
          y += Math.max(0.0f, -side * swing * locomotionProfile.stepSign()) * WALK_FOOT_LIFT * liftBlend * strideWeight;
          x += side * pelvisSway * liftBlend;
          if (locomotionProfile.sideStepDirection() != 0.0f) {
            x += side
                * locomotionProfile.sideStepDirection()
                * swing
                * SIDE_STEP_LATERAL_SHIFT
                * liftBlend
                * strideWeight;
          }
        }
      } else {
        float armBlend = smoothstep((Math.abs(xOffset) - armBlendStart) / armBlendRange)
            * verticalBandBlend(y, hipY, shoulderY, armSwingTopY, actorBounds.maxY());
        if (armBlend > 0.0f) {
          float[] rotated = rotateAroundX(
              y,
              z,
              shoulderY,
              centerZ,
              -side * swing * WALK_ARM_SWING_DEGREES * locomotionProfile.armSwingScale() * strideWeight
          );
          y = lerp(y, rotated[0], armBlend);
          z = lerp(z, rotated[1], armBlend);
          if (locomotionProfile.sideStepDirection() != 0.0f) {
            float[] tilted = rotateAroundZ(
                x,
                y,
                centerX,
                shoulderY,
                side * locomotionProfile.sideStepDirection() * swing * SIDE_STEP_TILT_DEGREES * strideWeight
            );
            x = lerp(x, tilted[0], armBlend * 0.42f);
            y = lerp(y, tilted[1], armBlend * 0.42f);
          }
        }
      }

      float torsoBlend = verticalBandBlend(y, hipY, shoulderY, torsoTwistTopY, actorBounds.maxY());
      if (torsoBlend > 0.0f) {
        float[] twisted = rotateAroundY(x, z, centerX, centerZ, torsoTwist);
        x = lerp(x, twisted[0], torsoBlend * 0.55f);
        z = lerp(z, twisted[1], torsoBlend * 0.55f);
        float[] leaned = rotateAroundX(y, z, hipY, centerZ, forwardLean);
        y = lerp(y, leaned[0], torsoBlend * 0.38f);
        z = lerp(z, leaned[1], torsoBlend * 0.38f);
        if (locomotionProfile.sideStepDirection() != 0.0f) {
          float[] sideTilted = rotateAroundZ(
              x,
              y,
              centerX,
              hipY,
              locomotionProfile.sideStepDirection() * counterSwing * SIDE_STEP_TILT_DEGREES * strideWeight
          );
          x = lerp(x, sideTilted[0], torsoBlend * 0.22f);
          y = lerp(y, sideTilted[1], torsoBlend * 0.22f);
        }
      }

      float leanBlend = smoothstep((y - actorBounds.minY()) / height);
      if (leanBlend > 0.0f) {
        x += pelvisSway * leanBlend * 0.35f;
      }

      transformedVertices[0][vertexIndex] = x;
      transformedVertices[1][vertexIndex] = y;
      transformedVertices[2][vertexIndex] = z;
    }
  }

  private static float lerp(float start, float end, float weight) {
    return start + (end - start) * weight;
  }

  private static float interpolateWalkFrame(float[] keyframes, int frameIndex, float frameProgress) {
    int currentIndex = Math.max(0, Math.min(keyframes.length - 2, frameIndex));
    return lerp(keyframes[currentIndex], keyframes[currentIndex + 1], smoothstep(frameProgress));
  }

  private static float smoothstep(float value) {
    float clamped = Math.max(0.0f, Math.min(1.0f, value));
    return clamped * clamped * (3.0f - 2.0f * clamped);
  }

  private static float verticalBandBlend(float y, float startY, float fullBlendY, float falloffStartY, float maxY) {
    float rise = smoothstep((y - startY) / Math.max(0.001f, fullBlendY - startY));
    float fall = 1.0f - smoothstep((y - falloffStartY) / Math.max(0.001f, maxY - falloffStartY));
    return rise * fall;
  }

  private static float[] rotateAroundX(float y, float z, float pivotY, float pivotZ, float degrees) {
    float radians = (float) Math.toRadians(degrees);
    float cosine = (float) Math.cos(radians);
    float sine = (float) Math.sin(radians);
    float shiftedY = y - pivotY;
    float shiftedZ = z - pivotZ;
    float rotatedY = shiftedY * cosine - shiftedZ * sine;
    float rotatedZ = shiftedY * sine + shiftedZ * cosine;
    return new float[]{rotatedY + pivotY, rotatedZ + pivotZ};
  }

  private static float[] rotateAroundY(float x, float z, float pivotX, float pivotZ, float degrees) {
    float radians = (float) Math.toRadians(degrees);
    float cosine = (float) Math.cos(radians);
    float sine = (float) Math.sin(radians);
    float shiftedX = x - pivotX;
    float shiftedZ = z - pivotZ;
    float rotatedX = shiftedX * cosine + shiftedZ * sine;
    float rotatedZ = shiftedZ * cosine - shiftedX * sine;
    return new float[]{rotatedX + pivotX, rotatedZ + pivotZ};
  }

  private static float[] rotateAroundZ(float x, float y, float pivotX, float pivotY, float degrees) {
    float radians = (float) Math.toRadians(degrees);
    float cosine = (float) Math.cos(radians);
    float sine = (float) Math.sin(radians);
    float shiftedX = x - pivotX;
    float shiftedY = y - pivotY;
    float rotatedX = shiftedX * cosine - shiftedY * sine;
    float rotatedY = shiftedX * sine + shiftedY * cosine;
    return new float[]{rotatedX + pivotX, rotatedY + pivotY};
  }

  private static LocomotionProfile locomotionProfile(ActorAnimationState.LocomotionMode locomotionMode) {
    return switch (locomotionMode) {
      case RUN_FORWARD -> new LocomotionProfile(1.55f, 1.38f, 1.30f, 0.95f, 1.35f, 1.25f, 1.0f, 0.0f);
      case WALK_BACKWARD -> new LocomotionProfile(-0.72f, -0.72f, 0.72f, -0.85f, 0.75f, 0.85f, 1.0f, 0.0f);
      case STRAFE_LEFT -> new LocomotionProfile(0.62f, 0.78f, 0.18f, 0.12f, 0.78f, 0.70f, 1.0f, -1.0f);
      case STRAFE_RIGHT -> new LocomotionProfile(0.62f, 0.78f, -0.18f, 0.12f, 0.78f, 0.70f, 1.0f, 1.0f);
      case WALK_FORWARD, IDLE -> new LocomotionProfile(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f);
    };
  }

  private record LocomotionProfile(
      float armSwingScale,
      float legSwingScale,
      float torsoTwistScale,
      float forwardLeanScale,
      float bodyBobScale,
      float pelvisSwayScale,
      float stepSign,
      float sideStepDirection
  ) {
  }
}
