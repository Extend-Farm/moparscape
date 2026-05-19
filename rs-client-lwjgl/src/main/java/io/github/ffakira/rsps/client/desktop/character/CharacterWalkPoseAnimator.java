package io.github.ffakira.rsps.client.desktop.character;

final class CharacterWalkPoseAnimator {

  private static final float WALK_ARM_SWING_DEGREES = 14.0f;
  private static final float WALK_LEG_SWING_DEGREES = 18.0f;
  private static final float WALK_BODY_BOB = 0.010f;
  private static final float WALK_TORSO_TWIST_DEGREES = 2.2f;
  private static final float WALK_FORWARD_LEAN_DEGREES = 1.6f;
  private static final float WALK_FOOT_LIFT = 0.028f;
  private static final float WALK_PELVIS_SWAY = 0.008f;

  private CharacterWalkPoseAnimator() {
  }

  static void apply(
      float[][] transformedVertices,
      CharacterModelAssembler.ActorBounds actorBounds,
      ActorAnimationState animationState
  ) {
    float strideWeight = animationState.strideWeight();
    float swing = (float) Math.sin(animationState.walkCycleRadians());
    float counterSwing = (float) Math.cos(animationState.walkCycleRadians());
    float bob = (float) Math.abs(Math.cos(animationState.walkCycleRadians() * 2.0f)) * WALK_BODY_BOB * strideWeight;
    float torsoTwist = swing * WALK_TORSO_TWIST_DEGREES * strideWeight;
    float pelvisSway = counterSwing * WALK_PELVIS_SWAY * strideWeight;
    float forwardLean = (0.65f + 0.35f * Math.abs(counterSwing)) * WALK_FORWARD_LEAN_DEGREES * strideWeight;
    float centerX = actorBounds.centerX();
    float centerZ = actorBounds.centerZ();
    float width = Math.max(0.001f, actorBounds.maxX() - actorBounds.minX());
    float height = Math.max(0.001f, actorBounds.maxY() - actorBounds.minY());
    float kneeY = actorBounds.minY() + height * 0.28f;
    float hipY = actorBounds.minY() + height * 0.49f;
    float shoulderY = actorBounds.minY() + height * 0.77f;
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
        float[] rotated = rotateAroundX(y, z, hipY, centerZ, side * swing * WALK_LEG_SWING_DEGREES * strideWeight);
        y = lerp(y, rotated[0], legBlend);
        z = lerp(z, rotated[1], legBlend);
        if (y < kneeY) {
          float liftBlend = smoothstep((kneeY - y) / Math.max(0.001f, kneeY - actorBounds.minY()));
          y += Math.max(0.0f, -side * swing) * WALK_FOOT_LIFT * liftBlend * strideWeight;
          x += side * pelvisSway * liftBlend;
        }
      } else {
        float armBlend = smoothstep((Math.abs(xOffset) - armBlendStart) / armBlendRange)
            * smoothstep((y - hipY) / Math.max(0.001f, shoulderY - hipY));
        if (armBlend > 0.0f) {
          float[] rotated = rotateAroundX(y, z, shoulderY, centerZ, -side * swing * WALK_ARM_SWING_DEGREES * strideWeight);
          y = lerp(y, rotated[0], armBlend);
          z = lerp(z, rotated[1], armBlend);
        }
      }

      float torsoBlend = smoothstep((y - hipY) / Math.max(0.001f, actorBounds.maxY() - hipY));
      if (torsoBlend > 0.0f) {
        float[] twisted = rotateAroundY(x, z, centerX, centerZ, torsoTwist);
        x = lerp(x, twisted[0], torsoBlend * 0.55f);
        z = lerp(z, twisted[1], torsoBlend * 0.55f);
        float[] leaned = rotateAroundX(y, z, hipY, centerZ, forwardLean);
        y = lerp(y, leaned[0], torsoBlend * 0.38f);
        z = lerp(z, leaned[1], torsoBlend * 0.38f);
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

  private static float smoothstep(float value) {
    float clamped = Math.max(0.0f, Math.min(1.0f, value));
    return clamped * clamped * (3.0f - 2.0f * clamped);
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
}
