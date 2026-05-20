package io.github.ffakira.rsps.client.desktop.character;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.cache.AnimationFrameCatalog;
import io.github.ffakira.rsps.cache.CacheStoreReader;
import io.github.ffakira.rsps.cache.RawModelRepository;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObjectGeometry;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneRasterMode;
import io.github.ffakira.rsps.content.AnimationSequenceCatalog;
import io.github.ffakira.rsps.content.AnimationSequenceDefinition;
import io.github.ffakira.rsps.content.ContentBootstrapService;
import io.github.ffakira.rsps.content.ContentManifest;
import io.github.ffakira.rsps.content.IdentityKitDefinitionCatalog;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import io.github.ffakira.rsps.content.TopLevelArchiveId;
import io.github.ffakira.rsps.protocol.BootstrapAppearance;
import io.github.ffakira.rsps.protocol.BootstrapItemSlot;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import org.junit.jupiter.api.Test;

class CharacterModelAssemblerTest {

  private static final int REFERENCE_WALK_SEQUENCE_ID = 819;
  private static final int IDENTITY_KIT_APPEARANCE_BASE = 0x100;
  private static final int ITEM_APPEARANCE_BASE = 0x200;

  @Test
  void assemblesNativeCharacterGeometryFromCacheBackedIdentityKits() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    CharacterModelAssembler assembler = new CharacterModelAssembler(
        ItemDefinitionCatalog.load(manifest),
        IdentityKitDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore())
    );

    WorldSceneObjectGeometry geometry = assembler.assemble(
        new BootstrapAppearance(List.of(-1, -1, -1, -1, -1, -1)),
        List.of()
    );

    assertThat(geometry).isNotNull();
    assertThat(geometry.vertexX()).isNotEmpty();
    assertThat(geometry.vertexY()).hasSameSizeAs(geometry.vertexX());
    assertThat(geometry.vertexZ()).hasSameSizeAs(geometry.vertexX());
    assertThat(geometry.faceVertexA()).isNotEmpty();
    assertThat(geometry.faceVertexB()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.faceVertexC()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.faceColorA()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.faceColorB()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.faceColorC()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.faceAlpha()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.faceRasterModes()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.faceTextureIds()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.textureVertexA()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.textureVertexB()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.textureVertexC()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.faceRasterModes()).contains(SceneRasterMode.GOURAUD);
    for (int faceIndex = 0; faceIndex < geometry.faceVertexA().length; faceIndex++) {
      assertThat(geometry.faceAlpha()[faceIndex]).isBetween(0, 255);
      if (geometry.faceRasterModes()[faceIndex] == SceneRasterMode.TEXTURED) {
        assertThat(geometry.faceTextureIds()[faceIndex]).isGreaterThanOrEqualTo(0);
        assertThat(geometry.textureVertexA()[faceIndex]).isGreaterThanOrEqualTo(0);
        assertThat(geometry.textureVertexB()[faceIndex]).isGreaterThanOrEqualTo(0);
        assertThat(geometry.textureVertexC()[faceIndex]).isGreaterThanOrEqualTo(0);
      } else {
        assertThat(geometry.faceTextureIds()[faceIndex]).isEqualTo(-1);
        assertThat(geometry.textureVertexA()[faceIndex]).isEqualTo(-1);
        assertThat(geometry.textureVertexB()[faceIndex]).isEqualTo(-1);
        assertThat(geometry.textureVertexC()[faceIndex]).isEqualTo(-1);
      }
    }
  }

  @Test
  void cachesGeometryByAppearanceAndEquipmentKey() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    CharacterModelAssembler assembler = new CharacterModelAssembler(
        ItemDefinitionCatalog.load(manifest),
        IdentityKitDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore())
    );
    BootstrapAppearance appearance = new BootstrapAppearance(List.of(-1, -1, -1, -1, -1, -1));

    WorldSceneObjectGeometry first = assembler.assemble(appearance, List.of());
    WorldSceneObjectGeometry second = assembler.assemble(appearance, List.of());

    assertThat(second).isSameAs(first);
  }

  @Test
  void appliesAWalkPoseWhenAnimationStateIsActive() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    CharacterModelAssembler assembler = new CharacterModelAssembler(
        ItemDefinitionCatalog.load(manifest),
        IdentityKitDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore())
    );
    BootstrapAppearance appearance = new BootstrapAppearance(List.of(-1, -1, -1, -1, -1, -1));

    WorldSceneObjectGeometry idleGeometry = assembler.assemble(appearance, List.of());
    WorldSceneObjectGeometry walkingGeometry = assembler.assemble(
        appearance,
        List.of(),
        new ActorAnimationState(1.0f, (float) (Math.PI / 2.0), 0.0f)
    );

    assertThat(walkingGeometry).isNotNull();
    assertThat(walkingGeometry.faceVertexA()).containsExactly(idleGeometry.faceVertexA());
    assertThat(walkingGeometry.faceVertexB()).containsExactly(idleGeometry.faceVertexB());
    assertThat(walkingGeometry.faceVertexC()).containsExactly(idleGeometry.faceVertexC());

    boolean anyAnimatedVertex = false;
    for (int index = 0; index < walkingGeometry.vertexY().length; index++) {
      if (Math.abs(walkingGeometry.vertexX()[index] - idleGeometry.vertexX()[index]) > 0.0001f
          || Math.abs(walkingGeometry.vertexY()[index] - idleGeometry.vertexY()[index]) > 0.0001f
          || Math.abs(walkingGeometry.vertexZ()[index] - idleGeometry.vertexZ()[index]) > 0.0001f) {
        anyAnimatedVertex = true;
        break;
      }
    }

    assertThat(anyAnimatedVertex).isTrue();
  }

  @Test
  void appliesDecodedReferenceFramesWhenAnActiveFrameIdIsPresent() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    AnimationFrameCatalog animationFrames = AnimationFrameCatalog.load(manifest.cacheStore());
    AnimationSequenceCatalog animationSequences = AnimationSequenceCatalog.load(manifest, animationFrames);
    CharacterModelAssembler assembler = new CharacterModelAssembler(
        ItemDefinitionCatalog.load(manifest),
        IdentityKitDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore()),
        animationSequences,
        animationFrames
    );
    BootstrapAppearance appearance = new BootstrapAppearance(List.of(-1, -1, -1, -1, -1, -1));
    AnimationSequenceDefinition walkSequence = animationSequences.require(REFERENCE_WALK_SEQUENCE_ID);
    int firstWalkFrameId = walkSequence.frameIdAt(0);

    WorldSceneObjectGeometry idleGeometry = assembler.assemble(appearance, List.of());
    WorldSceneObjectGeometry animatedGeometry = assembler.assemble(
        appearance,
        List.of(),
        new ActorAnimationState(
            1.0f,
            0.0f,
            0.0f,
            0.0f,
            ActorAnimationState.LocomotionMode.WALK_FORWARD,
            REFERENCE_WALK_SEQUENCE_ID,
            firstWalkFrameId,
            0.0f,
            0.0f
        )
    );

    assertThat(firstWalkFrameId).isGreaterThanOrEqualTo(0);
    assertThat(animatedGeometry).isNotNull();
    assertThat(animatedGeometry.faceVertexA()).containsExactly(idleGeometry.faceVertexA());
    assertThat(animatedGeometry.faceVertexB()).containsExactly(idleGeometry.faceVertexB());
    assertThat(animatedGeometry.faceVertexC()).containsExactly(idleGeometry.faceVertexC());
    assertThat(anyVertexMoved(idleGeometry, animatedGeometry)).isTrue();
  }

  @Test
  void preservesReferenceFaceLightingAcrossAnimationFrames() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    AnimationFrameCatalog animationFrames = AnimationFrameCatalog.load(manifest.cacheStore());
    AnimationSequenceCatalog animationSequences = AnimationSequenceCatalog.load(manifest, animationFrames);
    CharacterModelAssembler assembler = new CharacterModelAssembler(
        ItemDefinitionCatalog.load(manifest),
        IdentityKitDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore()),
        animationSequences,
        animationFrames
    );
    BootstrapAppearance appearance = new BootstrapAppearance(List.of(-1, -1, -1, -1, -1, -1));
    AnimationSequenceDefinition walkSequence = animationSequences.require(REFERENCE_WALK_SEQUENCE_ID);
    int firstWalkFrameId = walkSequence.frameIdAt(0);

    WorldSceneObjectGeometry idleGeometry = assembler.assemble(appearance, List.of());
    WorldSceneObjectGeometry animatedGeometry = assembler.assemble(
        appearance,
        List.of(),
        new ActorAnimationState(
            1.0f,
            0.0f,
            0.0f,
            0.0f,
            ActorAnimationState.LocomotionMode.WALK_FORWARD,
            REFERENCE_WALK_SEQUENCE_ID,
            firstWalkFrameId,
            0.0f,
            0.0f
        )
    );

    assertThat(animatedGeometry).isNotNull();
    assertThat(animatedGeometry.faceColorA()).containsExactly(idleGeometry.faceColorA());
    assertThat(animatedGeometry.faceColorB()).containsExactly(idleGeometry.faceColorB());
    assertThat(animatedGeometry.faceColorC()).containsExactly(idleGeometry.faceColorC());
  }

  @Test
  void blendsReferenceActionFramesWithMovementFramesUsingThePrimarySequenceInterleave() throws Exception {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    AnimationFrameCatalog animationFrames = AnimationFrameCatalog.load(manifest.cacheStore());
    AnimationSequenceCatalog animationSequences = AnimationSequenceCatalog.load(manifest, animationFrames);
    CharacterModelAssembler assembler = new CharacterModelAssembler(
        ItemDefinitionCatalog.load(manifest),
        IdentityKitDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore()),
        animationSequences,
        animationFrames
    );
    BootstrapAppearance appearance = new BootstrapAppearance(List.of(-1, -1, -1, -1, -1, -1));
    int movementFrameId = animationSequences.require(REFERENCE_WALK_SEQUENCE_ID).frameIdAt(0);

    ActionBlendCandidate blendCandidate = findBlendCandidate(
        animationSequences,
        assembler,
        appearance,
        movementFrameId
    );

    assertThat(blendCandidate).isNotNull();
    assertThat(blendCandidate.actionSequenceId()).isNotEqualTo(REFERENCE_WALK_SEQUENCE_ID);
    assertThat(blendCandidate.actionFrameId()).isGreaterThanOrEqualTo(0);
    assertThat(anyVertexMoved(blendCandidate.movementOnlyGeometry(), blendCandidate.blendedGeometry())).isTrue();
    assertThat(anyVertexMoved(blendCandidate.actionOnlyGeometry(), blendCandidate.blendedGeometry())).isTrue();
  }

  @Test
  void keepsTheAssembledActorOnAHumanSizedFootprint() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    CharacterModelAssembler assembler = new CharacterModelAssembler(
        ItemDefinitionCatalog.load(manifest),
        IdentityKitDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore())
    );

    WorldSceneObjectGeometry geometry = assembler.assemble(
        new BootstrapAppearance(List.of(-1, -1, -1, -1, -1, -1)),
        List.of()
    );

    assertThat(geometry).isNotNull();
    float minX = Float.POSITIVE_INFINITY;
    float minY = Float.POSITIVE_INFINITY;
    float minZ = Float.POSITIVE_INFINITY;
    float maxX = Float.NEGATIVE_INFINITY;
    float maxY = Float.NEGATIVE_INFINITY;
    float maxZ = Float.NEGATIVE_INFINITY;
    for (int index = 0; index < geometry.vertexX().length; index++) {
      minX = Math.min(minX, geometry.vertexX()[index]);
      minY = Math.min(minY, geometry.vertexY()[index]);
      minZ = Math.min(minZ, geometry.vertexZ()[index]);
      maxX = Math.max(maxX, geometry.vertexX()[index]);
      maxY = Math.max(maxY, geometry.vertexY()[index]);
      maxZ = Math.max(maxZ, geometry.vertexZ()[index]);
    }

    assertThat(maxX - minX).isLessThanOrEqualTo(0.70f);
    assertThat(maxZ - minZ).isLessThanOrEqualTo(0.70f);
    assertThat(maxY - minY).isBetween(1.5f, 1.82f);
    assertThat(minY).isBetween(-0.05f, 0.05f);
  }

  @Test
  void preservesEquipmentSilhouetteInsteadOfRescalingEveryAppearanceToOneHeight() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    CharacterModelAssembler assembler = new CharacterModelAssembler(
        ItemDefinitionCatalog.load(manifest),
        IdentityKitDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore())
    );
    BootstrapAppearance appearance = new BootstrapAppearance(List.of(-1, -1, -1, -1, -1, -1));

    WorldSceneObjectGeometry idleGeometry = assembler.assemble(appearance, List.of());
    WorldSceneObjectGeometry partyhatGeometry = assembler.assemble(
        appearance,
        List.of(new BootstrapItemSlot(0, 1048, 1))
    );

    assertThat(partyhatGeometry).isNotNull();
    assertThat(maxVertexY(partyhatGeometry) - minVertexY(partyhatGeometry))
        .isGreaterThan(maxVertexY(idleGeometry) - minVertexY(idleGeometry));
    assertThat(Math.abs(minVertexY(partyhatGeometry))).isLessThan(0.05f);
  }

  @Test
  void matchesLegacyMergedLightingForTheAmuletSlotAgainstPlatebodyEquipment() throws Exception {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    ItemDefinitionCatalog itemDefinitions = ItemDefinitionCatalog.load(manifest);
    IdentityKitDefinitionCatalog identityKitDefinitions = IdentityKitDefinitionCatalog.load(manifest);
    RawModelRepository rawModelRepository = new RawModelRepository(manifest.cacheStore());
    CharacterModelAssembler assembler = new CharacterModelAssembler(
        itemDefinitions,
        identityKitDefinitions,
        rawModelRepository
    );
    BootstrapAppearance appearance = new BootstrapAppearance(List.of(-1, -1, -1, -1, -1, -1));
    List<BootstrapItemSlot> equipment = List.of(
        new BootstrapItemSlot(2, 1712, 1),
        new BootstrapItemSlot(4, 1115, 1),
        new BootstrapItemSlot(7, 1067, 1),
        new BootstrapItemSlot(9, 6110, 1),
        new BootstrapItemSlot(10, 4121, 1)
    );

    WorldSceneObjectGeometry nativeGeometry = assembler.assemble(appearance, equipment);
    LegacyLitModel legacyModel = buildLegacyPlayerModel(
        manifest,
        itemDefinitions,
        identityKitDefinitions,
        rawModelRepository,
        appearance,
        equipment
    );
    int amuletFaceCount = summedFaceCount(
        rawModelRepository,
        itemDefinitions.require(1712).bodyModelIds(false)
    );

    assertThat(nativeGeometry).isNotNull();
    assertThat(legacyModel.faceColorA().length).isGreaterThanOrEqualTo(amuletFaceCount);
    for (int faceIndex = 0; faceIndex < amuletFaceCount; faceIndex++) {
      assertThat(nativeGeometry.faceColorA()[faceIndex]).isEqualTo(CharacterColorPalette.rgb(legacyModel.faceColorA()[faceIndex]));
      assertThat(nativeGeometry.faceColorB()[faceIndex]).isEqualTo(CharacterColorPalette.rgb(legacyModel.faceColorB()[faceIndex]));
      assertThat(nativeGeometry.faceColorC()[faceIndex]).isEqualTo(CharacterColorPalette.rgb(legacyModel.faceColorC()[faceIndex]));
    }
  }

  private float minVertexY(WorldSceneObjectGeometry geometry) {
    float minY = Float.POSITIVE_INFINITY;
    for (float y : geometry.vertexY()) {
      minY = Math.min(minY, y);
    }
    return minY;
  }

  private LegacyLitModel buildLegacyPlayerModel(
      ContentManifest manifest,
      ItemDefinitionCatalog itemDefinitions,
      IdentityKitDefinitionCatalog identityKitDefinitions,
      RawModelRepository rawModelRepository,
      BootstrapAppearance appearance,
      List<BootstrapItemSlot> equipment
  ) throws Exception {
    CharacterModelSourceBuilder sourceBuilder = new CharacterModelSourceBuilder(
        itemDefinitions,
        identityKitDefinitions,
        rawModelRepository
    );
    int[] lookValues = sourceBuilder.normalizedLookValues(appearance);
    int[] appearanceEntries = sourceBuilder.resolveAppearanceEntries(
        lookValues,
        equipment,
        CharacterModelSourceBuilder.SequenceEquipmentOverrides.none()
    );
    initializeLegacyPlayerContent(manifest, itemDefinitions, identityKitDefinitions, lookValues[0] == 1, appearanceEntries);

    Class<?> playerClass = Class.forName("io.github.ffakira.moparscape.client.Player");
    var playerConstructor = playerClass.getDeclaredConstructor();
    playerConstructor.setAccessible(true);
    Object legacyPlayer = playerConstructor.newInstance();
    setField(playerClass, legacyPlayer, "aBoolean1710", true);
    setField(playerClass, legacyPlayer, "anInt1702", lookValues[0]);
    setField(playerClass, legacyPlayer, "anIntArray1700", new int[5]);
    setField(playerClass, legacyPlayer, "anIntArray1717", toLegacyAppearanceValues(appearanceEntries));

    Method method452 = playerClass.getDeclaredMethod("method452", int.class);
    method452.setAccessible(true);
    Object legacyModel = method452.invoke(legacyPlayer, 0);
    Class<?> modelClass = Class.forName("io.github.ffakira.moparscape.client.Model");
    return new LegacyLitModel(
        ((int[]) getField(modelClass, legacyModel, "anIntArray1634")).clone(),
        ((int[]) getField(modelClass, legacyModel, "anIntArray1635")).clone(),
        ((int[]) getField(modelClass, legacyModel, "anIntArray1636")).clone()
    );
  }

  private void initializeLegacyPlayerContent(
      ContentManifest manifest,
      ItemDefinitionCatalog itemDefinitions,
      IdentityKitDefinitionCatalog identityKitDefinitions,
      boolean female,
      int[] appearanceEntries
  ) throws Exception {
    byte[] configArchiveBytes;
    try (CacheStoreReader reader = new CacheStoreReader(manifest.cacheStore())) {
      configArchiveBytes = reader.readArchive(0, TopLevelArchiveId.CONFIG.archiveId()).bytes();
    }
    Class<?> archiveClass = Class.forName("io.github.ffakira.moparscape.cache.Archive");
    Object configArchive = archiveClass.getConstructor(int.class, byte[].class).newInstance(44820, configArchiveBytes);
    Class<?> legacyItemDefinitionClass = Class.forName("io.github.ffakira.moparscape.client.ItemDefinition");
    Method itemBootstrap = legacyItemDefinitionClass.getDeclaredMethod("method193", archiveClass);
    itemBootstrap.setAccessible(true);
    itemBootstrap.invoke(null, configArchive);
    Class<?> legacyIdentityKitClass = Class.forName("io.github.ffakira.moparscape.client.IdentityKitDefinition");
    Method identityBootstrap = legacyIdentityKitClass.getDeclaredMethod("method535", int.class, archiveClass);
    identityBootstrap.setAccessible(true);
    identityBootstrap.invoke(null, 0, configArchive);

    Set<Integer> requiredModelIds = requiredLegacyModelIds(itemDefinitions, identityKitDefinitions, female, appearanceEntries);
    int maxModelId = requiredModelIds.stream().mapToInt(Integer::intValue).max().orElse(0);
    Class<?> resourceProviderClass = Class.forName("io.github.ffakira.moparscape.client.ResourceProvider");
    Object resourceProvider = resourceProviderClass.getDeclaredConstructor().newInstance();
    Class<?> modelClass = Class.forName("io.github.ffakira.moparscape.client.Model");
    Method modelHeaderBootstrap = modelClass.getDeclaredMethod("method459", int.class, resourceProviderClass);
    modelHeaderBootstrap.setAccessible(true);
    modelHeaderBootstrap.invoke(null, maxModelId + 1, resourceProvider);
    Method modelLoad = modelClass.getDeclaredMethod("method460", byte[].class, int.class, int.class);
    modelLoad.setAccessible(true);
    try (CacheStoreReader reader = new CacheStoreReader(manifest.cacheStore())) {
      for (Integer modelId : requiredModelIds) {
        byte[] modelBytes = reader.readArchive(1, modelId).bytes();
        modelLoad.invoke(null, decompressGzipIfNeeded(modelBytes), -4036, modelId);
      }
    }
  }

  private Set<Integer> requiredLegacyModelIds(
      ItemDefinitionCatalog itemDefinitions,
      IdentityKitDefinitionCatalog identityKitDefinitions,
      boolean female,
      int[] appearanceEntries
  ) {
    HashSet<Integer> modelIds = new HashSet<>();
    for (int appearanceEntry : appearanceEntries) {
      if (appearanceEntry >= ITEM_APPEARANCE_BASE) {
        modelIds.addAll(itemDefinitions.require(appearanceEntry - ITEM_APPEARANCE_BASE).bodyModelIds(female));
      } else if (appearanceEntry >= IDENTITY_KIT_APPEARANCE_BASE) {
        modelIds.addAll(identityKitDefinitions.require(appearanceEntry - IDENTITY_KIT_APPEARANCE_BASE).bodyModelIds());
      }
    }
    return modelIds;
  }

  private int[] toLegacyAppearanceValues(int[] appearanceEntries) {
    int[] legacyValues = new int[appearanceEntries.length];
    for (int index = 0; index < appearanceEntries.length; index++) {
      int appearanceEntry = appearanceEntries[index];
      if (appearanceEntry >= ITEM_APPEARANCE_BASE) {
        legacyValues[index] = appearanceEntry - ITEM_APPEARANCE_BASE + 512;
      } else if (appearanceEntry >= IDENTITY_KIT_APPEARANCE_BASE) {
        legacyValues[index] = appearanceEntry - IDENTITY_KIT_APPEARANCE_BASE + 256;
      } else {
        legacyValues[index] = 0;
      }
    }
    return legacyValues;
  }

  private int summedFaceCount(RawModelRepository rawModelRepository, List<Integer> modelIds) {
    int faceCount = 0;
    for (Integer modelId : modelIds) {
      faceCount += rawModelRepository.loadModel(modelId).faceCount();
    }
    return faceCount;
  }

  private byte[] decompressGzipIfNeeded(byte[] bytes) throws Exception {
    if (bytes.length < 2 || (bytes[0] & 0xff) != 0x1f || (bytes[1] & 0xff) != 0x8b) {
      return bytes;
    }
    try (GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(bytes))) {
      return gzipInputStream.readAllBytes();
    }
  }

  private void setField(Class<?> owner, Object target, String fieldName, Object value) throws Exception {
    Field field = owner.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private Object getField(Class<?> owner, Object target, String fieldName) throws Exception {
    Field field = owner.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  private float maxVertexY(WorldSceneObjectGeometry geometry) {
    float maxY = Float.NEGATIVE_INFINITY;
    for (float y : geometry.vertexY()) {
      maxY = Math.max(maxY, y);
    }
    return maxY;
  }

  private boolean anyVertexMoved(WorldSceneObjectGeometry before, WorldSceneObjectGeometry after) {
    for (int index = 0; index < before.vertexX().length; index++) {
      if (Math.abs(after.vertexX()[index] - before.vertexX()[index]) > 0.0001f
          || Math.abs(after.vertexY()[index] - before.vertexY()[index]) > 0.0001f
          || Math.abs(after.vertexZ()[index] - before.vertexZ()[index]) > 0.0001f) {
        return true;
      }
    }
    return false;
  }

  private ActionBlendCandidate findBlendCandidate(
      AnimationSequenceCatalog animationSequences,
      CharacterModelAssembler assembler,
      BootstrapAppearance appearance,
      int movementFrameId
  ) throws ReflectiveOperationException {
    WorldSceneObjectGeometry movementOnlyGeometry = assembler.assemble(
        appearance,
        List.of(),
        new ActorAnimationState(
            1.0f,
            0.0f,
            0.0f,
            0.0f,
            ActorAnimationState.LocomotionMode.WALK_FORWARD,
            REFERENCE_WALK_SEQUENCE_ID,
            movementFrameId,
            0.0f,
            0.0f
        )
    );
    for (Map.Entry<Integer, AnimationSequenceDefinition> entry : sequenceDefinitions(animationSequences).entrySet()) {
      AnimationSequenceDefinition sequence = entry.getValue();
      int actionSequenceId = entry.getKey();
      if (actionSequenceId == REFERENCE_WALK_SEQUENCE_ID || sequence.interleaveOrder() == null || sequence.frameCount() <= 0) {
        continue;
      }
      int actionFrameId = sequence.frameIdAt(0);
      if (actionFrameId < 0) {
        continue;
      }
      WorldSceneObjectGeometry actionOnlyGeometry = assembler.assemble(
          appearance,
          List.of(),
          new ActorAnimationState(
              0.0f,
              0.0f,
              0.0f,
              0.0f,
              ActorAnimationState.LocomotionMode.IDLE,
              -1,
              -1,
              actionSequenceId,
              actionFrameId,
              0.0f,
              0.0f
          )
      );
      WorldSceneObjectGeometry blendedGeometry = assembler.assemble(
          appearance,
          List.of(),
          new ActorAnimationState(
              1.0f,
              0.0f,
              0.0f,
              0.0f,
              ActorAnimationState.LocomotionMode.WALK_FORWARD,
              REFERENCE_WALK_SEQUENCE_ID,
              movementFrameId,
              actionSequenceId,
              actionFrameId,
              0.0f,
              0.0f
          )
      );
      if (actionOnlyGeometry != null
          && blendedGeometry != null
          && anyVertexMoved(movementOnlyGeometry, blendedGeometry)
          && anyVertexMoved(actionOnlyGeometry, blendedGeometry)) {
        return new ActionBlendCandidate(
            actionSequenceId,
            actionFrameId,
            movementOnlyGeometry,
            actionOnlyGeometry,
            blendedGeometry
        );
      }
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  private Map<Integer, AnimationSequenceDefinition> sequenceDefinitions(AnimationSequenceCatalog animationSequences)
      throws ReflectiveOperationException {
    Field field = AnimationSequenceCatalog.class.getDeclaredField("sequencesById");
    field.setAccessible(true);
    return (Map<Integer, AnimationSequenceDefinition>) field.get(animationSequences);
  }

  private record ActionBlendCandidate(
      int actionSequenceId,
      int actionFrameId,
      WorldSceneObjectGeometry movementOnlyGeometry,
      WorldSceneObjectGeometry actionOnlyGeometry,
      WorldSceneObjectGeometry blendedGeometry
  ) {
  }

  private record LegacyLitModel(int[] faceColorA, int[] faceColorB, int[] faceColorC) {
  }
}
