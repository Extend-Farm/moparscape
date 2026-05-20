package io.github.ffakira.rsps.content;

import java.util.Arrays;

public record AnimationSequenceDefinition(
    int frameCount,
    int[] primaryFrameIds,
    int[] secondaryFrameIds,
    int[] frameDurations,
    int loopOffset,
    int[] interleaveOrder,
    boolean stretches,
    int forcedPriority,
    int playerOffhandAppearanceId,
    int playerMainhandAppearanceId,
    int maximumLoops,
    int animatingPrecedence,
    int priority,
    int replayMode
) {

  public AnimationSequenceDefinition {
    primaryFrameIds = Arrays.copyOf(primaryFrameIds, primaryFrameIds.length);
    secondaryFrameIds = Arrays.copyOf(secondaryFrameIds, secondaryFrameIds.length);
    frameDurations = Arrays.copyOf(frameDurations, frameDurations.length);
    interleaveOrder = interleaveOrder == null ? null : Arrays.copyOf(interleaveOrder, interleaveOrder.length);
  }

  public int frameIdAt(int frameIndex) {
    if (frameIndex < 0 || frameIndex >= primaryFrameIds.length) {
      return -1;
    }
    return primaryFrameIds[frameIndex];
  }

  public int frameDurationAt(int frameIndex) {
    if (frameIndex < 0 || frameIndex >= frameDurations.length) {
      return 1;
    }
    return Math.max(1, frameDurations[frameIndex]);
  }
}
