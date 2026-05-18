package io.github.ffakira.rsps.content;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ItemDefinitionCatalog {

  private static final String ITEM_INDEX_ENTRY = "obj.idx";
  private static final String ITEM_DATA_ENTRY = "obj.dat";

  private final ItemDefinition[] definitionsById;
  private final int definitionCount;

  private ItemDefinitionCatalog(ItemDefinition[] definitionsById, int definitionCount) {
    this.definitionsById = definitionsById;
    this.definitionCount = definitionCount;
  }

  public static ItemDefinitionCatalog empty() {
    return new ItemDefinitionCatalog(new ItemDefinition[0], 0);
  }

  public static ItemDefinitionCatalog of(ItemDefinition... definitions) {
    return of(List.of(definitions));
  }

  public static ItemDefinitionCatalog of(List<ItemDefinition> definitions) {
    if (definitions.isEmpty()) {
      return empty();
    }
    int highestItemId = definitions.stream()
        .mapToInt(ItemDefinition::id)
        .max()
        .orElse(-1);
    ItemDefinition[] definitionsById = new ItemDefinition[highestItemId + 1];
    for (ItemDefinition definition : definitions) {
      definitionsById[definition.id()] = definition;
    }
    return new ItemDefinitionCatalog(definitionsById, definitions.size());
  }

  public static ItemDefinitionCatalog load(ContentManifest manifest) {
    ContentArchiveSnapshot archiveSnapshot = new ContentArchiveCatalog().load(manifest);
    return parse(archiveSnapshot.readConfigEntry(ITEM_INDEX_ENTRY), archiveSnapshot.readConfigEntry(ITEM_DATA_ENTRY));
  }

  static ItemDefinitionCatalog parse(byte[] indexBytes, byte[] dataBytes) {
    ByteCursor indexCursor = new ByteCursor(indexBytes);
    int itemCount = indexCursor.readUnsignedShort();
    int[] offsetsById = new int[itemCount];
    int offset = 2;
    for (int itemId = 0; itemId < itemCount; itemId++) {
      offsetsById[itemId] = offset;
      offset += indexCursor.readUnsignedShort();
    }

    MutableItemDefinition[] definitions = new MutableItemDefinition[itemCount];
    for (int itemId = 0; itemId < itemCount; itemId++) {
      ByteCursor dataCursor = new ByteCursor(dataBytes, offsetsById[itemId]);
      MutableItemDefinition definition = new MutableItemDefinition(itemId);
      definition.readFrom(dataCursor);
      definitions[itemId] = definition;
    }

    ItemDefinition[] finalizedDefinitions = new ItemDefinition[itemCount];
    for (int itemId = 0; itemId < itemCount; itemId++) {
      finalizedDefinitions[itemId] = definitions[itemId].toDefinition(definitions);
    }
    return new ItemDefinitionCatalog(finalizedDefinitions, itemCount);
  }

  public int size() {
    return definitionCount;
  }

  public Optional<ItemDefinition> find(int itemId) {
    if (itemId < 0 || itemId >= definitionsById.length) {
      return Optional.empty();
    }
    return Optional.ofNullable(definitionsById[itemId]);
  }

  public ItemDefinition require(int itemId) {
    return find(itemId)
        .orElseThrow(() -> new IllegalArgumentException("Unknown item definition " + itemId));
  }

  private static String indefiniteArticleFor(String name) {
    if (name == null || name.isEmpty()) {
      return "a";
    }
    return switch (Character.toLowerCase(name.charAt(0))) {
      case 'a', 'e', 'i', 'o', 'u' -> "an";
      default -> "a";
    };
  }

  private static final class MutableItemDefinition {
    private final int id;

    private String name = "";
    private String description = "";
    private boolean stackable;
    private int value = 1;
    private boolean membersOnly;
    private int noteLinkItemId = -1;
    private int noteTemplateItemId = -1;
    private final List<Integer> recolorSources = new ArrayList<>();
    private final List<Integer> recolorTargets = new ArrayList<>();
    private final List<Integer> maleBodyModelIds = new ArrayList<>();
    private int maleBodyOffsetY;
    private final List<Integer> femaleBodyModelIds = new ArrayList<>();
    private int femaleBodyOffsetY;

    private MutableItemDefinition(int id) {
      this.id = id;
    }

    private void readFrom(ByteCursor cursor) {
      while (true) {
        int opcode = cursor.readUnsignedByte();
        if (opcode == 0) {
          return;
        }
        switch (opcode) {
          case 1 -> cursor.readUnsignedShort();
          case 2 -> name = cursor.readString();
          case 3 -> description = cursor.readString();
          case 4, 5, 6, 10, 90, 91, 92, 93, 95, 97, 98, 110, 111, 112 ->
              cursor.readUnsignedShort();
          case 7, 8 -> cursor.readUnsignedShort();
          case 11 -> stackable = true;
          case 12 -> value = cursor.readInt();
          case 16 -> membersOnly = true;
          case 23 -> {
            maleBodyModelIds.add(cursor.readUnsignedShort());
            maleBodyOffsetY = cursor.readSignedByte();
          }
          case 24 -> maleBodyModelIds.add(cursor.readUnsignedShort());
          case 25 -> {
            femaleBodyModelIds.add(cursor.readUnsignedShort());
            femaleBodyOffsetY = cursor.readSignedByte();
          }
          case 26 -> femaleBodyModelIds.add(cursor.readUnsignedShort());
          case 30, 31, 32, 33, 34, 35, 36, 37, 38, 39 -> cursor.readString();
          case 40 -> {
            int recolorCount = cursor.readUnsignedByte();
            for (int index = 0; index < recolorCount; index++) {
              recolorSources.add(cursor.readUnsignedShort());
              recolorTargets.add(cursor.readUnsignedShort());
            }
          }
          case 78 -> maleBodyModelIds.add(cursor.readUnsignedShort());
          case 79 -> femaleBodyModelIds.add(cursor.readUnsignedShort());
          case 100, 101, 102, 103, 104, 105, 106, 107, 108, 109 -> {
            cursor.readUnsignedShort();
            cursor.readUnsignedShort();
          }
          case 113, 114, 115 -> cursor.readUnsignedByte();
          default -> throw new IllegalStateException(
              "Unsupported item definition opcode " + opcode + " for item " + id
          );
        }
        if (opcode == 97) {
          noteLinkItemId = cursor.lastUnsignedShort();
        } else if (opcode == 98) {
          noteTemplateItemId = cursor.lastUnsignedShort();
        }
      }
    }

    private ItemDefinition toDefinition(MutableItemDefinition[] definitions) {
      String resolvedName = name;
      String resolvedDescription = description;
      int resolvedValue = value;
      boolean resolvedMembersOnly = membersOnly;
      boolean resolvedStackable = stackable;
      boolean noted = noteLinkItemId >= 0 && noteTemplateItemId >= 0;

      if (noted && noteLinkItemId < definitions.length) {
        MutableItemDefinition linkedDefinition = definitions[noteLinkItemId];
        resolvedName = linkedDefinition.name;
        resolvedValue = linkedDefinition.value;
        resolvedMembersOnly = linkedDefinition.membersOnly;
        resolvedDescription =
            "Swap this note at any bank for "
                + indefiniteArticleFor(linkedDefinition.name)
                + " "
                + linkedDefinition.name
                + ".";
        resolvedStackable = true;
      }

      if (resolvedName == null || resolvedName.isBlank()) {
        resolvedName = "item-" + id;
      }

      return new ItemDefinition(
          id,
          resolvedName,
          resolvedDescription,
          resolvedStackable,
          resolvedValue,
          resolvedMembersOnly,
          noted,
          noteLinkItemId,
          noteTemplateItemId,
          recolorSources,
          recolorTargets,
          maleBodyModelIds,
          maleBodyOffsetY,
          femaleBodyModelIds,
          femaleBodyOffsetY
      );
    }
  }

  private static final class ByteCursor {
    private final byte[] bytes;
    private int offset;
    private int lastUnsignedShort;

    private ByteCursor(byte[] bytes) {
      this(bytes, 0);
    }

    private ByteCursor(byte[] bytes, int offset) {
      this.bytes = bytes;
      this.offset = offset;
    }

    private int readUnsignedByte() {
      return bytes[offset++] & 0xff;
    }

    private int readSignedByte() {
      return bytes[offset++];
    }

    private int readUnsignedShort() {
      lastUnsignedShort = (readUnsignedByte() << 8) | readUnsignedByte();
      return lastUnsignedShort;
    }

    private int lastUnsignedShort() {
      return lastUnsignedShort;
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
}
