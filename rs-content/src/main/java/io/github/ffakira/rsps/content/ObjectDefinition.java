package io.github.ffakira.rsps.content;

import java.util.List;

public record ObjectDefinition(
    int id,
    String name,
    List<Integer> modelIds,
    List<Integer> modelTypes,
    List<Integer> recolorSources,
    List<Integer> recolorTargets,
    int sizeX,
    int sizeY,
    boolean solid,
    boolean impenetrable,
    boolean interactive,
    boolean mirrored,
    boolean castsShadow,
    boolean obstructsGround,
    int decorDisplacement,
    int mapSceneId,
    int blockingMask,
    int scaleX,
    int scaleY,
    int scaleZ,
    int translateX,
    int translateY,
    int translateZ
) {

  public ObjectDefinition {
    modelIds = List.copyOf(modelIds);
    modelTypes = List.copyOf(modelTypes);
    recolorSources = List.copyOf(recolorSources);
    recolorTargets = List.copyOf(recolorTargets);
  }

  public int footprintWidth(int orientation) {
    return (orientation & 1) == 1 ? sizeY : sizeX;
  }

  public int footprintHeight(int orientation) {
    return (orientation & 1) == 1 ? sizeX : sizeY;
  }

  public List<Integer> modelIdsForType(int objectType) {
    if (modelTypes.isEmpty()) {
      return modelIds;
    }
    java.util.ArrayList<Integer> selectedIds = new java.util.ArrayList<>();
    for (int index = 0; index < modelTypes.size(); index++) {
      if (modelTypes.get(index) == objectType && index < modelIds.size()) {
        selectedIds.add(modelIds.get(index));
      }
    }
    return List.copyOf(selectedIds);
  }
}
