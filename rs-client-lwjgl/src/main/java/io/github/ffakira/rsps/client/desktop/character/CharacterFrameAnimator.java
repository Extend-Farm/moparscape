package io.github.ffakira.rsps.client.desktop.character;

import io.github.ffakira.rsps.cache.AnimationFrameCatalog;
import io.github.ffakira.rsps.cache.AnimationFrameDefinition;
import io.github.ffakira.rsps.cache.AnimationFrameBase;
import io.github.ffakira.rsps.cache.RawModelData;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

final class CharacterFrameAnimator {

  private static final int TRIG_SCALE = 1 << 16;
  private static final int REFERENCE_INTERLEAVE_SENTINEL = 0x98967f;
  private static final int[] EMPTY_SKIN_LABELS = new int[0];
  private static final int[] SINE = buildTrigTable(true);
  private static final int[] COSINE = buildTrigTable(false);
  private static final ThreadLocal<TransformState> TRANSFORM_STATE =
      ThreadLocal.withInitial(TransformState::new);

  private final AnimationFrameCatalog animationFrames;
  private final Map<RawModelData, SkinGroups> skinGroupsByModel = new ConcurrentHashMap<>();
  private final WeakIdentityCache<List<CharacterModelSourceBuilder.PreparedContribution>, CombinedSkinGroups>
      combinedSkinGroupsByContributions = new WeakIdentityCache<>();

  CharacterFrameAnimator(AnimationFrameCatalog animationFrames) {
    this.animationFrames = animationFrames;
  }

  AnimatedContribution apply(CharacterModelSourceBuilder.PreparedContribution contribution, int frameId) {
    return animateContribution(contribution, findFrame(frameId));
  }

  List<AnimatedContribution> apply(List<CharacterModelSourceBuilder.PreparedContribution> contributions, int frameId) {
    return animateContributions(contributions, findFrame(frameId));
  }

  AnimatedContribution applyInterleaved(
      CharacterModelSourceBuilder.PreparedContribution contribution,
      int actionFrameId,
      int movementFrameId,
      int[] interleaveOrder
  ) {
    AnimationFrameDefinition actionFrame = findFrame(actionFrameId);
    if (actionFrame == null) {
      return null;
    }
    AnimationFrameDefinition movementFrame = interleavedMovementFrame(movementFrameId, interleaveOrder);
    if (movementFrame == null) {
      return animateContribution(contribution, actionFrame);
    }
    return animateInterleavedContribution(contribution, actionFrame, movementFrame, interleaveOrder);
  }

  List<AnimatedContribution> applyInterleaved(
      List<CharacterModelSourceBuilder.PreparedContribution> contributions,
      int actionFrameId,
      int movementFrameId,
      int[] interleaveOrder
  ) {
    AnimationFrameDefinition actionFrame = findFrame(actionFrameId);
    if (actionFrame == null) {
      return null;
    }
    AnimationFrameDefinition movementFrame = interleavedMovementFrame(movementFrameId, interleaveOrder);
    if (movementFrame == null) {
      return animateContributions(contributions, actionFrame);
    }
    return animateInterleavedContributions(contributions, actionFrame, movementFrame, interleaveOrder);
  }

  private AnimationFrameDefinition findFrame(int frameId) {
    return animationFrames.find(frameId).orElse(null);
  }

  private AnimationFrameDefinition interleavedMovementFrame(int movementFrameId, int[] interleaveOrder) {
    if (movementFrameId < 0 || interleaveOrder == null) {
      return null;
    }
    return findFrame(movementFrameId);
  }

  private AnimatedContribution animateContribution(
      CharacterModelSourceBuilder.PreparedContribution contribution,
      AnimationFrameDefinition frame
  ) {
    if (frame == null) {
      return null;
    }
    SkinGroups skinGroups = skinGroups(contribution);
    if (skinGroups == null) {
      return null;
    }
    WorkingModel workingModel = newWorkingModel(contribution);
    applyFrame(frame, frame.base(), skinGroups, reusableTransformState(), workingModel);
    return toAnimatedContribution(workingModel);
  }

  private List<AnimatedContribution> animateContributions(
      List<CharacterModelSourceBuilder.PreparedContribution> contributions,
      AnimationFrameDefinition frame
  ) {
    if (frame == null) {
      return null;
    }
    CombinedSkinGroups skinGroups = combinedSkinGroups(contributions);
    if (skinGroups == null) {
      return null;
    }
    WorkingContribution[] workingContributions = newWorkingContributions(contributions);
    applyFrame(frame, frame.base(), skinGroups, reusableTransformState(), workingContributions);
    return toAnimatedContributions(workingContributions);
  }

  private AnimatedContribution animateInterleavedContribution(
      CharacterModelSourceBuilder.PreparedContribution contribution,
      AnimationFrameDefinition actionFrame,
      AnimationFrameDefinition movementFrame,
      int[] interleaveOrder
  ) {
    SkinGroups skinGroups = skinGroups(contribution);
    if (skinGroups == null) {
      return null;
    }
    WorkingModel workingModel = newWorkingModel(contribution);
    AnimationFrameBase actionBase = actionFrame.base();
    TransformState transformState = reusableTransformState();
    applyInterleavedFrames(actionFrame, movementFrame, actionBase, skinGroups, transformState, workingModel, interleaveOrder);
    return toAnimatedContribution(workingModel);
  }

  private List<AnimatedContribution> animateInterleavedContributions(
      List<CharacterModelSourceBuilder.PreparedContribution> contributions,
      AnimationFrameDefinition actionFrame,
      AnimationFrameDefinition movementFrame,
      int[] interleaveOrder
  ) {
    CombinedSkinGroups skinGroups = combinedSkinGroups(contributions);
    if (skinGroups == null) {
      return null;
    }
    WorkingContribution[] workingContributions = newWorkingContributions(contributions);
    AnimationFrameBase actionBase = actionFrame.base();
    TransformState transformState = reusableTransformState();
    applyInterleavedFrames(actionFrame, movementFrame, actionBase, skinGroups, transformState, workingContributions, interleaveOrder);
    return toAnimatedContributions(workingContributions);
  }

  private TransformState reusableTransformState() {
    TransformState transformState = TRANSFORM_STATE.get();
    transformState.reset();
    return transformState;
  }

  private SkinGroups skinGroups(CharacterModelSourceBuilder.PreparedContribution contribution) {
    RawModelData rawModelData = contribution.contribution().rawModelData();
    SkinGroups skinGroups = skinGroupsByModel.computeIfAbsent(rawModelData, CharacterFrameAnimator::buildSkinGroups);
    if (skinGroups.vertexGroups().length == 0 && skinGroups.faceGroups().length == 0) {
      return null;
    }
    return skinGroups;
  }

  private CombinedSkinGroups combinedSkinGroups(List<CharacterModelSourceBuilder.PreparedContribution> contributions) {
    CombinedSkinGroups skinGroups = combinedSkinGroupsByContributions.computeIfAbsent(
        contributions,
        this::buildCombinedSkinGroups
    );
    if (skinGroups.vertexGroups().length == 0 && skinGroups.faceGroups().length == 0) {
      return null;
    }
    return skinGroups;
  }

  private WorkingModel newWorkingModel(CharacterModelSourceBuilder.PreparedContribution contribution) {
    RawModelData rawModelData = contribution.contribution().rawModelData();
    // Frame application mutates the working arrays in place, so each evaluation must clone the
    // cached prepared-model state before applying any transforms.
    return new WorkingModel(
        contribution.modelX().clone(),
        contribution.modelY().clone(),
        contribution.modelZ().clone(),
        rawModelData.faceAlpha()
    );
  }

  private AnimatedContribution toAnimatedContribution(WorkingModel workingModel) {
    return new AnimatedContribution(
        workingModel.modelX(),
        workingModel.modelY(),
        workingModel.modelZ(),
        workingModel.faceAlpha()
    );
  }

  private List<AnimatedContribution> toAnimatedContributions(WorkingContribution[] workingContributions) {
    AnimatedContribution[] animatedContributions = new AnimatedContribution[workingContributions.length];
    for (int contributionIndex = 0; contributionIndex < workingContributions.length; contributionIndex++) {
      WorkingContribution workingContribution = workingContributions[contributionIndex];
      animatedContributions[contributionIndex] = new AnimatedContribution(
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
      SkinGroups skinGroups,
      TransformState transformState,
      WorkingModel workingModel
  ) {
    for (int index = 0; index < frame.transformationCount(); index++) {
      int transformationIndex = frame.transformationIndices()[index];
      applyFrameTransform(frame, base, index, transformationIndex, skinGroups, transformState, workingModel);
    }
  }

  private void applyInterleavedFrames(
      AnimationFrameDefinition actionFrame,
      AnimationFrameDefinition movementFrame,
      AnimationFrameBase actionBase,
      SkinGroups skinGroups,
      TransformState transformState,
      WorkingModel workingModel,
      int[] interleaveOrder
  ) {
    // The reference client's method471 uses the primary action frame's base for both passes and
    // resets the pivot accumulators between them before applying the masked movement transforms.
    applyInterleavedPass(actionFrame, actionBase, skinGroups, transformState, workingModel, interleaveOrder, false);
    transformState.reset();
    applyInterleavedPass(movementFrame, actionBase, skinGroups, transformState, workingModel, interleaveOrder, true);
  }

  private void applyFrame(
      AnimationFrameDefinition frame,
      AnimationFrameBase base,
      CombinedSkinGroups skinGroups,
      TransformState transformState,
      WorkingContribution[] workingContributions
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
      CombinedSkinGroups skinGroups,
      TransformState transformState,
      WorkingContribution[] workingContributions,
      int[] interleaveOrder
  ) {
    applyInterleavedPass(actionFrame, actionBase, skinGroups, transformState, workingContributions, interleaveOrder, false);
    transformState.reset();
    applyInterleavedPass(movementFrame, actionBase, skinGroups, transformState, workingContributions, interleaveOrder, true);
  }

  private void applyInterleavedPass(
      AnimationFrameDefinition frame,
      AnimationFrameBase base,
      SkinGroups skinGroups,
      TransformState transformState,
      WorkingModel workingModel,
      int[] interleaveOrder,
      boolean maskedPass
  ) {
    int interleaveCursor = 0;
    int interleaveIndex = interleaveOrder.length == 0 ? REFERENCE_INTERLEAVE_SENTINEL : interleaveOrder[interleaveCursor++];
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
            workingModel
        );
      }
    }
  }

  private void applyInterleavedPass(
      AnimationFrameDefinition frame,
      AnimationFrameBase base,
      CombinedSkinGroups skinGroups,
      TransformState transformState,
      WorkingContribution[] workingContributions,
      int[] interleaveOrder,
      boolean maskedPass
  ) {
    int interleaveCursor = 0;
    int interleaveIndex = interleaveOrder.length == 0 ? REFERENCE_INTERLEAVE_SENTINEL : interleaveOrder[interleaveCursor++];
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
      SkinGroups skinGroups,
      TransformState transformState,
      WorkingModel workingModel
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
        workingModel
    );
  }

  private void applyFrameTransform(
      AnimationFrameDefinition frame,
      AnimationFrameBase base,
      int frameTransformIndex,
      int transformationIndex,
      CombinedSkinGroups skinGroups,
      TransformState transformState,
      WorkingContribution[] workingContributions
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
      SkinGroups skinGroups,
      TransformState transformState,
      WorkingModel workingModel
  ) {
    int[] modelX = workingModel.modelX();
    int[] modelY = workingModel.modelY();
    int[] modelZ = workingModel.modelZ();
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
          transformState.pivotX += modelX[vertexIndex];
          transformState.pivotY += modelY[vertexIndex];
          transformState.pivotZ += modelZ[vertexIndex];
          pointCount++;
        }
      }
      if (pointCount > 0) {
        transformState.pivotX = transformState.pivotX / pointCount + transformX;
        transformState.pivotY = transformState.pivotY / pointCount + transformY;
        transformState.pivotZ = transformState.pivotZ / pointCount + transformZ;
        return;
      }
      transformState.pivotX = transformX;
      transformState.pivotY = transformY;
      transformState.pivotZ = transformZ;
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
      }
      return;
    }
    if (transformationType == 5) {
      int[] faceAlpha = workingModel.writableFaceAlpha();
      int[][] faceGroups = skinGroups.faceGroups();
      for (int skinLabel : skinLabels) {
        if (skinLabel >= faceGroups.length) {
          continue;
        }
        for (int faceIndex : faceGroups[skinLabel]) {
          faceAlpha[faceIndex] += transformX * 8;
          if (faceAlpha[faceIndex] < 0) {
            faceAlpha[faceIndex] = 0;
          } else if (faceAlpha[faceIndex] > 255) {
            faceAlpha[faceIndex] = 255;
          }
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
      CombinedSkinGroups skinGroups,
      TransformState transformState,
      WorkingContribution[] workingContributions
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
        for (VertexReference vertexReference : skinGroups.vertexGroups()[skinLabel]) {
          WorkingContribution workingContribution = workingContributions[vertexReference.contributionIndex()];
          transformState.pivotX += workingContribution.modelX()[vertexReference.vertexIndex()];
          transformState.pivotY += workingContribution.modelY()[vertexReference.vertexIndex()];
          transformState.pivotZ += workingContribution.modelZ()[vertexReference.vertexIndex()];
          pointCount++;
        }
      }
      if (pointCount > 0) {
        transformState.pivotX = transformState.pivotX / pointCount + transformX;
        transformState.pivotY = transformState.pivotY / pointCount + transformY;
        transformState.pivotZ = transformState.pivotZ / pointCount + transformZ;
        return;
      }
      transformState.pivotX = transformX;
      transformState.pivotY = transformY;
      transformState.pivotZ = transformZ;
      return;
    }
    if (transformationType == 1) {
      VertexReference[][] vertexGroups = skinGroups.vertexGroups();
      for (int skinLabel : skinLabels) {
        if (skinLabel >= vertexGroups.length) {
          continue;
        }
        for (VertexReference vertexReference : vertexGroups[skinLabel]) {
          WorkingContribution workingContribution = workingContributions[vertexReference.contributionIndex()];
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
      VertexReference[][] vertexGroups = skinGroups.vertexGroups();
      for (int skinLabel : skinLabels) {
        if (skinLabel >= vertexGroups.length) {
          continue;
        }
        for (VertexReference vertexReference : vertexGroups[skinLabel]) {
          WorkingContribution workingContribution = workingContributions[vertexReference.contributionIndex()];
          int[] modelX = workingContribution.modelX();
          int[] modelY = workingContribution.modelY();
          int[] modelZ = workingContribution.modelZ();
          int vertexIndex = vertexReference.vertexIndex();
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
      }
      return;
    }
    if (transformationType == 3) {
      VertexReference[][] vertexGroups = skinGroups.vertexGroups();
      for (int skinLabel : skinLabels) {
        if (skinLabel >= vertexGroups.length) {
          continue;
        }
        for (VertexReference vertexReference : vertexGroups[skinLabel]) {
          WorkingContribution workingContribution = workingContributions[vertexReference.contributionIndex()];
          int[] modelX = workingContribution.modelX();
          int[] modelY = workingContribution.modelY();
          int[] modelZ = workingContribution.modelZ();
          int vertexIndex = vertexReference.vertexIndex();
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
      }
      return;
    }
    if (transformationType == 5) {
      FaceReference[][] faceGroups = skinGroups.faceGroups();
      for (int skinLabel : skinLabels) {
        if (skinLabel >= faceGroups.length) {
          continue;
        }
        for (FaceReference faceReference : faceGroups[skinLabel]) {
          WorkingContribution workingContribution = workingContributions[faceReference.contributionIndex()];
          int[] faceAlpha = workingContribution.writableFaceAlpha();
          faceAlpha[faceReference.faceIndex()] += transformX * 8;
          if (faceAlpha[faceReference.faceIndex()] < 0) {
            faceAlpha[faceReference.faceIndex()] = 0;
          } else if (faceAlpha[faceReference.faceIndex()] > 255) {
            faceAlpha[faceReference.faceIndex()] = 255;
          }
        }
      }
    }
  }

  private CombinedSkinGroups buildCombinedSkinGroups(List<CharacterModelSourceBuilder.PreparedContribution> contributions) {
    ArrayList<SkinGroups> contributionSkinGroups = new ArrayList<>(contributions.size());
    int maxVertexLabel = -1;
    int maxFaceLabel = -1;
    for (CharacterModelSourceBuilder.PreparedContribution contribution : contributions) {
      SkinGroups skinGroups = skinGroups(contribution);
      contributionSkinGroups.add(skinGroups);
      if (skinGroups == null) {
        continue;
      }
      maxVertexLabel = Math.max(maxVertexLabel, skinGroups.vertexGroups().length - 1);
      maxFaceLabel = Math.max(maxFaceLabel, skinGroups.faceGroups().length - 1);
    }
    if (maxVertexLabel < 0 && maxFaceLabel < 0) {
      return new CombinedSkinGroups(new VertexReference[0][], new FaceReference[0][]);
    }
    ArrayList<ArrayList<VertexReference>> vertexGroups = new ArrayList<>(Math.max(0, maxVertexLabel + 1));
    for (int label = 0; label <= maxVertexLabel; label++) {
      vertexGroups.add(new ArrayList<>());
    }
    ArrayList<ArrayList<FaceReference>> faceGroups = new ArrayList<>(Math.max(0, maxFaceLabel + 1));
    for (int label = 0; label <= maxFaceLabel; label++) {
      faceGroups.add(new ArrayList<>());
    }
    for (int contributionIndex = 0; contributionIndex < contributions.size(); contributionIndex++) {
      SkinGroups skinGroups = contributionSkinGroups.get(contributionIndex);
      if (skinGroups == null) {
        continue;
      }
      for (int label = 0; label < skinGroups.vertexGroups().length; label++) {
        for (int vertexIndex : skinGroups.vertexGroups()[label]) {
          vertexGroups.get(label).add(new VertexReference(contributionIndex, vertexIndex));
        }
      }
      for (int label = 0; label < skinGroups.faceGroups().length; label++) {
        for (int faceIndex : skinGroups.faceGroups()[label]) {
          faceGroups.get(label).add(new FaceReference(contributionIndex, faceIndex));
        }
      }
    }
    return new CombinedSkinGroups(toVertexReferenceArrays(vertexGroups), toFaceReferenceArrays(faceGroups));
  }

  private WorkingContribution[] newWorkingContributions(List<CharacterModelSourceBuilder.PreparedContribution> contributions) {
    WorkingContribution[] workingContributions = new WorkingContribution[contributions.size()];
    for (int contributionIndex = 0; contributionIndex < contributions.size(); contributionIndex++) {
      CharacterModelSourceBuilder.PreparedContribution contribution = contributions.get(contributionIndex);
      RawModelData rawModelData = contribution.contribution().rawModelData();
      // Like the single-model path, contribution-local arrays must be cloned before any frame
      // transform mutates them in place.
      workingContributions[contributionIndex] = new WorkingContribution(
          contribution.modelX().clone(),
          contribution.modelY().clone(),
          contribution.modelZ().clone(),
          rawModelData.faceAlpha()
      );
    }
    return workingContributions;
  }

  private static SkinGroups buildSkinGroups(RawModelData rawModelData) {
    return new SkinGroups(
        groupIndices(rawModelData.vertexSkins()),
        groupIndices(rawModelData.faceSkins())
    );
  }

  private static int[][] groupIndices(int[] labels) {
    if (labels == null || labels.length == 0) {
      return new int[0][];
    }
    int maxLabel = -1;
    for (int label : labels) {
      maxLabel = Math.max(maxLabel, label);
    }
    if (maxLabel < 0) {
      return new int[0][];
    }
    int[] counts = new int[maxLabel + 1];
    for (int label : labels) {
      counts[label]++;
    }
    int[][] groups = new int[maxLabel + 1][];
    for (int label = 0; label <= maxLabel; label++) {
      groups[label] = new int[counts[label]];
      counts[label] = 0;
    }
    for (int index = 0; index < labels.length; index++) {
      int label = labels[index];
      groups[label][counts[label]++] = index;
    }
    return groups;
  }

  private static int[] buildTrigTable(boolean sine) {
    int[] values = new int[2048];
    for (int index = 0; index < values.length; index++) {
      double angle = index * (Math.PI * 2.0 / values.length);
      values[index] = (int) Math.round((sine ? Math.sin(angle) : Math.cos(angle)) * TRIG_SCALE);
    }
    return values;
  }

  record AnimatedContribution(int[] modelX, int[] modelY, int[] modelZ, int[] faceAlpha) {
  }

  private static final class WorkingModel {
    private final int[] modelX;
    private final int[] modelY;
    private final int[] modelZ;
    private final int[] sourceFaceAlpha;
    private int[] faceAlpha;

    private WorkingModel(int[] modelX, int[] modelY, int[] modelZ, int[] sourceFaceAlpha) {
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

  private static final class WorkingContribution {
    private final int[] modelX;
    private final int[] modelY;
    private final int[] modelZ;
    private final int[] sourceFaceAlpha;
    private int[] faceAlpha;

    private WorkingContribution(int[] modelX, int[] modelY, int[] modelZ, int[] sourceFaceAlpha) {
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

  private record SkinGroups(int[][] vertexGroups, int[][] faceGroups) {
  }

  private record CombinedSkinGroups(VertexReference[][] vertexGroups, FaceReference[][] faceGroups) {
  }

  private record VertexReference(int contributionIndex, int vertexIndex) {
  }

  private record FaceReference(int contributionIndex, int faceIndex) {
  }

  private static VertexReference[][] toVertexReferenceArrays(ArrayList<ArrayList<VertexReference>> groups) {
    VertexReference[][] arrays = new VertexReference[groups.size()][];
    for (int index = 0; index < groups.size(); index++) {
      arrays[index] = groups.get(index).toArray(VertexReference[]::new);
    }
    return arrays;
  }

  private static FaceReference[][] toFaceReferenceArrays(ArrayList<ArrayList<FaceReference>> groups) {
    FaceReference[][] arrays = new FaceReference[groups.size()][];
    for (int index = 0; index < groups.size(); index++) {
      arrays[index] = groups.get(index).toArray(FaceReference[]::new);
    }
    return arrays;
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

  private static final class WeakIdentityCache<K, V> {
    private final ReferenceQueue<K> staleKeys = new ReferenceQueue<>();
    private final Map<IdentityReference<K>, V> values = new HashMap<>();

    private synchronized V computeIfAbsent(K key, Function<? super K, ? extends V> builder) {
      expungeStaleEntries();
      IdentityReference<K> lookup = new StrongIdentityKey<>(key);
      V existing = values.get(lookup);
      if (existing != null) {
        return existing;
      }
      V created = builder.apply(key);
      values.put(new WeakIdentityKey<>(key, staleKeys), created);
      return created;
    }

    @SuppressWarnings("unchecked")
    private void expungeStaleEntries() {
      WeakIdentityKey<K> staleKey;
      while ((staleKey = (WeakIdentityKey<K>) staleKeys.poll()) != null) {
        values.remove(staleKey);
      }
    }
  }

  private sealed interface IdentityReference<K> permits StrongIdentityKey, WeakIdentityKey {
    K referent();

    int identityHash();
  }

  private static final class StrongIdentityKey<K> implements IdentityReference<K> {
    private final K referent;
    private final int identityHash;

    private StrongIdentityKey(K referent) {
      this.referent = referent;
      this.identityHash = System.identityHashCode(referent);
    }

    @Override
    public K referent() {
      return referent;
    }

    @Override
    public int identityHash() {
      return identityHash;
    }

    @Override
    public int hashCode() {
      return identityHash;
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof IdentityReference<?> other && referent == other.referent();
    }
  }

  private static final class WeakIdentityKey<K> extends WeakReference<K> implements IdentityReference<K> {
    private final int identityHash;

    private WeakIdentityKey(K referent, ReferenceQueue<K> staleKeys) {
      super(referent, staleKeys);
      this.identityHash = System.identityHashCode(referent);
    }

    @Override
    public K referent() {
      return get();
    }

    @Override
    public int identityHash() {
      return identityHash;
    }

    @Override
    public int hashCode() {
      return identityHash;
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof IdentityReference<?> other && referent() == other.referent();
    }
  }
}
