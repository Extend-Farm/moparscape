package com.veyrmoor.content;

import java.util.List;

public record ItemDefinition(
    int id,
    String name,
    String description,
    boolean stackable,
    int value,
    boolean membersOnly,
    boolean noted,
    int noteLinkItemId,
    int noteTemplateItemId,
    InventoryAppearance inventoryAppearance,
    List<StackVariant> stackVariants,
    List<Integer> recolorSources,
    List<Integer> recolorTargets,
    List<Integer> maleBodyModelIds,
    int maleBodyOffsetY,
    List<Integer> femaleBodyModelIds,
    int femaleBodyOffsetY
) {

  public ItemDefinition {
    name = name == null ? "" : name;
    description = description == null ? "" : description;
    inventoryAppearance = inventoryAppearance == null ? InventoryAppearance.empty() : inventoryAppearance;
    stackVariants = List.copyOf(stackVariants);
    recolorSources = List.copyOf(recolorSources);
    recolorTargets = List.copyOf(recolorTargets);
    maleBodyModelIds = List.copyOf(maleBodyModelIds);
    femaleBodyModelIds = List.copyOf(femaleBodyModelIds);
  }

  public List<Integer> bodyModelIds(boolean female) {
    return female ? femaleBodyModelIds : maleBodyModelIds;
  }

  public int bodyOffsetY(boolean female) {
    return female ? femaleBodyOffsetY : maleBodyOffsetY;
  }

  public record InventoryAppearance(
      int modelId,
      int zoom,
      int rotationX,
      int rotationY,
      int rotationZ,
      int offsetX,
      int offsetY,
      int resizeX,
      int resizeY,
      int resizeZ,
      int ambient,
      int contrast
  ) {

    private static final InventoryAppearance EMPTY = new InventoryAppearance(
        -1,
        2000,
        0,
        0,
        0,
        0,
        0,
        128,
        128,
        128,
        0,
        0
    );

    public static InventoryAppearance empty() {
      return EMPTY;
    }
  }

  public record StackVariant(int itemId, int minimumQuantity) {
  }
}
