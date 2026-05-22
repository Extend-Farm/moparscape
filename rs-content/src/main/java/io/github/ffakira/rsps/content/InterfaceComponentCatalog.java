package io.github.ffakira.rsps.content;

import io.github.ffakira.rsps.cache.CacheArchiveContainer;
import io.github.ffakira.rsps.cache.CacheArchiveRepository;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public final class InterfaceComponentCatalog {

  private static final int TOP_LEVEL_ARCHIVE_STORE = 0;
  private static final String INTERFACE_DATA_ENTRY = "data";

  private final InterfaceComponentDefinition[] componentsById;

  private InterfaceComponentCatalog(InterfaceComponentDefinition[] componentsById) {
    this.componentsById = componentsById;
  }

  public static InterfaceComponentCatalog empty() {
    return new InterfaceComponentCatalog(new InterfaceComponentDefinition[0]);
  }

  public static InterfaceComponentCatalog load(ContentManifest manifest) {
    CacheArchiveContainer interfaceArchive = new CacheArchiveRepository(manifest.cacheStore())
        .loadArchive(TOP_LEVEL_ARCHIVE_STORE, TopLevelArchiveId.INTERFACE.archiveId());
    return parse(interfaceArchive.readEntry(INTERFACE_DATA_ENTRY));
  }

  static InterfaceComponentCatalog parse(byte[] dataBytes) {
    ByteCursor cursor = new ByteCursor(dataBytes);
    int componentCount = cursor.readUnsignedShort();
    InterfaceComponentDefinition[] components = new InterfaceComponentDefinition[componentCount];
    int currentParentId = -1;

    while (cursor.hasRemaining()) {
      int componentId = cursor.readUnsignedShort();
      if (componentId == 65535) {
        currentParentId = cursor.readUnsignedShort();
        componentId = cursor.readUnsignedShort();
      }

      int componentType = cursor.readUnsignedByte();
      int optionType = cursor.readUnsignedByte();
      int scriptTriggerId = cursor.readUnsignedShort();
      int width = cursor.readUnsignedShort();
      int height = cursor.readUnsignedShort();
      int alpha = cursor.readUnsignedByte();
      int hoverTargetId = decodeHoverTargetId(cursor);

      int[] conditionTypes = new int[cursor.readUnsignedByte()];
      int[] conditionOperands = new int[conditionTypes.length];
      for (int index = 0; index < conditionTypes.length; index++) {
        conditionTypes[index] = cursor.readUnsignedByte();
        conditionOperands[index] = cursor.readUnsignedShort();
      }

      int[][] clientScripts = new int[cursor.readUnsignedByte()][];
      for (int scriptIndex = 0; scriptIndex < clientScripts.length; scriptIndex++) {
        int entryCount = cursor.readUnsignedShort();
        int[] script = new int[entryCount];
        for (int entryIndex = 0; entryIndex < entryCount; entryIndex++) {
          script[entryIndex] = cursor.readUnsignedShort();
        }
        clientScripts[scriptIndex] = script;
      }

      InterfaceComponentDefinition.Container container = InterfaceComponentDefinition.Container.empty();
      InterfaceComponentDefinition.InventoryGrid inventoryGrid = InterfaceComponentDefinition.InventoryGrid.empty();
      boolean horizontalFill = false;
      boolean centeredText = false;
      int fontIndex = -1;
      boolean textShadow = false;
      String defaultText = "";
      String activeText = "";
      int defaultColor = 0;
      int activeColor = 0;
      int hoverColor = 0;
      int activeHoverColor = 0;
      InterfaceComponentDefinition.SpriteReference defaultSprite = null;
      InterfaceComponentDefinition.SpriteReference activeSprite = null;
      InterfaceComponentDefinition.ModelDisplay modelDisplay = InterfaceComponentDefinition.ModelDisplay.empty();
      int staticModelId = 0;
      boolean staticModelEnabled = false;

      if (componentType == 0) {
        int scrollContentHeight = cursor.readUnsignedShort();
        boolean hidden = cursor.readUnsignedByte() == 1;
        int childCount = cursor.readUnsignedShort();
        int[] childIds = new int[childCount];
        int[] childX = new int[childCount];
        int[] childY = new int[childCount];
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
          childIds[childIndex] = cursor.readUnsignedShort();
          childX[childIndex] = cursor.readSignedShort();
          childY[childIndex] = cursor.readSignedShort();
        }
        container = new InterfaceComponentDefinition.Container(scrollContentHeight, hidden, childIds, childX, childY);
      }

      if (componentType == 1) {
        staticModelId = cursor.readUnsignedShort();
        staticModelEnabled = cursor.readUnsignedByte() == 1;
      }

      if (componentType == 2) {
        inventoryGrid = decodeInventoryGrid(cursor, false);
      }

      if (componentType == 3) {
        horizontalFill = cursor.readUnsignedByte() == 1;
      }

      if (componentType == 4 || componentType == 1) {
        centeredText = cursor.readUnsignedByte() == 1;
        fontIndex = cursor.readUnsignedByte();
        textShadow = cursor.readUnsignedByte() == 1;
      }

      if (componentType == 4) {
        defaultText = cursor.readString();
        activeText = cursor.readString();
      }

      if (componentType == 1 || componentType == 3 || componentType == 4) {
        defaultColor = cursor.readInt();
      }

      if (componentType == 3 || componentType == 4) {
        activeColor = cursor.readInt();
        hoverColor = cursor.readInt();
        activeHoverColor = cursor.readInt();
      }

      if (componentType == 5) {
        defaultSprite = decodeSpriteReference(cursor.readString());
        activeSprite = decodeSpriteReference(cursor.readString());
      }

      if (componentType == 6) {
        DecodedModelReference defaultModel = decodeModelReference(cursor);
        DecodedModelReference activeModel = decodeModelReference(cursor);
        modelDisplay = new InterfaceComponentDefinition.ModelDisplay(
            defaultModel.sourceKind(),
            defaultModel.modelId(),
            activeModel.sourceKind(),
            activeModel.modelId(),
            decodeAnimationId(cursor),
            decodeAnimationId(cursor),
            cursor.readUnsignedShort(),
            cursor.readUnsignedShort(),
            cursor.readUnsignedShort()
        );
      }

      if (componentType == 7) {
        InterfaceComponentDefinition.InventoryGrid textGrid = decodeInventoryGrid(cursor, true);
        inventoryGrid = new InterfaceComponentDefinition.InventoryGrid(
            textGrid.swappable(),
            textGrid.usable(),
            textGrid.useTarget(),
            textGrid.depthFlag(),
            textGrid.paddingX(),
            textGrid.paddingY(),
            textGrid.slotDecorations(),
            textGrid.options(),
            textGrid.centeredText(),
            textGrid.fontIndex(),
            textGrid.textShadow(),
            textGrid.textColor()
        );
      }

      InterfaceComponentDefinition.TextBlock textBlock = new InterfaceComponentDefinition.TextBlock(
          centeredText,
          fontIndex,
          textShadow,
          defaultText,
          activeText
      );
      InterfaceComponentDefinition.ColorSet colors = new InterfaceComponentDefinition.ColorSet(
          defaultColor,
          activeColor,
          hoverColor,
          activeHoverColor
      );

      String tooltipPrefix = "";
      String tooltipSuffix = "";
      int tooltipTargetId = -1;
      if (optionType == 2 || componentType == 2) {
        tooltipPrefix = cursor.readString();
        tooltipSuffix = cursor.readString();
        tooltipTargetId = cursor.readUnsignedShort();
      }

      String actionLabel = "";
      if (optionType == 1 || optionType == 4 || optionType == 5 || optionType == 6) {
        actionLabel = cursor.readString();
        if (actionLabel.isEmpty()) {
          actionLabel = defaultActionLabel(optionType);
        }
      }

      components[componentId] = new InterfaceComponentDefinition(
          componentId,
          currentParentId,
          componentType,
          optionType,
          scriptTriggerId,
          width,
          height,
          alpha,
          hoverTargetId,
          conditionTypes,
          conditionOperands,
          clientScripts,
          container,
          inventoryGrid,
          horizontalFill,
          textBlock,
          colors,
          defaultSprite,
          activeSprite,
          modelDisplay,
          staticModelId,
          staticModelEnabled,
          tooltipPrefix,
          tooltipSuffix,
          tooltipTargetId,
          actionLabel
      );
    }

    return new InterfaceComponentCatalog(components);
  }

  public int size() {
    return componentsById.length;
  }

  public InterfaceComponentDefinition getOrNull(int componentId) {
    if (componentId < 0 || componentId >= componentsById.length) {
      return null;
    }
    return componentsById[componentId];
  }

  public Optional<InterfaceComponentDefinition> find(int componentId) {
    return Optional.ofNullable(getOrNull(componentId));
  }

  public InterfaceComponentDefinition require(int componentId) {
    InterfaceComponentDefinition component = getOrNull(componentId);
    if (component == null) {
      throw new IllegalArgumentException("Unknown interface component " + componentId);
    }
    return component;
  }

  private static InterfaceComponentDefinition.InventoryGrid decodeInventoryGrid(ByteCursor cursor, boolean textGrid) {
    if (!textGrid) {
      boolean swappable = cursor.readUnsignedByte() == 1;
      boolean usable = cursor.readUnsignedByte() == 1;
      boolean useTarget = cursor.readUnsignedByte() == 1;
      boolean depthFlag = cursor.readUnsignedByte() == 1;
      int paddingX = cursor.readUnsignedByte();
      int paddingY = cursor.readUnsignedByte();
      InterfaceComponentDefinition.SlotDecoration[] slotDecorations = new InterfaceComponentDefinition.SlotDecoration[20];
      for (int slot = 0; slot < slotDecorations.length; slot++) {
        if (cursor.readUnsignedByte() == 1) {
          slotDecorations[slot] = new InterfaceComponentDefinition.SlotDecoration(
              cursor.readSignedShort(),
              cursor.readSignedShort(),
              decodeSpriteReference(cursor.readString())
          );
        }
      }
      String[] options = new String[5];
      for (int option = 0; option < options.length; option++) {
        String value = cursor.readString();
        options[option] = value.isEmpty() ? null : value;
      }
      return new InterfaceComponentDefinition.InventoryGrid(
          swappable,
          usable,
          useTarget,
          depthFlag,
          paddingX,
          paddingY,
          slotDecorations,
          options,
          false,
          -1,
          false,
          0
      );
    }

    boolean centeredText = cursor.readUnsignedByte() == 1;
    int fontIndex = cursor.readUnsignedByte();
    boolean textShadow = cursor.readUnsignedByte() == 1;
    int textColor = cursor.readInt();
    int paddingX = cursor.readSignedShort();
    int paddingY = cursor.readSignedShort();
    boolean usable = cursor.readUnsignedByte() == 1;
    String[] options = new String[5];
    for (int option = 0; option < options.length; option++) {
      String value = cursor.readString();
      options[option] = value.isEmpty() ? null : value;
    }
    return new InterfaceComponentDefinition.InventoryGrid(
        false,
        usable,
        false,
        false,
        paddingX,
        paddingY,
        new InterfaceComponentDefinition.SlotDecoration[0],
        options,
        centeredText,
        fontIndex,
        textShadow,
        textColor
    );
  }

  private static InterfaceComponentDefinition.SpriteReference decodeSpriteReference(String rawValue) {
    if (rawValue == null || rawValue.isEmpty()) {
      return null;
    }
    int separator = rawValue.lastIndexOf(',');
    if (separator < 0 || separator + 1 >= rawValue.length()) {
      return null;
    }
    return new InterfaceComponentDefinition.SpriteReference(
        rawValue.substring(0, separator),
        Integer.parseInt(rawValue.substring(separator + 1))
    );
  }

  private static int decodeHoverTargetId(ByteCursor cursor) {
    int high = cursor.readUnsignedByte();
    if (high == 0) {
      return -1;
    }
    return ((high - 1) << 8) + cursor.readUnsignedByte();
  }

  private static DecodedModelReference decodeModelReference(ByteCursor cursor) {
    int high = cursor.readUnsignedByte();
    if (high == 0) {
      return new DecodedModelReference(0, 0);
    }
    return new DecodedModelReference(1, ((high - 1) << 8) + cursor.readUnsignedByte());
  }

  private static int decodeAnimationId(ByteCursor cursor) {
    int high = cursor.readUnsignedByte();
    if (high == 0) {
      return -1;
    }
    return ((high - 1) << 8) + cursor.readUnsignedByte();
  }

  private static String defaultActionLabel(int optionType) {
    return switch (optionType) {
      case 1 -> "Ok";
      case 4, 5 -> "Select";
      case 6 -> "Continue";
      default -> "";
    };
  }

  private static final class ByteCursor {
    private final byte[] bytes;
    private int offset;

    private ByteCursor(byte[] bytes) {
      this.bytes = bytes;
    }

    private boolean hasRemaining() {
      return offset < bytes.length;
    }

    private int readUnsignedByte() {
      return bytes[offset++] & 0xff;
    }

    private int readUnsignedShort() {
      return (readUnsignedByte() << 8) | readUnsignedByte();
    }

    private int readSignedShort() {
      int value = readUnsignedShort();
      return value > 32767 ? value - 65536 : value;
    }

    private int readInt() {
      return (readUnsignedByte() << 24)
          | (readUnsignedByte() << 16)
          | (readUnsignedByte() << 8)
          | readUnsignedByte();
    }

    private String readString() {
      int start = offset;
      while (bytes[offset++] != 10) {
        // advance
      }
      return new String(bytes, start, offset - start - 1, StandardCharsets.ISO_8859_1);
    }
  }

  private record DecodedModelReference(int sourceKind, int modelId) {
  }
}
