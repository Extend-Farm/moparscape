package io.github.ffakira.rsps.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class IdentityKitDefinitionCatalog {

  private static final String IDENTITY_KIT_ENTRY = "idk.dat";

  private static final int[] DEFAULT_MALE_KIT_IDS = {7, 25, 29, 35, 39, 44};

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
    if (!female && bodyPartIndex >= 0 && bodyPartIndex < DEFAULT_MALE_KIT_IDS.length) {
      return DEFAULT_MALE_KIT_IDS[bodyPartIndex];
    }
    int targetBodyPartId = female ? bodyPartIndex + 7 : bodyPartIndex;
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
