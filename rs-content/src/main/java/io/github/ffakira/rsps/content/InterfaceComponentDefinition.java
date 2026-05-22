package io.github.ffakira.rsps.content;

import java.util.Arrays;

public record InterfaceComponentDefinition(
    int id,
    int parentId,
    int componentType,
    int optionType,
    int scriptTriggerId,
    int width,
    int height,
    int alpha,
    int hoverTargetId,
    int[] conditionTypes,
    int[] conditionOperands,
    int[][] clientScripts,
    Container container,
    InventoryGrid inventoryGrid,
    boolean horizontalFill,
    TextBlock textBlock,
    ColorSet colors,
    SpriteReference defaultSprite,
    SpriteReference activeSprite,
    ModelDisplay modelDisplay,
    int staticModelId,
    boolean staticModelEnabled,
    String tooltipPrefix,
    String tooltipSuffix,
    int tooltipTargetId,
    String actionLabel
) {

  public InterfaceComponentDefinition {
    conditionTypes = conditionTypes == null ? new int[0] : conditionTypes.clone();
    conditionOperands = conditionOperands == null ? new int[0] : conditionOperands.clone();
    clientScripts = cloneMatrix(clientScripts);
    container = container == null ? Container.empty() : container;
    inventoryGrid = inventoryGrid == null ? InventoryGrid.empty() : inventoryGrid;
    textBlock = textBlock == null ? TextBlock.empty() : textBlock;
    colors = colors == null ? ColorSet.empty() : colors;
    modelDisplay = modelDisplay == null ? ModelDisplay.empty() : modelDisplay;
    tooltipPrefix = tooltipPrefix == null ? "" : tooltipPrefix;
    tooltipSuffix = tooltipSuffix == null ? "" : tooltipSuffix;
    actionLabel = actionLabel == null ? "" : actionLabel;
  }

  private static int[][] cloneMatrix(int[][] source) {
    if (source == null || source.length == 0) {
      return new int[0][];
    }
    int[][] copy = new int[source.length][];
    for (int index = 0; index < source.length; index++) {
      copy[index] = source[index] == null ? new int[0] : source[index].clone();
    }
    return copy;
  }

  public record Container(
      int scrollContentHeight,
      boolean hidden,
      int[] childIds,
      int[] childX,
      int[] childY
  ) {

    private static final Container EMPTY = new Container(0, false, new int[0], new int[0], new int[0]);

    public Container {
      childIds = childIds == null ? new int[0] : childIds.clone();
      childX = childX == null ? new int[0] : childX.clone();
      childY = childY == null ? new int[0] : childY.clone();
    }

    public static Container empty() {
      return EMPTY;
    }
  }

  public record InventoryGrid(
      boolean swappable,
      boolean usable,
      boolean useTarget,
      boolean depthFlag,
      int paddingX,
      int paddingY,
      SlotDecoration[] slotDecorations,
      String[] options,
      boolean centeredText,
      int fontIndex,
      boolean textShadow,
      int textColor
  ) {

    private static final InventoryGrid EMPTY = new InventoryGrid(
        false,
        false,
        false,
        false,
        0,
        0,
        new SlotDecoration[0],
        new String[0],
        false,
        -1,
        false,
        0
    );

    public InventoryGrid {
      slotDecorations = slotDecorations == null ? new SlotDecoration[0] : slotDecorations.clone();
      options = options == null ? new String[0] : Arrays.copyOf(options, options.length);
    }

    public static InventoryGrid empty() {
      return EMPTY;
    }
  }

  public record SlotDecoration(
      int offsetX,
      int offsetY,
      SpriteReference sprite
  ) {
  }

  public record TextBlock(
      boolean centered,
      int fontIndex,
      boolean shadow,
      String defaultText,
      String activeText
  ) {

    private static final TextBlock EMPTY = new TextBlock(false, -1, false, "", "");

    public TextBlock {
      defaultText = defaultText == null ? "" : defaultText;
      activeText = activeText == null ? "" : activeText;
    }

    public static TextBlock empty() {
      return EMPTY;
    }
  }

  public record ColorSet(
      int defaultColor,
      int activeColor,
      int hoverColor,
      int activeHoverColor
  ) {

    private static final ColorSet EMPTY = new ColorSet(0, 0, 0, 0);

    public static ColorSet empty() {
      return EMPTY;
    }
  }

  public record SpriteReference(String entryName, int frameIndex) {

    public SpriteReference {
      entryName = entryName == null ? "" : entryName;
    }
  }

  public record ModelDisplay(
      int defaultSourceKind,
      int defaultModelId,
      int activeSourceKind,
      int activeModelId,
      int defaultAnimationId,
      int activeAnimationId,
      int zoom,
      int yaw,
      int roll
  ) {

    private static final ModelDisplay EMPTY = new ModelDisplay(0, 0, 0, 0, 0, 0, 0, 0, 0);

    public static ModelDisplay empty() {
      return EMPTY;
    }
  }
}
