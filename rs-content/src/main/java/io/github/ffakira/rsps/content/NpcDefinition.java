package io.github.ffakira.rsps.content;

import java.util.List;

public record NpcDefinition(
    int id,
    String name,
    List<Integer> modelIds,
    List<Integer> headModelIds,
    List<String> actions,
    List<Integer> recolorSources,
    List<Integer> recolorTargets,
    int tileSize,
    int idleSequenceId,
    int walkSequenceId,
    int turnAroundSequenceId,
    int turnRightSequenceId,
    int turnLeftSequenceId,
    int combatLevel,
    boolean renderOnMinimap,
    int scaleXY,
    int scaleZ,
    boolean visiblePriority,
    int ambient,
    int contrast,
    int headIconId,
    int rotationSpeed,
    int morphVarBitId,
    int morphVarpId,
    List<Integer> morphIds,
    boolean clickable
) {

  public NpcDefinition {
    modelIds = List.copyOf(modelIds);
    headModelIds = List.copyOf(headModelIds);
    actions = java.util.Collections.unmodifiableList(new java.util.ArrayList<>(actions));
    recolorSources = List.copyOf(recolorSources);
    recolorTargets = List.copyOf(recolorTargets);
    morphIds = List.copyOf(morphIds);
  }
}
