package io.github.ffakira.rsps.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ObjectDefinitionCatalog {

  private static final String OBJECT_INDEX_ENTRY = "loc.idx";
  private static final String OBJECT_DATA_ENTRY = "loc.dat";
  private static final int MAX_SCENE_MORPH_DEPTH = 8;

  private final ObjectDefinition[] definitionsById;
  private final ObjectDefinition[] sceneResolvedDefinitionsById;
  private final int definitionCount;

  private ObjectDefinitionCatalog(
      ObjectDefinition[] definitionsById,
      ObjectDefinition[] sceneResolvedDefinitionsById,
      int definitionCount
  ) {
    this.definitionsById = definitionsById;
    this.sceneResolvedDefinitionsById = sceneResolvedDefinitionsById;
    this.definitionCount = definitionCount;
  }

  public static ObjectDefinitionCatalog empty() {
    return new ObjectDefinitionCatalog(new ObjectDefinition[0], new ObjectDefinition[0], 0);
  }

  public static ObjectDefinitionCatalog load(ContentManifest manifest) {
    ContentArchiveSnapshot archiveSnapshot = new ContentArchiveCatalog().load(manifest);
    return parse(archiveSnapshot.readConfigEntry(OBJECT_INDEX_ENTRY), archiveSnapshot.readConfigEntry(OBJECT_DATA_ENTRY));
  }

  public static ObjectDefinitionCatalog parse(byte[] indexBytes, byte[] dataBytes) {
    ContentDataReader indexReader = new ContentDataReader(indexBytes);
    int objectCount = indexReader.readUnsignedShort();
    int[] offsetsById = new int[objectCount];
    int offset = 2;
    for (int objectId = 0; objectId < objectCount; objectId++) {
      offsetsById[objectId] = offset;
      offset += indexReader.readUnsignedShort();
    }

    ObjectDefinition[] definitionsById = new ObjectDefinition[objectCount];
    for (int objectId = 0; objectId < objectCount; objectId++) {
      ContentDataReader dataReader = new ContentDataReader(dataBytes);
      dataReader.position(offsetsById[objectId]);
      definitionsById[objectId] = MutableObjectDefinition.parse(objectId, dataReader).toDefinition();
    }
    return new ObjectDefinitionCatalog(definitionsById, new ObjectDefinition[objectCount], objectCount);
  }

  public int size() {
    return definitionCount;
  }

  public Optional<ObjectDefinition> find(int objectId) {
    if (objectId < 0 || objectId >= definitionsById.length) {
      return Optional.empty();
    }
    return Optional.ofNullable(definitionsById[objectId]);
  }

  public ObjectDefinition require(int objectId) {
    return find(objectId)
        .orElseThrow(() -> new IllegalArgumentException("Unknown object definition " + objectId));
  }

  public ObjectDefinition resolveSceneDefinition(int objectId) {
    ObjectDefinition cachedDefinition = sceneResolvedDefinitionsById[objectId];
    if (cachedDefinition != null) {
      return cachedDefinition;
    }
    ObjectDefinition resolvedDefinition = resolveSceneDefinition(require(objectId), 0);
    sceneResolvedDefinitionsById[objectId] = resolvedDefinition;
    return resolvedDefinition;
  }

  private ObjectDefinition resolveSceneDefinition(ObjectDefinition definition, int depth) {
    if (depth >= MAX_SCENE_MORPH_DEPTH || definition.morphIds().isEmpty()) {
      return definition;
    }

    ObjectDefinition firstResolvedChild = null;
    for (int morphId : definition.morphIds()) {
      if (morphId < 0) {
        continue;
      }
      ObjectDefinition resolvedChild = find(morphId)
          .map(child -> resolveSceneDefinition(child, depth + 1))
          .orElse(null);
      if (resolvedChild == null) {
        continue;
      }
      if (firstResolvedChild == null) {
        firstResolvedChild = resolvedChild;
      }
      if (!resolvedChild.modelIds().isEmpty()) {
        // The native scene path does not have live varbit/varp state yet. For opcode 77 morph
        // containers, prefer the first child that can actually render so the viewport keeps the
        // authored prop instead of dropping to a placeholder proxy.
        return resolvedChild;
      }
    }
    return firstResolvedChild == null ? definition : firstResolvedChild;
  }

  private static final class MutableObjectDefinition {
    private final int id;
    private final List<Integer> modelIds = new ArrayList<>();
    private final List<Integer> modelTypes = new ArrayList<>();
    private final List<Integer> morphIds = new ArrayList<>();
    private final List<Integer> recolorSources = new ArrayList<>();
    private final List<Integer> recolorTargets = new ArrayList<>();
    private String name = "";
    private int sizeX = 1;
    private int sizeY = 1;
    private boolean solid = true;
    private boolean impenetrable = true;
    private boolean interactive = false;
    private boolean contouredGround = false;
    private boolean mirrored = false;
    private boolean castsShadow = true;
    private boolean obstructsGround = false;
    private int decorDisplacement = 16;
    private int mapSceneId = -1;
    private int mapFunctionId = -1;
    private int blockingMask = 0;
    private boolean hasInteractiveFlag = false;
    private boolean hasActions = false;
    private boolean hollow = false;
    private int ambient = 0;
    private int contrast = 0;
    private int scaleX = 128;
    private int scaleY = 128;
    private int scaleZ = 128;
    private int translateX = 0;
    private int translateY = 0;
    private int translateZ = 0;
    private int animationId = -1;
    private int morphVarBitId = -1;
    private int morphVarpId = -1;

    private MutableObjectDefinition(int id) {
      this.id = id;
    }

    static MutableObjectDefinition parse(int id, ContentDataReader reader) {
      MutableObjectDefinition definition = new MutableObjectDefinition(id);
      while (true) {
        int opcode = reader.readUnsignedByte();
        if (opcode == 0) {
          return definition;
        }
        switch (opcode) {
          case 1 -> {
            int count = reader.readUnsignedByte();
            definition.modelIds.clear();
            definition.modelTypes.clear();
            for (int index = 0; index < count; index++) {
              definition.modelIds.add(reader.readUnsignedShort());
              definition.modelTypes.add(reader.readUnsignedByte());
            }
          }
          case 2 -> definition.name = reader.readString();
          case 3 -> reader.readByteArray();
          case 5 -> {
            int count = reader.readUnsignedByte();
            definition.modelIds.clear();
            definition.modelTypes.clear();
            for (int index = 0; index < count; index++) {
              definition.modelIds.add(reader.readUnsignedShort());
            }
          }
          case 14 -> definition.sizeX = reader.readUnsignedByte();
          case 15 -> definition.sizeY = reader.readUnsignedByte();
          case 17 -> definition.solid = false;
          case 18 -> definition.impenetrable = false;
          case 19 -> {
            definition.interactive = reader.readUnsignedByte() == 1;
            definition.hasInteractiveFlag = true;
          }
          case 21 -> definition.contouredGround = true;
          case 22, 23 -> {
            // Current native scene path still ignores delayed shading and occlusion flags.
          }
          case 24 -> {
            int rawAnimationId = reader.readUnsignedShort();
            definition.animationId = rawAnimationId == 65535 ? -1 : rawAnimationId;
          }
          case 28 -> definition.decorDisplacement = reader.readUnsignedByte();
          case 29 -> definition.ambient = reader.readSignedByte();
          case 39 -> definition.contrast = reader.readSignedByte();
          case 30, 31, 32, 33, 34 -> {
            String action = reader.readString();
            if (!"hidden".equalsIgnoreCase(action)) {
              definition.hasActions = true;
            }
          }
          case 35, 36, 37, 38 -> {
            String action = reader.readString();
            if (!"hidden".equalsIgnoreCase(action)) {
              definition.hasActions = true;
            }
          }
          case 40 -> {
            int recolorCount = reader.readUnsignedByte();
            for (int index = 0; index < recolorCount; index++) {
              definition.recolorSources.add(reader.readUnsignedShort());
              definition.recolorTargets.add(reader.readUnsignedShort());
            }
          }
          case 60 -> definition.mapFunctionId = reader.readUnsignedShort();
          case 62 -> definition.mirrored = true;
          case 64 -> definition.castsShadow = false;
          case 65, 66, 67, 68 -> {
            int value = reader.readUnsignedShort();
            switch (opcode) {
              case 65 -> definition.scaleX = value;
              case 66 -> definition.scaleY = value;
              case 67 -> definition.scaleZ = value;
              case 68 -> definition.mapSceneId = value;
              default -> throw new IllegalStateException("Unhandled scale opcode " + opcode);
            }
          }
          case 69 -> definition.blockingMask = reader.readUnsignedByte();
          case 70 -> definition.translateX = reader.readSignedShort();
          case 71 -> definition.translateY = reader.readSignedShort();
          case 72 -> definition.translateZ = reader.readSignedShort();
          case 73 -> definition.obstructsGround = true;
          case 74 -> definition.hollow = true;
          case 75 -> reader.readUnsignedByte();
          case 77 -> {
            int varBit = reader.readUnsignedShort();
            if (varBit == 65535) {
              varBit = -1;
            }
            int varp = reader.readUnsignedShort();
            if (varp == 65535) {
              varp = -1;
            }
            definition.morphVarBitId = varBit;
            definition.morphVarpId = varp;
            definition.morphIds.clear();
            int morphCount = reader.readUnsignedByte();
            for (int index = 0; index <= morphCount; index++) {
              int morphId = reader.readUnsignedShort();
              if (morphId == 65535) {
                morphId = -1;
              }
              definition.morphIds.add(morphId);
            }
          }
          default -> throw new IllegalStateException("Unsupported object definition opcode " + opcode + " for object " + id);
        }
      }
    }

    ObjectDefinition toDefinition() {
      boolean resolvedInteractive = interactive;
      if (!hasInteractiveFlag) {
        resolvedInteractive = (!modelIds.isEmpty() && (modelTypes.isEmpty() || modelTypes.getFirst() == 10)) || hasActions;
      }
      boolean resolvedSolid = hollow ? false : solid;
      boolean resolvedImpenetrable = hollow ? false : impenetrable;
      String resolvedName = name == null || name.isBlank() ? "object-" + id : name;
      return new ObjectDefinition(
          id,
          resolvedName,
          modelIds,
          modelTypes,
          morphVarBitId,
          morphVarpId,
          morphIds,
          recolorSources,
          recolorTargets,
          sizeX,
          sizeY,
          resolvedSolid,
          resolvedImpenetrable,
          resolvedInteractive,
          contouredGround,
          mirrored,
          castsShadow,
          obstructsGround,
          decorDisplacement,
          mapSceneId,
          mapFunctionId,
          blockingMask,
          ambient,
          contrast,
          scaleX,
          scaleY,
          scaleZ,
          translateX,
          translateY,
          translateZ,
          animationId
      );
    }
  }
}
