package com.veyrmoor.client.desktop.character;

import com.veyrmoor.cache.AnimationFrameCatalog;
import com.veyrmoor.cache.RawModelRepository;
import com.veyrmoor.client.desktop.world.object.WorldSceneObjectGeometry;
import com.veyrmoor.content.AnimationSequenceCatalog;
import com.veyrmoor.content.AnimationSequenceDefinition;
import com.veyrmoor.content.IdentityKitDefinitionCatalog;
import com.veyrmoor.content.ItemDefinitionCatalog;
import com.veyrmoor.protocol.bootstrap.BootstrapAppearance;
import com.veyrmoor.protocol.bootstrap.BootstrapItemSlot;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CharacterModelAssembler {

  private final CharacterModelSourceBuilder sourceBuilder;
  private final AnimationSequenceCatalog animationSequenceCatalog;
  private final CharacterLightingResolver lightingResolver;
  private final CharacterGeometryBuilder geometryBuilder;
  private final Map<String, CharacterPreparedModel> preparedModelByAppearanceKey = new HashMap<>();
  private final Map<String, WorldSceneObjectGeometry> geometryByAppearanceKey = new HashMap<>();

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
    CharacterModelSourceBuilder.PreparedCharacterSource preparedCharacterSource =
        sourceBuilder.prepareSourceModel(lookValues, equipment, equipmentOverrides);
    if (preparedCharacterSource == null) {
      return null;
    }
    List<PreparedContributionLighting> preparedLighting = lightingResolver.prepareMergedLighting(
        preparedCharacterSource.preparedContributions(),
        lookValues
    );
    CharacterActorTransform actorTransform = resolveActorTransform(preparedCharacterSource.sourceBounds());
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

  private CharacterActorTransform resolveActorTransform(CharacterModelSourceBuilder.SourceBounds sourceBounds) {
    // The reference player merge keeps the authored model-space origin, including the slight
    // below-origin sole overhang some equipped bodies retain after the merged cache model is lit.
    return new CharacterActorTransform(1.0f, 0.0f, 0.0f, 0.0f);
  }

  private CharacterModelSourceBuilder.SequenceEquipmentOverrides resolveSequenceEquipmentOverrides(
      ActorAnimationState animationState
  ) {
    if (animationState == null || animationSequenceCatalog == null) {
      return CharacterModelSourceBuilder.SequenceEquipmentOverrides.none();
    }
    int activeSequenceId = animationState.activeSequenceId();
    if (activeSequenceId < 0) {
      return CharacterModelSourceBuilder.SequenceEquipmentOverrides.none();
    }
    // The reference player model pulls slot-3/5 overrides from whichever sequence is currently
    // driving the rendered frame, not only from forced/action animations.
    AnimationSequenceDefinition sequence = animationSequenceCatalog.find(activeSequenceId).orElse(null);
    if (sequence == null) {
      return CharacterModelSourceBuilder.SequenceEquipmentOverrides.none();
    }
    return new CharacterModelSourceBuilder.SequenceEquipmentOverrides(
        sequence.playerMainhandAppearanceId(),
        sequence.playerOffhandAppearanceId()
    );
  }
}
