package io.github.ffakira.rsps.client.desktop.character;

import io.github.ffakira.rsps.cache.AnimationFrameCatalog;
import io.github.ffakira.rsps.cache.AnimationFrameDefinition;
import io.github.ffakira.rsps.cache.AnimationFrameBase;
import io.github.ffakira.rsps.cache.RawModelData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class CharacterFrameAnimator {

  private static final int TRIG_SCALE = 1 << 16;
  private static final int REFERENCE_INTERLEAVE_SENTINEL = 0x98967f;
  private static final int[] EMPTY_SKIN_LABELS = new int[0];
  private static final int[] SINE = buildTrigTable(true);
  private static final int[] COSINE = buildTrigTable(false);

  private final AnimationFrameCatalog animationFrames;
  private final Map<RawModelData, SkinGroups> skinGroupsByModel = new ConcurrentHashMap<>();
  private final Map<List<CharacterModelSourceBuilder.PreparedContribution>, CombinedSkinGroups> combinedSkinGroupsByContributions =
      Collections.synchronizedMap(new IdentityHashMap<>());

  CharacterFrameAnimator(AnimationFrameCatalog animationFrames) {
    this.animationFrames = animationFrames;
  }

  AnimatedContribution apply(CharacterModelSourceBuilder.PreparedContribution contribution, int frameId) {
    AnimationFrameDefinition frame = animationFrames.find(frameId).orElse(null);
    if (frame == null) {
      return null;
    }
    SkinGroups skinGroups = skinGroups(contribution);
    if (skinGroups == null) {
      return null;
    }
    WorkingModel workingModel = newWorkingModel(contribution);
    TransformState transformState = new TransformState();
    applyFrame(frame, frame.base(), skinGroups, transformState, workingModel);
    return toAnimatedContribution(workingModel);
  }

  List<AnimatedContribution> apply(List<CharacterModelSourceBuilder.PreparedContribution> contributions, int frameId) {
    AnimationFrameDefinition frame = animationFrames.find(frameId).orElse(null);
    if (frame == null) {
      return null;
    }
    CombinedSkinGroups skinGroups = combinedSkinGroups(contributions);
    if (skinGroups == null) {
      return null;
    }
    WorkingContribution[] workingContributions = newWorkingContributions(contributions);
    TransformState transformState = new TransformState();
    applyFrame(frame, frame.base(), skinGroups, transformState, workingContributions);
    return toAnimatedContributions(workingContributions);
  }

  AnimatedContribution applyInterleaved(
      CharacterModelSourceBuilder.PreparedContribution contribution,
      int actionFrameId,
      int movementFrameId,
      int[] interleaveOrder
  ) {
    AnimationFrameDefinition actionFrame = animationFrames.find(actionFrameId).orElse(null);
    if (actionFrame == null) {
      return null;
    }
    if (movementFrameId < 0 || interleaveOrder == null) {
      return apply(contribution, actionFrameId);
    }
    AnimationFrameDefinition movementFrame = animationFrames.find(movementFrameId).orElse(null);
    if (movementFrame == null) {
      return apply(contribution, actionFrameId);
    }
    SkinGroups skinGroups = skinGroups(contribution);
    if (skinGroups == null) {
      return null;
    }
    WorkingModel workingModel = newWorkingModel(contribution);
    AnimationFrameBase actionBase = actionFrame.base();
    TransformState transformState = new TransformState();
    // The reference client's method471 uses the primary action frame's base for both passes and
    // resets the pivot accumulators between them before applying the masked movement transforms.
    applyInterleavedPass(actionFrame, actionBase, skinGroups, transformState, workingModel, interleaveOrder, false);
    transformState.reset();
    applyInterleavedPass(movementFrame, actionBase, skinGroups, transformState, workingModel, interleaveOrder, true);
    return toAnimatedContribution(workingModel);
  }

  List<AnimatedContribution> applyInterleaved(
      List<CharacterModelSourceBuilder.PreparedContribution> contributions,
      int actionFrameId,
      int movementFrameId,
      int[] interleaveOrder
  ) {
    AnimationFrameDefinition actionFrame = animationFrames.find(actionFrameId).orElse(null);
    if (actionFrame == null) {
      return null;
    }
    if (movementFrameId < 0 || interleaveOrder == null) {
      return apply(contributions, actionFrameId);
    }
    AnimationFrameDefinition movementFrame = animationFrames.find(movementFrameId).orElse(null);
    if (movementFrame == null) {
      return apply(contributions, actionFrameId);
    }
    CombinedSkinGroups skinGroups = combinedSkinGroups(contributions);
    if (skinGroups == null) {
      return null;
    }
    WorkingContribution[] workingContributions = newWorkingContributions(contributions);
    AnimationFrameBase actionBase = actionFrame.base();
    TransformState transformState = new TransformState();
    applyInterleavedPass(actionFrame, actionBase, skinGroups, transformState, workingContributions, interleaveOrder, false);
    transformState.reset();
    applyInterleavedPass(movementFrame, actionBase, skinGroups, transformState, workingContributions, interleaveOrder, true);
    return toAnimatedContributions(workingContributions);
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
    return new WorkingModel(
        contribution.modelX().clone(),
        contribution.modelY().clone(),
        contribution.modelZ().clone(),
        rawModelData.faceAlpha().clone()
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
    ArrayList<AnimatedContribution> animatedContributions = new ArrayList<>(workingContributions.length);
    for (WorkingContribution workingContribution : workingContributions) {
      animatedContributions.add(new AnimatedContribution(
          workingContribution.modelX(),
          workingContribution.modelY(),
          workingContribution.modelZ(),
          workingContribution.faceAlpha()
      ));
    }
    return List.copyOf(animatedContributions);
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
            workingModel.modelX(),
            workingModel.modelY(),
            workingModel.modelZ(),
            workingModel.faceAlpha()
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
        workingModel.modelX(),
        workingModel.modelY(),
        workingModel.modelZ(),
        workingModel.faceAlpha()
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
      int[] modelX,
      int[] modelY,
      int[] modelZ,
      int[] faceAlpha
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
      forEachVertex(skinLabels, skinGroups, vertexIndex -> {
        modelX[vertexIndex] += transformX;
        modelY[vertexIndex] += transformY;
        modelZ[vertexIndex] += transformZ;
      });
      return;
    }
    if (transformationType == 2) {
      int angleX = (transformX & 0xff) * 8;
      int angleY = (transformY & 0xff) * 8;
      int angleZ = (transformZ & 0xff) * 8;
      forEachVertex(skinLabels, skinGroups, vertexIndex -> {
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
      });
      return;
    }
    if (transformationType == 3) {
      forEachVertex(skinLabels, skinGroups, vertexIndex -> {
        modelX[vertexIndex] -= transformState.pivotX;
        modelY[vertexIndex] -= transformState.pivotY;
        modelZ[vertexIndex] -= transformState.pivotZ;
        modelX[vertexIndex] = modelX[vertexIndex] * transformX / 128;
        modelY[vertexIndex] = modelY[vertexIndex] * transformY / 128;
        modelZ[vertexIndex] = modelZ[vertexIndex] * transformZ / 128;
        modelX[vertexIndex] += transformState.pivotX;
        modelY[vertexIndex] += transformState.pivotY;
        modelZ[vertexIndex] += transformState.pivotZ;
      });
      return;
    }
    if (transformationType == 5) {
      forEachFace(skinLabels, skinGroups, faceIndex -> {
        faceAlpha[faceIndex] += transformX * 8;
        if (faceAlpha[faceIndex] < 0) {
          faceAlpha[faceIndex] = 0;
        } else if (faceAlpha[faceIndex] > 255) {
          faceAlpha[faceIndex] = 255;
        }
      });
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
      forEachVertex(skinLabels, skinGroups, vertexReference -> {
        WorkingContribution workingContribution = workingContributions[vertexReference.contributionIndex()];
        workingContribution.modelX()[vertexReference.vertexIndex()] += transformX;
        workingContribution.modelY()[vertexReference.vertexIndex()] += transformY;
        workingContribution.modelZ()[vertexReference.vertexIndex()] += transformZ;
      });
      return;
    }
    if (transformationType == 2) {
      int angleX = (transformX & 0xff) * 8;
      int angleY = (transformY & 0xff) * 8;
      int angleZ = (transformZ & 0xff) * 8;
      forEachVertex(skinLabels, skinGroups, vertexReference -> {
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
      });
      return;
    }
    if (transformationType == 3) {
      forEachVertex(skinLabels, skinGroups, vertexReference -> {
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
      });
      return;
    }
    if (transformationType == 5) {
      forEachFace(skinLabels, skinGroups, faceReference -> {
        WorkingContribution workingContribution = workingContributions[faceReference.contributionIndex()];
        int[] faceAlpha = workingContribution.faceAlpha();
        faceAlpha[faceReference.faceIndex()] += transformX * 8;
        if (faceAlpha[faceReference.faceIndex()] < 0) {
          faceAlpha[faceReference.faceIndex()] = 0;
        } else if (faceAlpha[faceReference.faceIndex()] > 255) {
          faceAlpha[faceReference.faceIndex()] = 255;
        }
      });
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
      workingContributions[contributionIndex] = new WorkingContribution(
          contribution.modelX().clone(),
          contribution.modelY().clone(),
          contribution.modelZ().clone(),
          rawModelData.faceAlpha().clone()
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

  private static void forEachVertex(int[] skinLabels, SkinGroups skinGroups, IntConsumer consumer) {
    for (int skinLabel : skinLabels) {
      if (skinLabel >= skinGroups.vertexGroups().length) {
        continue;
      }
      for (int vertexIndex : skinGroups.vertexGroups()[skinLabel]) {
        consumer.accept(vertexIndex);
      }
    }
  }

  private static void forEachFace(int[] skinLabels, SkinGroups skinGroups, IntConsumer consumer) {
    for (int skinLabel : skinLabels) {
      if (skinLabel >= skinGroups.faceGroups().length) {
        continue;
      }
      for (int faceIndex : skinGroups.faceGroups()[skinLabel]) {
        consumer.accept(faceIndex);
      }
    }
  }

  private static void forEachVertex(int[] skinLabels, CombinedSkinGroups skinGroups, VertexReferenceConsumer consumer) {
    for (int skinLabel : skinLabels) {
      if (skinLabel >= skinGroups.vertexGroups().length) {
        continue;
      }
      for (VertexReference vertexReference : skinGroups.vertexGroups()[skinLabel]) {
        consumer.accept(vertexReference);
      }
    }
  }

  private static void forEachFace(int[] skinLabels, CombinedSkinGroups skinGroups, FaceReferenceConsumer consumer) {
    for (int skinLabel : skinLabels) {
      if (skinLabel >= skinGroups.faceGroups().length) {
        continue;
      }
      for (FaceReference faceReference : skinGroups.faceGroups()[skinLabel]) {
        consumer.accept(faceReference);
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

  record AnimatedContribution(int[] modelX, int[] modelY, int[] modelZ, int[] faceAlpha) {
  }

  private record WorkingModel(int[] modelX, int[] modelY, int[] modelZ, int[] faceAlpha) {
  }

  private record WorkingContribution(int[] modelX, int[] modelY, int[] modelZ, int[] faceAlpha) {
  }

  private record SkinGroups(int[][] vertexGroups, int[][] faceGroups) {
  }

  private record CombinedSkinGroups(VertexReference[][] vertexGroups, FaceReference[][] faceGroups) {
  }

  private record VertexReference(int contributionIndex, int vertexIndex) {
  }

  private record FaceReference(int contributionIndex, int faceIndex) {
  }

  private interface IntConsumer {
    void accept(int value);
  }

  private interface VertexReferenceConsumer {
    void accept(VertexReference value);
  }

  private interface FaceReferenceConsumer {
    void accept(FaceReference value);
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
}
