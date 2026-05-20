package io.github.ffakira.rsps.client.desktop.character;

import io.github.ffakira.rsps.cache.AnimationFrameCatalog;
import io.github.ffakira.rsps.cache.RawModelRepository;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObjectGeometry;
import io.github.ffakira.rsps.content.AnimationSequenceCatalog;
import io.github.ffakira.rsps.content.AnimationSequenceDefinition;
import io.github.ffakira.rsps.content.IdentityKitDefinitionCatalog;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import io.github.ffakira.rsps.protocol.BootstrapAppearance;
import io.github.ffakira.rsps.protocol.BootstrapItemSlot;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CharacterModelAssembler {

  private static final float ACTOR_FOOTPRINT = 0.66f;
  private static final float ACTOR_HEIGHT = 1.74f;

  private final CharacterModelSourceBuilder sourceBuilder;
  private final AnimationSequenceCatalog animationSequenceCatalog;
  private final CharacterLightingResolver lightingResolver;
  private final CharacterGeometryBuilder geometryBuilder;
  private final Map<String, CharacterPreparedModel> preparedModelByAppearanceKey = new HashMap<>();
  private final Map<String, WorldSceneObjectGeometry> geometryByAppearanceKey = new HashMap<>();
  private Float cachedMaleReferenceScale;
  private Float cachedFemaleReferenceScale;

  // This assembler mirrors the legacy appearance contract without importing legacy runtime code:
  // persisted look values are sex + palette choices, while the visible body comes from default
  // identity kits plus wearable item body models resolved from the native cache layer.
  public CharacterModelAssembler(
      ItemDefinitionCatalog itemDefinitions,
      IdentityKitDefinitionCatalog identityKitDefinitions,
      RawModelRepository rawModelRepository
  ) {
    this(itemDefinitions, identityKitDefinitions, rawModelRepository, null, null);
  }

  public CharacterModelAssembler(
      ItemDefinitionCatalog itemDefinitions,
      IdentityKitDefinitionCatalog identityKitDefinitions,
      RawModelRepository rawModelRepository,
      AnimationSequenceCatalog animationSequenceCatalog,
      AnimationFrameCatalog animationFrameCatalog
  ) {
    this.sourceBuilder = new CharacterModelSourceBuilder(itemDefinitions, identityKitDefinitions, rawModelRepository);
    this.animationSequenceCatalog = animationSequenceCatalog;
    CharacterFrameAnimator frameAnimator = animationFrameCatalog == null ? null : new CharacterFrameAnimator(animationFrameCatalog);
    this.lightingResolver = new CharacterLightingResolver();
    this.geometryBuilder = new CharacterGeometryBuilder(animationSequenceCatalog, frameAnimator);
  }

  public AnimationSequenceCatalog animationSequenceCatalog() {
    return animationSequenceCatalog;
  }

  public WorldSceneObjectGeometry assemble(BootstrapAppearance appearance, List<BootstrapItemSlot> equipment) {
    String cacheKey = buildCacheKey(appearance, equipment, CharacterModelSourceBuilder.SequenceEquipmentOverrides.none());
    return geometryByAppearanceKey.computeIfAbsent(
        cacheKey,
        ignored -> geometryBuilder.build(
            preparedModelByAppearanceKey.computeIfAbsent(
                cacheKey,
                unused -> prepareModel(appearance, equipment, CharacterModelSourceBuilder.SequenceEquipmentOverrides.none())
            ),
            ActorAnimationState.idle()
        )
    );
  }

  public WorldSceneObjectGeometry assemble(
      BootstrapAppearance appearance,
      List<BootstrapItemSlot> equipment,
      ActorAnimationState animationState
  ) {
    if (animationState == null || animationState.isIdle() && animationState.activeFrameId() < 0) {
      return assemble(appearance, equipment);
    }
    CharacterModelSourceBuilder.SequenceEquipmentOverrides equipmentOverrides = resolveSequenceEquipmentOverrides(animationState);
    String cacheKey = buildCacheKey(appearance, equipment, equipmentOverrides);
    return geometryBuilder.build(
        preparedModelByAppearanceKey.computeIfAbsent(cacheKey, unused -> prepareModel(appearance, equipment, equipmentOverrides)),
        animationState
    );
  }

  private CharacterPreparedModel prepareModel(
      BootstrapAppearance appearance,
      List<BootstrapItemSlot> equipment,
      CharacterModelSourceBuilder.SequenceEquipmentOverrides equipmentOverrides
  ) {
    int[] lookValues = sourceBuilder.normalizedLookValues(appearance);
    boolean female = lookValues[0] == 1;
    CharacterModelSourceBuilder.PreparedCharacterSource preparedCharacterSource =
        sourceBuilder.prepareSourceModel(lookValues, equipment, equipmentOverrides);
    if (preparedCharacterSource == null) {
      return null;
    }
    List<PreparedContributionLighting> preparedLighting = lightingResolver.prepareMergedLighting(
        preparedCharacterSource.preparedContributions(),
        lookValues
    );
    CharacterActorTransform actorTransform = resolveActorTransform(preparedCharacterSource.sourceBounds(), female);
    CharacterModelSourceBuilder.SourceBounds sourceBounds = preparedCharacterSource.sourceBounds();
    return new CharacterPreparedModel(
        preparedCharacterSource.preparedContributions(),
        preparedLighting,
        actorTransform,
        new CharacterActorBounds(
            sourceBounds.minX() * actorTransform.scale() + actorTransform.offsetX(),
            sourceBounds.minY() * actorTransform.scale() + actorTransform.offsetY(),
            sourceBounds.minZ() * actorTransform.scale() + actorTransform.offsetZ(),
            sourceBounds.maxX() * actorTransform.scale() + actorTransform.offsetX(),
            sourceBounds.maxY() * actorTransform.scale() + actorTransform.offsetY(),
            sourceBounds.maxZ() * actorTransform.scale() + actorTransform.offsetZ()
        )
    );
  }

  private String buildCacheKey(
      BootstrapAppearance appearance,
      List<BootstrapItemSlot> equipment,
      CharacterModelSourceBuilder.SequenceEquipmentOverrides equipmentOverrides
  ) {
    StringBuilder builder = new StringBuilder();
    if (appearance != null) {
      for (Integer lookValue : appearance.lookValues()) {
        builder.append(lookValue).append(':');
      }
    }
    builder.append('|');
    for (BootstrapItemSlot equipmentSlot : equipment) {
      builder.append(equipmentSlot.slotIndex()).append('=').append(equipmentSlot.itemId()).append(';');
    }
    builder.append("|anim:");
    builder.append(equipmentOverrides.mainhandAppearanceId()).append(':').append(equipmentOverrides.offhandAppearanceId());
    return builder.toString();
  }

  private CharacterActorTransform resolveActorTransform(CharacterModelSourceBuilder.SourceBounds sourceBounds, boolean female) {
    float scale = referenceActorScale(female);
    float offsetX = -sourceBounds.centerX() * scale;
    float offsetZ = -sourceBounds.centerZ() * scale;
    float offsetY = -sourceBounds.minY() * scale;
    return new CharacterActorTransform(scale, offsetX, offsetY, offsetZ);
  }

  private float referenceActorScale(boolean female) {
    if (female) {
      if (cachedFemaleReferenceScale == null) {
        cachedFemaleReferenceScale = computeReferenceActorScale(true);
      }
      return cachedFemaleReferenceScale;
    }
    if (cachedMaleReferenceScale == null) {
      cachedMaleReferenceScale = computeReferenceActorScale(false);
    }
    return cachedMaleReferenceScale;
  }

  private float computeReferenceActorScale(boolean female) {
    int[] referenceLookValues = sourceBuilder.normalizedLookValues(null);
    referenceLookValues[0] = female ? 1 : 0;
    CharacterModelSourceBuilder.PreparedCharacterSource referenceSource = sourceBuilder.prepareSourceModel(
        referenceLookValues,
        List.of(),
        CharacterModelSourceBuilder.SequenceEquipmentOverrides.none()
    );
    if (referenceSource == null) {
      return 1.0f;
    }
    CharacterModelSourceBuilder.SourceBounds sourceBounds = referenceSource.sourceBounds();
    float sourceWidth = Math.max(0.01f, sourceBounds.maxX() - sourceBounds.minX());
    float sourceDepth = Math.max(0.01f, sourceBounds.maxZ() - sourceBounds.minZ());
    float sourceHeight = Math.max(0.01f, sourceBounds.maxY() - sourceBounds.minY());
    return Math.min(ACTOR_FOOTPRINT / Math.max(sourceWidth, sourceDepth), ACTOR_HEIGHT / sourceHeight);
  }

  private CharacterModelSourceBuilder.SequenceEquipmentOverrides resolveSequenceEquipmentOverrides(
      ActorAnimationState animationState
  ) {
    if (animationState == null || animationState.actionSequenceId() < 0 || animationSequenceCatalog == null) {
      return CharacterModelSourceBuilder.SequenceEquipmentOverrides.none();
    }
    AnimationSequenceDefinition sequence = animationSequenceCatalog.find(animationState.actionSequenceId()).orElse(null);
    if (sequence == null) {
      return CharacterModelSourceBuilder.SequenceEquipmentOverrides.none();
    }
    return new CharacterModelSourceBuilder.SequenceEquipmentOverrides(
        sequence.playerMainhandAppearanceId(),
        sequence.playerOffhandAppearanceId()
    );
  }
}
