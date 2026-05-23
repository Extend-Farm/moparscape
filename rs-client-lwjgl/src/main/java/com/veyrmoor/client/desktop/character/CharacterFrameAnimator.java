package com.veyrmoor.client.desktop.character;

import com.veyrmoor.cache.AnimationFrameCatalog;
import com.veyrmoor.cache.AnimationFrameDefinition;
import java.util.List;

final class CharacterFrameAnimator {

  private final AnimationFrameCatalog animationFrames;
  private final CharacterSkinGroupCatalog skinGroupCatalog = new CharacterSkinGroupCatalog();
  private final CharacterFrameTransformApplier transformApplier = new CharacterFrameTransformApplier();

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
    CharacterSkinGroupCatalog.SkinGroups skinGroups = skinGroupCatalog.skinGroups(contribution);
    if (skinGroups == null) {
      return null;
    }
    return transformApplier.apply(contribution, frame, skinGroups);
  }

  private List<AnimatedContribution> animateContributions(
      List<CharacterModelSourceBuilder.PreparedContribution> contributions,
      AnimationFrameDefinition frame
  ) {
    if (frame == null) {
      return null;
    }
    CharacterSkinGroupCatalog.CombinedSkinGroups skinGroups = skinGroupCatalog.combinedSkinGroups(contributions);
    if (skinGroups == null) {
      return null;
    }
    return transformApplier.apply(contributions, frame, skinGroups);
  }

  private AnimatedContribution animateInterleavedContribution(
      CharacterModelSourceBuilder.PreparedContribution contribution,
      AnimationFrameDefinition actionFrame,
      AnimationFrameDefinition movementFrame,
      int[] interleaveOrder
  ) {
    CharacterSkinGroupCatalog.SkinGroups skinGroups = skinGroupCatalog.skinGroups(contribution);
    if (skinGroups == null) {
      return null;
    }
    return transformApplier.applyInterleaved(contribution, actionFrame, movementFrame, interleaveOrder, skinGroups);
  }

  private List<AnimatedContribution> animateInterleavedContributions(
      List<CharacterModelSourceBuilder.PreparedContribution> contributions,
      AnimationFrameDefinition actionFrame,
      AnimationFrameDefinition movementFrame,
      int[] interleaveOrder
  ) {
    CharacterSkinGroupCatalog.CombinedSkinGroups skinGroups = skinGroupCatalog.combinedSkinGroups(contributions);
    if (skinGroups == null) {
      return null;
    }
    return transformApplier.applyInterleaved(contributions, actionFrame, movementFrame, interleaveOrder, skinGroups);
  }

  record AnimatedContribution(int[] modelX, int[] modelY, int[] modelZ, int[] faceAlpha) {
  }
}
