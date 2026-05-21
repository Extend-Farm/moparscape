package io.github.ffakira.rsps.content;

import java.util.List;

public record ObjectDefinition(
    int id,
    String name,
    List<Integer> modelIds,
    List<Integer> modelTypes,
    int morphVarBitId,
    int morphVarpId,
    List<Integer> morphIds,
    List<Integer> recolorSources,
    List<Integer> recolorTargets,
    int sizeX,
    int sizeY,
    boolean solid,
    boolean impenetrable,
    boolean interactive,
    boolean contouredGround,
    boolean mirrored,
    boolean castsShadow,
    boolean obstructsGround,
    int decorDisplacement,
    int mapSceneId,
    int mapFunctionId,
    int blockingMask,
    int ambient,
    int contrast,
    int scaleX,
    int scaleY,
    int scaleZ,
    int translateX,
    int translateY,
    int translateZ,
    int animationId
) {

  public ObjectDefinition {
    modelIds = List.copyOf(modelIds);
    modelTypes = List.copyOf(modelTypes);
    morphIds = List.copyOf(morphIds);
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
    int canonicalType = canonicalModelType(objectType);
    java.util.ArrayList<Integer> selectedIds = new java.util.ArrayList<>();
    for (int index = 0; index < modelTypes.size(); index++) {
      if (modelTypes.get(index) == canonicalType && index < modelIds.size()) {
        selectedIds.add(modelIds.get(index));
      }
    }
    return List.copyOf(selectedIds);
  }

  private static int canonicalModelType(int objectType) {
    // In the 317 client, wall-decoration placement variants 5..8 reuse model type 4.
    return switch (objectType) {
      case 5, 6, 7, 8 -> 4;
      default -> objectType;
    };
  }
}
