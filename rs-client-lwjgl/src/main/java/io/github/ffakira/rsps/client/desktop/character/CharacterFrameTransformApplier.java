package io.github.ffakira.rsps.client.desktop.character;

import io.github.ffakira.rsps.cache.AnimationFrameBase;
import io.github.ffakira.rsps.cache.AnimationFrameDefinition;
import io.github.ffakira.rsps.cache.RawModelData;
import java.util.List;

final class CharacterFrameTransformApplier {

  private static final int TRIG_SCALE = 1 << 16;
  private static final int INTERLEAVE_SENTINEL = 0x98967f;
  private static final int[] EMPTY_SKIN_LABELS = new int[0];
  private static final int[] SINE = buildTrigTable(true);
  private static final int[] COSINE = buildTrigTable(false);
  private static final ThreadLocal<TransformState> TRANSFORM_STATE =
      ThreadLocal.withInitial(TransformState::new);

  CharacterFrameAnimator.AnimatedContribution apply(
      CharacterModelSourceBuilder.PreparedContribution contribution,
      AnimationFrameDefinition frame,
      CharacterSkinGroupCatalog.SkinGroups skinGroups
  ) {
    MutableContribution workingContribution = newWorkingContribution(contribution);
    applyFrame(frame, frame.base(), skinGroups, reusableTransformState(), workingContribution);
    return toAnimatedContribution(workingContribution);
  }

  List<CharacterFrameAnimator.AnimatedContribution> apply(
      List<CharacterModelSourceBuilder.PreparedContribution> contributions,
      AnimationFrameDefinition frame,
      CharacterSkinGroupCatalog.CombinedSkinGroups skinGroups
  ) {
    MutableContribution[] workingContributions = newWorkingContributions(contributions);
    applyFrame(frame, frame.base(), skinGroups, reusableTransformState(), workingContributions);
    return toAnimatedContributions(workingContributions);
  }

  CharacterFrameAnimator.AnimatedContribution applyInterleaved(
      CharacterModelSourceBuilder.PreparedContribution contribution,
      AnimationFrameDefinition actionFrame,
      AnimationFrameDefinition movementFrame,
      int[] interleaveOrder,
      CharacterSkinGroupCatalog.SkinGroups skinGroups
  ) {
    MutableContribution workingContribution = newWorkingContribution(contribution);
    TransformState transformState = reusableTransformState();
    applyInterleavedFrames(
        actionFrame,
        movementFrame,
        actionFrame.base(),
        skinGroups,
        transformState,
        workingContribution,
        interleaveOrder
    );
    return toAnimatedContribution(workingContribution);
  }

  List<CharacterFrameAnimator.AnimatedContribution> applyInterleaved(
      List<CharacterModelSourceBuilder.PreparedContribution> contributions,
      AnimationFrameDefinition actionFrame,
      AnimationFrameDefinition movementFrame,
      int[] interleaveOrder,
      CharacterSkinGroupCatalog.CombinedSkinGroups skinGroups
  ) {
    MutableContribution[] workingContributions = newWorkingContributions(contributions);
    TransformState transformState = reusableTransformState();
    applyInterleavedFrames(
        actionFrame,
        movementFrame,
        actionFrame.base(),
        skinGroups,
        transformState,
        workingContributions,
        interleaveOrder
    );
    return toAnimatedContributions(workingContributions);
  }

  private TransformState reusableTransformState() {
    TransformState transformState = TRANSFORM_STATE.get();
    transformState.reset();
    return transformState;
  }

  private MutableContribution newWorkingContribution(CharacterModelSourceBuilder.PreparedContribution contribution) {
    RawModelData rawModelData = contribution.contribution().rawModelData();
    // Frame application mutates the working arrays in place, so each evaluation must clone the
    // cached prepared-model state before applying any transforms.
    return new MutableContribution(
        contribution.modelX().clone(),
        contribution.modelY().clone(),
        contribution.modelZ().clone(),
        rawModelData.faceAlpha()
    );
  }

  private MutableContribution[] newWorkingContributions(List<CharacterModelSourceBuilder.PreparedContribution> contributions) {
    MutableContribution[] workingContributions = new MutableContribution[contributions.size()];
    for (int contributionIndex = 0; contributionIndex < contributions.size(); contributionIndex++) {
      CharacterModelSourceBuilder.PreparedContribution contribution = contributions.get(contributionIndex);
      RawModelData rawModelData = contribution.contribution().rawModelData();
      // Like the single-model path, contribution-local arrays must be cloned before any frame
      // transform mutates them in place.
      workingContributions[contributionIndex] = new MutableContribution(
          contribution.modelX().clone(),
          contribution.modelY().clone(),
          contribution.modelZ().clone(),
          rawModelData.faceAlpha()
      );
    }
    return workingContributions;
  }

  private CharacterFrameAnimator.AnimatedContribution toAnimatedContribution(MutableContribution workingContribution) {
    return new CharacterFrameAnimator.AnimatedContribution(
        workingContribution.modelX(),
        workingContribution.modelY(),
        workingContribution.modelZ(),
        workingContribution.faceAlpha()
    );
  }

  private List<CharacterFrameAnimator.AnimatedContribution> toAnimatedContributions(
      MutableContribution[] workingContributions
  ) {
    CharacterFrameAnimator.AnimatedContribution[] animatedContributions =
        new CharacterFrameAnimator.AnimatedContribution[workingContributions.length];
    for (int contributionIndex = 0; contributionIndex < workingContributions.length; contributionIndex++) {
      MutableContribution workingContribution = workingContributions[contributionIndex];
      animatedContributions[contributionIndex] = new CharacterFrameAnimator.AnimatedContribution(
          workingContribution.modelX(),
          workingContribution.modelY(),
          workingContribution.modelZ(),
          workingContribution.faceAlpha()
      );
    }
    return List.of(animatedContributions);
  }

  private void applyFrame(
      AnimationFrameDefinition frame,
      AnimationFrameBase base,
      CharacterSkinGroupCatalog.SkinGroups skinGroups,
      TransformState transformState,
      MutableContribution workingContribution
  ) {
    for (int index = 0; index < frame.transformationCount(); index++) {
      int transformationIndex = frame.transformationIndices()[index];
      applyFrameTransform(frame, base, index, transformationIndex, skinGroups, transformState, workingContribution);
    }
  }

  private void applyInterleavedFrames(
      AnimationFrameDefinition actionFrame,
      AnimationFrameDefinition movementFrame,
      AnimationFrameBase actionBase,
      CharacterSkinGroupCatalog.SkinGroups skinGroups,
      TransformState transformState,
      MutableContribution workingContribution,
      int[] interleaveOrder
  ) {
    // The reference client's method471 uses the primary action frame's base for both passes and
    // resets the pivot accumulators between them before applying the masked movement transforms.
    applyInterleavedPass(actionFrame, actionBase, skinGroups, transformState, workingContribution, interleaveOrder, false);
    transformState.reset();
    applyInterleavedPass(movementFrame, actionBase, skinGroups, transformState, workingContribution, interleaveOrder, true);
  }

  private void applyFrame(
      AnimationFrameDefinition frame,
      AnimationFrameBase base,
      CharacterSkinGroupCatalog.CombinedSkinGroups skinGroups,
      TransformState transformState,
      MutableContribution[] workingContributions
  ) {
    for (int index = 0; index < frame.transformationCount(); index++) {
      int transformationIndex = frame.transformationIndices()[index];
      applyFrameTransform(frame, base, index, transformationIndex, skinGroups, transformState, workingContributions);
    }
  }

  private void applyInterleavedFrames(
      AnimationFrameDefinition actionFrame,
      AnimationFrameDefinition movementFrame,
      AnimationFrameBase actionBase,
      CharacterSkinGroupCatalog.CombinedSkinGroups skinGroups,
      TransformState transformState,
      MutableContribution[] workingContributions,
      int[] interleaveOrder
  ) {
    applyInterleavedPass(actionFrame, actionBase, skinGroups, transformState, workingContributions, interleaveOrder, false);
    transformState.reset();
    applyInterleavedPass(movementFrame, actionBase, skinGroups, transformState, workingContributions, interleaveOrder, true);
  }

  private void applyInterleavedPass(
      AnimationFrameDefinition frame,
      AnimationFrameBase base,
      CharacterSkinGroupCatalog.SkinGroups skinGroups,
      TransformState transformState,
      MutableContribution workingContribution,
      int[] interleaveOrder,
      boolean maskedPass
  ) {
    int interleaveCursor = 0;
    int interleaveIndex = interleaveOrder.length == 0 ? INTERLEAVE_SENTINEL : interleaveOrder[interleaveCursor++];
    for (int index = 0; index < frame.transformationCount(); index++) {
      int transformationIndex = frame.transformationIndices()[index];
      while (transformationIndex > interleaveIndex && interleaveCursor < interleaveOrder.length) {
        interleaveIndex = interleaveOrder[interleaveCursor++];
      }
      int transformationType = transformationType(base, transformationIndex);
      if (transformationType < 0) {
        continue;
      }
      boolean matchesInterleave = transformationIndex == interleaveIndex;
      if (matchesInterleave == maskedPass || transformationType == 0) {
        applyTransform(
            transformationType,
            skinLabels(base, transformationIndex),
            frame.transformX()[index],
            frame.transformY()[index],
            frame.transformZ()[index],
            skinGroups,
            transformState,
            workingContribution
        );
      }
    }
  }

  private void applyInterleavedPass(
      AnimationFrameDefinition frame,
      AnimationFrameBase base,
      CharacterSkinGroupCatalog.CombinedSkinGroups skinGroups,
      TransformState transformState,
      MutableContribution[] workingContributions,
      int[] interleaveOrder,
      boolean maskedPass
  ) {
    int interleaveCursor = 0;
    int interleaveIndex = interleaveOrder.length == 0 ? INTERLEAVE_SENTINEL : interleaveOrder[interleaveCursor++];
    for (int index = 0; index < frame.transformationCount(); index++) {
      int transformationIndex = frame.transformationIndices()[index];
      while (transformationIndex > interleaveIndex && interleaveCursor < interleaveOrder.length) {
        interleaveIndex = interleaveOrder[interleaveCursor++];
      }
      int transformationType = transformationType(base, transformationIndex);
      if (transformationType < 0) {
        continue;
      }
      boolean matchesInterleave = transformationIndex == interleaveIndex;
      if (matchesInterleave == maskedPass || transformationType == 0) {
        applyTransform(
            transformationType,
            skinLabels(base, transformationIndex),
            frame.transformX()[index],
            frame.transformY()[index],
            frame.transformZ()[index],
            skinGroups,
            transformState,
            workingContributions
        );
      }
    }
  }

  private void applyFrameTransform(
      AnimationFrameDefinition frame,
      AnimationFrameBase base,
      int frameTransformIndex,
      int transformationIndex,
      CharacterSkinGroupCatalog.SkinGroups skinGroups,
      TransformState transformState,
      MutableContribution workingContribution
  ) {
    int transformationType = transformationType(base, transformationIndex);
    if (transformationType < 0) {
      return;
    }
    applyTransform(
        transformationType,
        skinLabels(base, transformationIndex),
        frame.transformX()[frameTransformIndex],
        frame.transformY()[frameTransformIndex],
        frame.transformZ()[frameTransformIndex],
        skinGroups,
        transformState,
        workingContribution
    );
  }

  private void applyFrameTransform(
      AnimationFrameDefinition frame,
      AnimationFrameBase base,
      int frameTransformIndex,
      int transformationIndex,
      CharacterSkinGroupCatalog.CombinedSkinGroups skinGroups,
      TransformState transformState,
      MutableContribution[] workingContributions
  ) {
    int transformationType = transformationType(base, transformationIndex);
    if (transformationType < 0) {
      return;
    }
    applyTransform(
        transformationType,
        skinLabels(base, transformationIndex),
        frame.transformX()[frameTransformIndex],
        frame.transformY()[frameTransformIndex],
        frame.transformZ()[frameTransformIndex],
        skinGroups,
        transformState,
        workingContributions
    );
  }

  private int transformationType(AnimationFrameBase base, int transformationIndex) {
    return transformationIndex < 0 || transformationIndex >= base.transformationTypes().length
        ? -1
        : base.transformationTypes()[transformationIndex];
  }

  private int[] skinLabels(AnimationFrameBase base, int transformationIndex) {
    return transformationIndex < 0 || transformationIndex >= base.skinList().length
        ? EMPTY_SKIN_LABELS
        : base.skinList()[transformationIndex];
  }

  private void applyTransform(
      int transformationType,
      int[] skinLabels,
      int transformX,
      int transformY,
      int transformZ,
      CharacterSkinGroupCatalog.SkinGroups skinGroups,
      TransformState transformState,
      MutableContribution workingContribution
  ) {
    int[] modelX = workingContribution.modelX();
    int[] modelY = workingContribution.modelY();
    int[] modelZ = workingContribution.modelZ();
    if (transformationType == 0) {
      int pointCount = 0;
      transformState.pivotX = 0;
      transformState.pivotY = 0;
      transformState.pivotZ = 0;
      for (int skinLabel : skinLabels) {
        if (skinLabel >= skinGroups.vertexGroups().length) {
          continue;
        }
        int[] vertexGroup = skinGroups.vertexGroups()[skinLabel];
        for (int vertexIndex : vertexGroup) {
          accumulatePivot(modelX, modelY, modelZ, vertexIndex, transformState);
          pointCount++;
        }
      }
      updatePivot(transformState, pointCount, transformX, transformY, transformZ);
      return;
    }
    if (transformationType == 1) {
      int[][] vertexGroups = skinGroups.vertexGroups();
      for (int skinLabel : skinLabels) {
        if (skinLabel >= vertexGroups.length) {
          continue;
        }
        for (int vertexIndex : vertexGroups[skinLabel]) {
          modelX[vertexIndex] += transformX;
          modelY[vertexIndex] += transformY;
          modelZ[vertexIndex] += transformZ;
        }
      }
      return;
    }
    if (transformationType == 2) {
      int angleX = (transformX & 0xff) * 8;
      int angleY = (transformY & 0xff) * 8;
      int angleZ = (transformZ & 0xff) * 8;
      int[][] vertexGroups = skinGroups.vertexGroups();
      for (int skinLabel : skinLabels) {
        if (skinLabel >= vertexGroups.length) {
          continue;
        }
        for (int vertexIndex : vertexGroups[skinLabel]) {
          rotateVertex(modelX, modelY, modelZ, vertexIndex, transformState, angleX, angleY, angleZ);
        }
      }
      return;
    }
    if (transformationType == 3) {
      int[][] vertexGroups = skinGroups.vertexGroups();
      for (int skinLabel : skinLabels) {
        if (skinLabel >= vertexGroups.length) {
          continue;
        }
        for (int vertexIndex : vertexGroups[skinLabel]) {
          scaleVertex(modelX, modelY, modelZ, vertexIndex, transformState, transformX, transformY, transformZ);
        }
      }
      return;
    }
    if (transformationType == 5) {
      int[] faceAlpha = workingContribution.writableFaceAlpha();
      int[][] faceGroups = skinGroups.faceGroups();
      for (int skinLabel : skinLabels) {
        if (skinLabel >= faceGroups.length) {
          continue;
        }
        for (int faceIndex : faceGroups[skinLabel]) {
          faceAlpha[faceIndex] = clampFaceAlpha(faceAlpha[faceIndex] + transformX * 8);
        }
      }
    }
  }

  private void applyTransform(
      int transformationType,
      int[] skinLabels,
      int transformX,
      int transformY,
      int transformZ,
      CharacterSkinGroupCatalog.CombinedSkinGroups skinGroups,
      TransformState transformState,
      MutableContribution[] workingContributions
  ) {
    if (transformationType == 0) {
      int pointCount = 0;
      transformState.pivotX = 0;
      transformState.pivotY = 0;
      transformState.pivotZ = 0;
      for (int skinLabel : skinLabels) {
        if (skinLabel >= skinGroups.vertexGroups().length) {
          continue;
        }
        for (CharacterSkinGroupCatalog.VertexReference vertexReference : skinGroups.vertexGroups()[skinLabel]) {
          MutableContribution workingContribution = workingContributions[vertexReference.contributionIndex()];
          transformState.pivotX += workingContribution.modelX()[vertexReference.vertexIndex()];
          transformState.pivotY += workingContribution.modelY()[vertexReference.vertexIndex()];
          transformState.pivotZ += workingContribution.modelZ()[vertexReference.vertexIndex()];
          pointCount++;
        }
      }
      updatePivot(transformState, pointCount, transformX, transformY, transformZ);
      return;
    }
    if (transformationType == 1) {
      CharacterSkinGroupCatalog.VertexReference[][] vertexGroups = skinGroups.vertexGroups();
      for (int skinLabel : skinLabels) {
        if (skinLabel >= vertexGroups.length) {
          continue;
        }
        for (CharacterSkinGroupCatalog.VertexReference vertexReference : vertexGroups[skinLabel]) {
          MutableContribution workingContribution = workingContributions[vertexReference.contributionIndex()];
          workingContribution.modelX()[vertexReference.vertexIndex()] += transformX;
          workingContribution.modelY()[vertexReference.vertexIndex()] += transformY;
          workingContribution.modelZ()[vertexReference.vertexIndex()] += transformZ;
        }
      }
      return;
    }
    if (transformationType == 2) {
      int angleX = (transformX & 0xff) * 8;
      int angleY = (transformY & 0xff) * 8;
      int angleZ = (transformZ & 0xff) * 8;
      CharacterSkinGroupCatalog.VertexReference[][] vertexGroups = skinGroups.vertexGroups();
      for (int skinLabel : skinLabels) {
        if (skinLabel >= vertexGroups.length) {
          continue;
        }
        for (CharacterSkinGroupCatalog.VertexReference vertexReference : vertexGroups[skinLabel]) {
          MutableContribution workingContribution = workingContributions[vertexReference.contributionIndex()];
          int[] modelX = workingContribution.modelX();
          int[] modelY = workingContribution.modelY();
          int[] modelZ = workingContribution.modelZ();
          int vertexIndex = vertexReference.vertexIndex();
          rotateVertex(modelX, modelY, modelZ, vertexIndex, transformState, angleX, angleY, angleZ);
        }
      }
      return;
    }
    if (transformationType == 3) {
      CharacterSkinGroupCatalog.VertexReference[][] vertexGroups = skinGroups.vertexGroups();
      for (int skinLabel : skinLabels) {
        if (skinLabel >= vertexGroups.length) {
          continue;
        }
        for (CharacterSkinGroupCatalog.VertexReference vertexReference : vertexGroups[skinLabel]) {
          MutableContribution workingContribution = workingContributions[vertexReference.contributionIndex()];
          int[] modelX = workingContribution.modelX();
          int[] modelY = workingContribution.modelY();
          int[] modelZ = workingContribution.modelZ();
          int vertexIndex = vertexReference.vertexIndex();
          scaleVertex(modelX, modelY, modelZ, vertexIndex, transformState, transformX, transformY, transformZ);
        }
      }
      return;
    }
    if (transformationType == 5) {
      CharacterSkinGroupCatalog.FaceReference[][] faceGroups = skinGroups.faceGroups();
      for (int skinLabel : skinLabels) {
        if (skinLabel >= faceGroups.length) {
          continue;
        }
        for (CharacterSkinGroupCatalog.FaceReference faceReference : faceGroups[skinLabel]) {
          MutableContribution workingContribution = workingContributions[faceReference.contributionIndex()];
          int[] faceAlpha = workingContribution.writableFaceAlpha();
          faceAlpha[faceReference.faceIndex()] =
              clampFaceAlpha(faceAlpha[faceReference.faceIndex()] + transformX * 8);
        }
      }
    }
  }

  private static int[] buildTrigTable(boolean sine) {
    int[] values = new int[2048];
    for (int index = 0; index < values.length; index++) {
      double angle = index * (Math.PI * 2.0 / values.length);
      values[index] = (int) Math.round((sine ? Math.sin(angle) : Math.cos(angle)) * TRIG_SCALE);
    }
    return values;
  }

  private static void accumulatePivot(
      int[] modelX,
      int[] modelY,
      int[] modelZ,
      int vertexIndex,
      TransformState transformState
  ) {
    transformState.pivotX += modelX[vertexIndex];
    transformState.pivotY += modelY[vertexIndex];
    transformState.pivotZ += modelZ[vertexIndex];
  }

  private static void updatePivot(
      TransformState transformState,
      int pointCount,
      int transformX,
      int transformY,
      int transformZ
  ) {
    if (pointCount > 0) {
      transformState.pivotX = transformState.pivotX / pointCount + transformX;
      transformState.pivotY = transformState.pivotY / pointCount + transformY;
      transformState.pivotZ = transformState.pivotZ / pointCount + transformZ;
      return;
    }
    transformState.pivotX = transformX;
    transformState.pivotY = transformY;
    transformState.pivotZ = transformZ;
  }

  private static void rotateVertex(
      int[] modelX,
      int[] modelY,
      int[] modelZ,
      int vertexIndex,
      TransformState transformState,
      int angleX,
      int angleY,
      int angleZ
  ) {
    modelX[vertexIndex] -= transformState.pivotX;
    modelY[vertexIndex] -= transformState.pivotY;
    modelZ[vertexIndex] -= transformState.pivotZ;
    if (angleZ != 0) {
      int sin = SINE[angleZ];
      int cos = COSINE[angleZ];
      int rotatedX = modelY[vertexIndex] * sin + modelX[vertexIndex] * cos >> 16;
      modelY[vertexIndex] = modelY[vertexIndex] * cos - modelX[vertexIndex] * sin >> 16;
      modelX[vertexIndex] = rotatedX;
    }
    if (angleX != 0) {
      int sin = SINE[angleX];
      int cos = COSINE[angleX];
      int rotatedY = modelY[vertexIndex] * cos - modelZ[vertexIndex] * sin >> 16;
      modelZ[vertexIndex] = modelY[vertexIndex] * sin + modelZ[vertexIndex] * cos >> 16;
      modelY[vertexIndex] = rotatedY;
    }
    if (angleY != 0) {
      int sin = SINE[angleY];
      int cos = COSINE[angleY];
      int rotatedZ = modelZ[vertexIndex] * sin + modelX[vertexIndex] * cos >> 16;
      modelZ[vertexIndex] = modelZ[vertexIndex] * cos - modelX[vertexIndex] * sin >> 16;
      modelX[vertexIndex] = rotatedZ;
    }
    modelX[vertexIndex] += transformState.pivotX;
    modelY[vertexIndex] += transformState.pivotY;
    modelZ[vertexIndex] += transformState.pivotZ;
  }

  private static void scaleVertex(
      int[] modelX,
      int[] modelY,
      int[] modelZ,
      int vertexIndex,
      TransformState transformState,
      int transformX,
      int transformY,
      int transformZ
  ) {
    modelX[vertexIndex] -= transformState.pivotX;
    modelY[vertexIndex] -= transformState.pivotY;
    modelZ[vertexIndex] -= transformState.pivotZ;
    modelX[vertexIndex] = modelX[vertexIndex] * transformX / 128;
    modelY[vertexIndex] = modelY[vertexIndex] * transformY / 128;
    modelZ[vertexIndex] = modelZ[vertexIndex] * transformZ / 128;
    modelX[vertexIndex] += transformState.pivotX;
    modelY[vertexIndex] += transformState.pivotY;
    modelZ[vertexIndex] += transformState.pivotZ;
  }

  private static int clampFaceAlpha(int faceAlpha) {
    return Math.max(0, Math.min(255, faceAlpha));
  }

  private static final class MutableContribution {
    private final int[] modelX;
    private final int[] modelY;
    private final int[] modelZ;
    private final int[] sourceFaceAlpha;
    private int[] faceAlpha;

    private MutableContribution(int[] modelX, int[] modelY, int[] modelZ, int[] sourceFaceAlpha) {
      this.modelX = modelX;
      this.modelY = modelY;
      this.modelZ = modelZ;
      this.sourceFaceAlpha = sourceFaceAlpha;
      this.faceAlpha = sourceFaceAlpha;
    }

    private int[] modelX() {
      return modelX;
    }

    private int[] modelY() {
      return modelY;
    }

    private int[] modelZ() {
      return modelZ;
    }

    private int[] faceAlpha() {
      return faceAlpha;
    }

    private int[] writableFaceAlpha() {
      if (faceAlpha == sourceFaceAlpha) {
        faceAlpha = sourceFaceAlpha.clone();
      }
      return faceAlpha;
    }
  }

  private static final class TransformState {
    private int pivotX;
    private int pivotY;
    private int pivotZ;

    private void reset() {
      pivotX = 0;
      pivotY = 0;
      pivotZ = 0;
    }
  }
}
