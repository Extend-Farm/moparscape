package com.veyrmoor.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class NpcDefinitionCatalog {

  private static final String NPC_INDEX_ENTRY = "npc.idx";
  private static final String NPC_DATA_ENTRY = "npc.dat";

  private final NpcDefinition[] definitionsById;

  private NpcDefinitionCatalog(NpcDefinition[] definitionsById) {
    this.definitionsById = definitionsById;
  }

  public static NpcDefinitionCatalog load(ContentManifest manifest) {
    ContentArchiveSnapshot archiveSnapshot = new ContentArchiveCatalog().load(manifest);
    return parse(
        archiveSnapshot.readConfigEntry(NPC_INDEX_ENTRY),
        archiveSnapshot.readConfigEntry(NPC_DATA_ENTRY)
    );
  }

  static NpcDefinitionCatalog parse(byte[] indexBytes, byte[] dataBytes) {
    ContentDataReader indexReader = new ContentDataReader(indexBytes);
    int npcCount = indexReader.readUnsignedShort();
    int[] offsetsById = new int[npcCount];
    int offset = 2;
    for (int npcId = 0; npcId < npcCount; npcId++) {
      offsetsById[npcId] = offset;
      offset += indexReader.readUnsignedShort();
    }

    ContentDataReader dataReader = new ContentDataReader(dataBytes);
    NpcDefinition[] definitions = new NpcDefinition[npcCount];
    for (int npcId = 0; npcId < npcCount; npcId++) {
      dataReader.position(offsetsById[npcId]);
      definitions[npcId] = parseDefinition(npcId, dataReader);
    }
    return new NpcDefinitionCatalog(definitions);
  }

  public int size() {
    return definitionsById.length;
  }

  public Optional<NpcDefinition> find(int npcId) {
    if (npcId < 0 || npcId >= definitionsById.length) {
      return Optional.empty();
    }
    return Optional.ofNullable(definitionsById[npcId]);
  }

  public NpcDefinition require(int npcId) {
    return find(npcId)
        .orElseThrow(() -> new IllegalArgumentException("Unknown npc definition " + npcId));
  }

  private static NpcDefinition parseDefinition(int npcId, ContentDataReader reader) {
    String name = "npc-" + npcId;
    ArrayList<Integer> modelIds = new ArrayList<>();
    ArrayList<Integer> headModelIds = new ArrayList<>();
    ArrayList<String> actions = new ArrayList<>(5);
    for (int index = 0; index < 5; index++) {
      actions.add(null);
    }
    ArrayList<Integer> recolorSources = new ArrayList<>();
    ArrayList<Integer> recolorTargets = new ArrayList<>();
    int tileSize = 1;
    int idleSequenceId = -1;
    int walkSequenceId = -1;
    int turnAroundSequenceId = -1;
    int turnRightSequenceId = -1;
    int turnLeftSequenceId = -1;
    int combatLevel = -1;
    boolean renderOnMinimap = true;
    int scaleXY = 128;
    int scaleZ = 128;
    boolean visiblePriority = false;
    int ambient = 0;
    int contrast = 0;
    int headIconId = -1;
    int rotationSpeed = 32;
    int morphVarBitId = -1;
    int morphVarpId = -1;
    ArrayList<Integer> morphIds = new ArrayList<>();
    boolean clickable = true;

    while (true) {
      int opcode = reader.readUnsignedByte();
      if (opcode == 0) {
        return new NpcDefinition(
            npcId,
            name,
            modelIds,
            headModelIds,
            actions,
            recolorSources,
            recolorTargets,
            tileSize,
            idleSequenceId,
            walkSequenceId,
            turnAroundSequenceId,
            turnRightSequenceId,
            turnLeftSequenceId,
            combatLevel,
            renderOnMinimap,
            scaleXY,
            scaleZ,
            visiblePriority,
            ambient,
            contrast,
            headIconId,
            rotationSpeed,
            morphVarBitId,
            morphVarpId,
            morphIds,
            clickable
        );
      }
      switch (opcode) {
        case 1 -> {
          int modelCount = reader.readUnsignedByte();
          for (int index = 0; index < modelCount; index++) {
            modelIds.add(reader.readUnsignedShort());
          }
        }
        case 2 -> name = reader.readString();
        case 3 -> reader.readByteArray();
        case 12 -> tileSize = reader.readSignedByte();
        case 13 -> idleSequenceId = reader.readUnsignedShort();
        case 14 -> walkSequenceId = reader.readUnsignedShort();
        case 17 -> {
          walkSequenceId = reader.readUnsignedShort();
          turnAroundSequenceId = reader.readUnsignedShort();
          turnRightSequenceId = reader.readUnsignedShort();
          turnLeftSequenceId = reader.readUnsignedShort();
        }
        case 30, 31, 32, 33, 34 -> {
          String action = reader.readString();
          actions.set(opcode - 30, "hidden".equalsIgnoreCase(action) ? null : action);
        }
        case 40 -> {
          int recolorCount = reader.readUnsignedByte();
          for (int index = 0; index < recolorCount; index++) {
            recolorSources.add(reader.readUnsignedShort());
            recolorTargets.add(reader.readUnsignedShort());
          }
        }
        case 60 -> {
          int headModelCount = reader.readUnsignedByte();
          for (int index = 0; index < headModelCount; index++) {
            headModelIds.add(reader.readUnsignedShort());
          }
        }
        case 90, 91, 92 -> reader.readUnsignedShort();
        case 93 -> renderOnMinimap = false;
        case 95 -> combatLevel = reader.readUnsignedShort();
        case 97 -> scaleXY = reader.readUnsignedShort();
        case 98 -> scaleZ = reader.readUnsignedShort();
        case 99 -> visiblePriority = true;
        case 100 -> ambient = reader.readSignedByte();
        case 101 -> contrast = reader.readSignedByte() * 5;
        case 102 -> headIconId = reader.readUnsignedShort();
        case 103 -> rotationSpeed = reader.readUnsignedShort();
        case 106 -> {
          morphVarBitId = normalizeDefinitionId(reader.readUnsignedShort());
          morphVarpId = normalizeDefinitionId(reader.readUnsignedShort());
          int morphCount = reader.readUnsignedByte();
          for (int index = 0; index <= morphCount; index++) {
            morphIds.add(normalizeDefinitionId(reader.readUnsignedShort()));
          }
        }
        case 107 -> clickable = false;
        default -> throw new IllegalStateException(
            "Unsupported npc definition opcode " + opcode + " for npc " + npcId
        );
      }
    }
  }

  private static int normalizeDefinitionId(int value) {
    return value == 65535 ? -1 : value;
  }
}
