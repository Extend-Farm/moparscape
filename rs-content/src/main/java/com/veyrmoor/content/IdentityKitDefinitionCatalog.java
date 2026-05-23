package com.veyrmoor.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class IdentityKitDefinitionCatalog {

  private static final String IDENTITY_KIT_ENTRY = "idk.dat";

  private final IdentityKitDefinition[] definitionsById;

  private IdentityKitDefinitionCatalog(IdentityKitDefinition[] definitionsById) {
    this.definitionsById = definitionsById;
  }

  public static IdentityKitDefinitionCatalog load(ContentManifest manifest) {
    ContentArchiveSnapshot archiveSnapshot = new ContentArchiveCatalog().load(manifest);
    return parse(archiveSnapshot.readConfigEntry(IDENTITY_KIT_ENTRY));
  }

  public static IdentityKitDefinitionCatalog parse(byte[] bytes) {
    ContentDataReader reader = new ContentDataReader(bytes);
    int count = reader.readUnsignedShort();
    IdentityKitDefinition[] definitions = new IdentityKitDefinition[count];
    for (int id = 0; id < count; id++) {
      definitions[id] = parseDefinition(id, reader);
    }
    return new IdentityKitDefinitionCatalog(definitions);
  }

  public Optional<IdentityKitDefinition> find(int id) {
    if (id < 0 || id >= definitionsById.length) {
      return Optional.empty();
    }
    return Optional.ofNullable(definitionsById[id]);
  }

  public IdentityKitDefinition require(int id) {
    return find(id).orElseThrow(() -> new IllegalArgumentException("Unknown identity kit " + id));
  }

  public int defaultBodyKitId(int bodyPartIndex, boolean female) {
    int targetBodyPartId = targetVisibleBodyPartId(bodyPartIndex, female);
    if (targetBodyPartId < 0) {
      return -1;
    }
    for (int id = 0; id < definitionsById.length; id++) {
      IdentityKitDefinition definition = definitionsById[id];
      if (definition == null || definition.nonSelectable()) {
        continue;
      }
      if (definition.bodyPartId() == targetBodyPartId) {
        return id;
      }
    }
    return -1;
  }

  private int targetVisibleBodyPartId(int bodyPartIndex, boolean female) {
    int baseBodyPartId = switch (bodyPartIndex) {
      case 0 -> 0;
      case 1 -> 2;
      case 2 -> 3;
      case 3 -> 4;
      case 4 -> 5;
      case 5 -> 6;
      default -> -1;
    };
    if (baseBodyPartId < 0) {
      return -1;
    }
    return female ? baseBodyPartId + 7 : baseBodyPartId;
  }

  private static IdentityKitDefinition parseDefinition(int id, ContentDataReader reader) {
    int bodyPartId = -1;
    boolean nonSelectable = false;
    List<Integer> bodyModelIds = new ArrayList<>();
    List<Integer> recolorSources = new ArrayList<>();
    List<Integer> recolorTargets = new ArrayList<>();
    while (true) {
      int opcode = reader.readUnsignedByte();
      if (opcode == 0) {
        return new IdentityKitDefinition(id, bodyPartId, bodyModelIds, recolorSources, recolorTargets, nonSelectable);
      }
      switch (opcode) {
        case 1 -> bodyPartId = reader.readUnsignedByte();
        case 2 -> {
          int modelCount = reader.readUnsignedByte();
          for (int index = 0; index < modelCount; index++) {
            bodyModelIds.add(reader.readUnsignedShort());
          }
        }
        case 3 -> nonSelectable = true;
        case 40, 41, 42, 43, 44, 45, 46, 47, 48, 49 -> recolorSources.add(reader.readUnsignedShort());
        case 50, 51, 52, 53, 54, 55, 56, 57, 58, 59 -> recolorTargets.add(reader.readUnsignedShort());
        case 60, 61, 62, 63, 64, 65, 66, 67, 68, 69 -> reader.readUnsignedShort();
        default -> throw new IllegalStateException("Unsupported identity kit opcode " + opcode + " for id " + id);
      }
    }
  }
}
