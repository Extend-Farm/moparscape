package com.veyrmoor.client.desktop.character;

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
  private static final LocomotionProfile RUN_FORWARD_PROFILE =
      new LocomotionProfile(1.55f, 1.38f, 1.30f, 0.95f, 1.35f, 1.25f, 1.0f, 0.0f);
  private static final LocomotionProfile WALK_BACKWARD_PROFILE =
      new LocomotionProfile(-0.72f, -0.72f, 0.72f, -0.85f, 0.75f, 0.85f, 1.0f, 0.0f);
  private static final LocomotionProfile STRAFE_LEFT_PROFILE =
      new LocomotionProfile(0.62f, 0.78f, 0.18f, 0.12f, 0.78f, 0.70f, 1.0f, -1.0f);
  private static final LocomotionProfile STRAFE_RIGHT_PROFILE =
      new LocomotionProfile(0.62f, 0.78f, -0.18f, 0.12f, 0.78f, 0.70f, 1.0f, 1.0f);
  private static final LocomotionProfile WALK_FORWARD_PROFILE =
      new LocomotionProfile(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f);

  private CharacterWalkPoseAnimator() {
  }

  static void apply(
      float[][] transformedVertices,
      CharacterActorBounds actorBounds,
      ActorAnimationState animationState
  ) {
    apply(transformedVertices[0], transformedVertices[1], transformedVertices[2], actorBounds, animationState);
  }

  static void apply(
      float[] transformedX,
      float[] transformedY,
      float[] transformedZ,
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
    float legSwingDegrees = swing * WALK_LEG_SWING_DEGREES * locomotionProfile.legSwingScale() * strideWeight;
    float legSwingCosine = cosDegrees(legSwingDegrees);
    float legSwingSine = sinDegrees(legSwingDegrees);
    float armSwingDegrees = swing * WALK_ARM_SWING_DEGREES * locomotionProfile.armSwingScale() * strideWeight;
    float armSwingCosine = cosDegrees(armSwingDegrees);
    float armSwingSine = sinDegrees(armSwingDegrees);
    float torsoTwist = swing * WALK_TORSO_TWIST_DEGREES * locomotionProfile.torsoTwistScale() * strideWeight;
    float torsoTwistCosine = cosDegrees(torsoTwist);
    float torsoTwistSine = sinDegrees(torsoTwist);
    float pelvisSway = counterSwing * WALK_PELVIS_SWAY * locomotionProfile.pelvisSwayScale() * strideWeight;
    float forwardLean = interpolateWalkFrame(WALK_FRAME_FORWARD_LEAN, walkFrameIndex, walkFrameProgress)
        * WALK_FORWARD_LEAN_DEGREES
        * locomotionProfile.forwardLeanScale()
        * strideWeight;
    float forwardLeanCosine = cosDegrees(forwardLean);
    float forwardLeanSine = sinDegrees(forwardLean);
    float sideStepDirection = locomotionProfile.sideStepDirection();
    boolean hasSideStep = sideStepDirection != 0.0f;
    float sideStepArmTiltDegrees = sideStepDirection * swing * SIDE_STEP_TILT_DEGREES * strideWeight;
    float sideStepArmTiltCosine = cosDegrees(sideStepArmTiltDegrees);
    float sideStepArmTiltSine = sinDegrees(sideStepArmTiltDegrees);
    float torsoSideTiltDegrees = sideStepDirection * counterSwing * SIDE_STEP_TILT_DEGREES * strideWeight;
    float torsoSideTiltCosine = cosDegrees(torsoSideTiltDegrees);
    float torsoSideTiltSine = sinDegrees(torsoSideTiltDegrees);
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
    for (int vertexIndex = 0; vertexIndex < transformedX.length; vertexIndex++) {
      float x = transformedX[vertexIndex];
      float y = transformedY[vertexIndex] - bob;
      float z = transformedZ[vertexIndex];
      float xOffset = x - centerX;
      float side = xOffset < 0.0f ? -1.0f : 1.0f;

      if (y < hipY) {
        float legBlend = smoothstep((hipY - y) / Math.max(0.001f, hipY - actorBounds.minY()));
        float signedLegSwingSine = side * legSwingSine;
        float legShiftedY = y - hipY;
        float legShiftedZ = z - centerZ;
        float rotatedLegY = legShiftedY * legSwingCosine - legShiftedZ * signedLegSwingSine;
        float rotatedLegZ = legShiftedY * signedLegSwingSine + legShiftedZ * legSwingCosine;
        y = lerp(y, rotatedLegY + hipY, legBlend);
        z = lerp(z, rotatedLegZ + centerZ, legBlend);
        if (y < kneeY) {
          float liftBlend = smoothstep((kneeY - y) / Math.max(0.001f, kneeY - actorBounds.minY()));
          y += Math.max(0.0f, -side * swing * locomotionProfile.stepSign()) * WALK_FOOT_LIFT * liftBlend * strideWeight;
          x += side * pelvisSway * liftBlend;
          if (hasSideStep) {
            x += side
                * sideStepDirection
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
          float signedArmSwingSine = -side * armSwingSine;
          float armShiftedY = y - shoulderY;
          float armShiftedZ = z - centerZ;
          float rotatedArmY = armShiftedY * armSwingCosine - armShiftedZ * signedArmSwingSine;
          float rotatedArmZ = armShiftedY * signedArmSwingSine + armShiftedZ * armSwingCosine;
          y = lerp(y, rotatedArmY + shoulderY, armBlend);
          z = lerp(z, rotatedArmZ + centerZ, armBlend);
          if (hasSideStep) {
            float signedSideStepArmTiltSine = side * sideStepArmTiltSine;
            float armTiltShiftedX = x - centerX;
            float armTiltShiftedY = y - shoulderY;
            float tiltedArmX = armTiltShiftedX * sideStepArmTiltCosine - armTiltShiftedY * signedSideStepArmTiltSine;
            float tiltedArmY = armTiltShiftedX * signedSideStepArmTiltSine + armTiltShiftedY * sideStepArmTiltCosine;
            x = lerp(x, tiltedArmX + centerX, armBlend * 0.42f);
            y = lerp(y, tiltedArmY + shoulderY, armBlend * 0.42f);
          }
        }
      }

      float torsoBlend = verticalBandBlend(y, hipY, shoulderY, torsoTwistTopY, actorBounds.maxY());
      if (torsoBlend > 0.0f) {
        float torsoShiftedX = x - centerX;
        float torsoShiftedZ = z - centerZ;
        float twistedX = torsoShiftedX * torsoTwistCosine + torsoShiftedZ * torsoTwistSine;
        float twistedZ = torsoShiftedZ * torsoTwistCosine - torsoShiftedX * torsoTwistSine;
        x = lerp(x, twistedX + centerX, torsoBlend * 0.55f);
        z = lerp(z, twistedZ + centerZ, torsoBlend * 0.55f);
        float leanShiftedY = y - hipY;
        float leanShiftedZ = z - centerZ;
        float leanedY = leanShiftedY * forwardLeanCosine - leanShiftedZ * forwardLeanSine;
        float leanedZ = leanShiftedY * forwardLeanSine + leanShiftedZ * forwardLeanCosine;
        y = lerp(y, leanedY + hipY, torsoBlend * 0.38f);
        z = lerp(z, leanedZ + centerZ, torsoBlend * 0.38f);
        if (hasSideStep) {
          float torsoTiltShiftedX = x - centerX;
          float torsoTiltShiftedY = y - hipY;
          float sideTiltedX = torsoTiltShiftedX * torsoSideTiltCosine - torsoTiltShiftedY * torsoSideTiltSine;
          float sideTiltedY = torsoTiltShiftedX * torsoSideTiltSine + torsoTiltShiftedY * torsoSideTiltCosine;
          x = lerp(x, sideTiltedX + centerX, torsoBlend * 0.22f);
          y = lerp(y, sideTiltedY + hipY, torsoBlend * 0.22f);
        }
      }

      float leanBlend = smoothstep((y - actorBounds.minY()) / height);
      if (leanBlend > 0.0f) {
        x += pelvisSway * leanBlend * 0.35f;
      }

      transformedX[vertexIndex] = x;
      transformedY[vertexIndex] = y;
      transformedZ[vertexIndex] = z;
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

  private static float cosDegrees(float degrees) {
    return (float) Math.cos(Math.toRadians(degrees));
  }

  private static float sinDegrees(float degrees) {
    return (float) Math.sin(Math.toRadians(degrees));
  }

  private static LocomotionProfile locomotionProfile(ActorAnimationState.LocomotionMode locomotionMode) {
    return switch (locomotionMode) {
      case RUN_FORWARD -> RUN_FORWARD_PROFILE;
      case WALK_BACKWARD -> WALK_BACKWARD_PROFILE;
      case STRAFE_LEFT -> STRAFE_LEFT_PROFILE;
      case STRAFE_RIGHT -> STRAFE_RIGHT_PROFILE;
      case WALK_FORWARD, IDLE -> WALK_FORWARD_PROFILE;
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
