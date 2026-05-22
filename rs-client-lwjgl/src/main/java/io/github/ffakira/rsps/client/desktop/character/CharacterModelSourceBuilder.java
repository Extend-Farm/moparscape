package io.github.ffakira.rsps.client.desktop.character;

import io.github.ffakira.rsps.cache.RawModelData;
import io.github.ffakira.rsps.cache.RawModelRepository;
import io.github.ffakira.rsps.content.IdentityKitDefinition;
import io.github.ffakira.rsps.content.IdentityKitDefinitionCatalog;
import io.github.ffakira.rsps.content.ItemDefinition;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import io.github.ffakira.rsps.protocol.bootstrap.BootstrapAppearance;
import io.github.ffakira.rsps.protocol.bootstrap.BootstrapItemSlot;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

final class CharacterModelSourceBuilder {

  private static final int[] DEFAULT_LOOK_VALUES = {0, 7, 8, 9, 5, 0};
  // The legacy server persists only sex + five recolor slots. The visible body therefore still
  // starts from the fixed default kit ids that its appearance block writes for unequipped players.
  private static final int[] MALE_DEFAULT_BODY_KIT_IDS = {7, 25, 29, 35, 39, 44};
  private static final int[] FEMALE_DEFAULT_BODY_KIT_IDS = {8, 11, 12, 13, 14, 15};

  private static final int SLOT_HAT = 0;
  private static final int SLOT_CAPE = 1;
  private static final int SLOT_AMULET = 2;
  private static final int SLOT_WEAPON = 3;
  private static final int SLOT_CHEST = 4;
  private static final int SLOT_SHIELD = 5;
  private static final int SLOT_ARMS = 6;
  private static final int SLOT_LEGS = 7;
  private static final int SLOT_HEAD = 8;
  private static final int SLOT_HANDS = 9;
  private static final int SLOT_FEET = 10;
  private static final int SLOT_JAW = 11;

  private static final int BODY_PART_HEAD = 0;
  private static final int BODY_PART_TORSO = 1;
  private static final int BODY_PART_ARMS = 2;
  private static final int BODY_PART_HANDS = 3;
  private static final int BODY_PART_LEGS = 4;
  private static final int BODY_PART_FEET = 5;
  private static final int IDENTITY_KIT_APPEARANCE_BASE = 0x100;
  private static final int ITEM_APPEARANCE_BASE = 0x200;

  private final ItemDefinitionCatalog itemDefinitions;
  private final IdentityKitDefinitionCatalog identityKitDefinitions;
  private final RawModelRepository rawModelRepository;

  CharacterModelSourceBuilder(
      ItemDefinitionCatalog itemDefinitions,
      IdentityKitDefinitionCatalog identityKitDefinitions,
      RawModelRepository rawModelRepository
  ) {
    this.itemDefinitions = itemDefinitions;
    this.identityKitDefinitions = identityKitDefinitions;
    this.rawModelRepository = rawModelRepository;
  }

  int[] normalizedLookValues(BootstrapAppearance appearance) {
    int[] lookValues = DEFAULT_LOOK_VALUES.clone();
    if (appearance == null) {
      return lookValues;
    }
    List<Integer> sourceValues = appearance.lookValues();
    for (int index = 0; index < Math.min(lookValues.length, sourceValues.size()); index++) {
      Integer value = sourceValues.get(index);
      if (value != null && value >= 0) {
        lookValues[index] = value;
      }
    }
    return lookValues;
  }

  PreparedCharacterSource prepareSourceModel(int[] lookValues, List<BootstrapItemSlot> equipment) {
    return prepareSourceModel(lookValues, equipment, SequenceEquipmentOverrides.none());
  }

  PreparedCharacterSource prepareSourceModel(
      int[] lookValues,
      List<BootstrapItemSlot> equipment,
      SequenceEquipmentOverrides equipmentOverrides
  ) {
    boolean female = lookValues[0] == 1;
    int[] appearanceEntries = resolveAppearanceEntries(lookValues, equipment, equipmentOverrides);

    ArrayList<ModelContribution> contributions = new ArrayList<>(appearanceEntries.length);
    for (int appearanceEntry : appearanceEntries) {
      addAppearanceContribution(contributions, appearanceEntry, female);
    }

    if (contributions.isEmpty()) {
      return null;
    }

    ArrayList<PreparedContribution> preparedContributions = new ArrayList<>(contributions.size());
    int minSourceX = Integer.MAX_VALUE;
    int minSourceY = Integer.MAX_VALUE;
    int minSourceZ = Integer.MAX_VALUE;
    int maxSourceX = Integer.MIN_VALUE;
    int maxSourceY = Integer.MIN_VALUE;
    int maxSourceZ = Integer.MIN_VALUE;
    for (ModelContribution contribution : contributions) {
      RawModelData rawModelData = contribution.rawModelData();
      int[] modelX = rawModelData.vertexX().clone();
      int[] modelY = rawModelData.vertexY().clone();
      int[] modelZ = rawModelData.vertexZ().clone();
      int translateY = contribution.translateY();
      if (translateY != 0) {
        for (int vertexIndex = 0; vertexIndex < modelY.length; vertexIndex++) {
          modelY[vertexIndex] += translateY;
        }
      }
      preparedContributions.add(new PreparedContribution(
          contribution,
          modelX,
          modelY,
          modelZ
      ));
      for (int vertexIndex = 0; vertexIndex < rawModelData.vertexCount(); vertexIndex++) {
        minSourceX = Math.min(minSourceX, modelX[vertexIndex]);
        minSourceY = Math.min(minSourceY, -modelY[vertexIndex]);
        minSourceZ = Math.min(minSourceZ, modelZ[vertexIndex]);
        maxSourceX = Math.max(maxSourceX, modelX[vertexIndex]);
        maxSourceY = Math.max(maxSourceY, -modelY[vertexIndex]);
        maxSourceZ = Math.max(maxSourceZ, modelZ[vertexIndex]);
      }
    }
    return new PreparedCharacterSource(
        preparedContributions,
        new SourceBounds(
            toSourceCoordinate(minSourceX),
            toSourceCoordinate(minSourceY),
            toSourceCoordinate(minSourceZ),
            toSourceCoordinate(maxSourceX),
            toSourceCoordinate(maxSourceY),
            toSourceCoordinate(maxSourceZ)
        )
    );
  }

  int[] resolveAppearanceEntries(
      int[] lookValues,
      List<BootstrapItemSlot> equipment,
      SequenceEquipmentOverrides equipmentOverrides
  ) {
    boolean female = lookValues[0] == 1;
    int[] equipmentItemIdsBySlot = equipmentItemIdsBySlot(equipment);
    int[] bodyKitIds = female ? FEMALE_DEFAULT_BODY_KIT_IDS : MALE_DEFAULT_BODY_KIT_IDS;
    int[] appearanceEntries = new int[12];
    int hatItemId = equipmentItemIdsBySlot[SLOT_HAT];
    int chestItemId = equipmentItemIdsBySlot[SLOT_CHEST];
    appearanceEntries[SLOT_HAT] = itemAppearanceEntry(hatItemId);
    appearanceEntries[SLOT_CAPE] = itemAppearanceEntry(equipmentItemIdsBySlot[SLOT_CAPE]);
    appearanceEntries[SLOT_AMULET] = itemAppearanceEntry(equipmentItemIdsBySlot[SLOT_AMULET]);
    appearanceEntries[SLOT_WEAPON] = itemAppearanceEntry(equipmentItemIdsBySlot[SLOT_WEAPON]);
    appearanceEntries[SLOT_CHEST] = itemOrKitAppearanceEntry(chestItemId, bodyKitIds[BODY_PART_TORSO]);
    appearanceEntries[SLOT_SHIELD] = itemAppearanceEntry(equipmentItemIdsBySlot[SLOT_SHIELD]);
    appearanceEntries[SLOT_ARMS] = EquipmentVisibilityRules.isPlateBody(chestItemId)
        ? 0
        : identityKitAppearanceEntry(bodyKitIds[BODY_PART_ARMS]);
    appearanceEntries[SLOT_LEGS] = itemOrKitAppearanceEntry(
        equipmentItemIdsBySlot[SLOT_LEGS],
        bodyKitIds[BODY_PART_LEGS]
    );
    appearanceEntries[SLOT_HEAD] = EquipmentVisibilityRules.isFullHelm(hatItemId)
        || EquipmentVisibilityRules.isFullMask(hatItemId)
        ? 0
        : identityKitAppearanceEntry(bodyKitIds[BODY_PART_HEAD]);
    appearanceEntries[SLOT_HANDS] = itemOrKitAppearanceEntry(
        equipmentItemIdsBySlot[SLOT_HANDS],
        bodyKitIds[BODY_PART_HANDS]
    );
    appearanceEntries[SLOT_FEET] = itemOrKitAppearanceEntry(
        equipmentItemIdsBySlot[SLOT_FEET],
        bodyKitIds[BODY_PART_FEET]
    );
    appearanceEntries[SLOT_JAW] = 0;
    if (equipmentOverrides.mainhandAppearanceId() >= 0) {
      appearanceEntries[SLOT_WEAPON] = equipmentOverrides.mainhandAppearanceId();
    }
    if (equipmentOverrides.offhandAppearanceId() >= 0) {
      appearanceEntries[SLOT_SHIELD] = equipmentOverrides.offhandAppearanceId();
    }
    return appearanceEntries;
  }

  private void addAppearanceContribution(List<ModelContribution> contributions, int appearanceEntry, boolean female) {
    if (appearanceEntry >= IDENTITY_KIT_APPEARANCE_BASE && appearanceEntry < ITEM_APPEARANCE_BASE) {
      addIdentityKitContribution(contributions, appearanceEntry - IDENTITY_KIT_APPEARANCE_BASE);
      return;
    }
    if (appearanceEntry >= ITEM_APPEARANCE_BASE) {
      addEquipmentContribution(contributions, appearanceEntry - ITEM_APPEARANCE_BASE, female);
    }
  }

  private int itemOrKitAppearanceEntry(int itemId, int bodyKitId) {
    return itemId >= 0 ? itemAppearanceEntry(itemId) : identityKitAppearanceEntry(bodyKitId);
  }

  private int itemAppearanceEntry(int itemId) {
    return itemId < 0 ? 0 : ITEM_APPEARANCE_BASE + itemId;
  }

  private int identityKitAppearanceEntry(int bodyKitId) {
    return bodyKitId < 0 ? 0 : IDENTITY_KIT_APPEARANCE_BASE + bodyKitId;
  }

  private boolean addEquipmentContribution(List<ModelContribution> contributions, int itemId, boolean female) {
    if (itemId < 0) {
      return false;
    }
    ItemDefinition definition = itemDefinitions.find(itemId).orElse(null);
    if (definition == null) {
      return false;
    }
    List<Integer> bodyModelIds = definition.bodyModelIds(female);
    if (bodyModelIds.isEmpty()) {
      return false;
    }
    for (Integer modelId : bodyModelIds) {
      contributions.add(new ModelContribution(
          rawModelRepository.loadModel(modelId),
          definition.recolorSources(),
          definition.recolorTargets(),
          definition.bodyOffsetY(female)
      ));
    }
    return true;
  }

  private void addIdentityKitContribution(List<ModelContribution> contributions, int kitId) {
    if (kitId < 0) {
      return;
    }
    IdentityKitDefinition definition = identityKitDefinitions.require(kitId);
    for (Integer modelId : definition.bodyModelIds()) {
      contributions.add(new ModelContribution(
          rawModelRepository.loadModel(modelId),
          definition.recolorSources(),
          definition.recolorTargets(),
          0
      ));
    }
  }

  private int[] equipmentItemIdsBySlot(List<BootstrapItemSlot> equipment) {
    int[] equipmentItemIdsBySlot = new int[12];
    Arrays.fill(equipmentItemIdsBySlot, -1);
    for (BootstrapItemSlot equipmentSlot : equipment) {
      int slotIndex = equipmentSlot.slotIndex();
      if (slotIndex >= 0 && slotIndex < equipmentItemIdsBySlot.length && equipmentSlot.itemId() >= 0) {
        equipmentItemIdsBySlot[slotIndex] = equipmentSlot.itemId();
      }
    }
    return equipmentItemIdsBySlot;
  }

  private float toSourceCoordinate(int value) {
    return value / 128.0f;
  }

  record ModelContribution(
      RawModelData rawModelData,
      List<Integer> recolorSources,
      List<Integer> recolorTargets,
      int translateY
  ) {
  }

  // Body-part models already live in one shared model space. Normalizing each contribution
  // independently makes the head, torso, and legs all expand to full actor size and destroys the
  // silhouette. The native path therefore captures source-space vertices per contribution and then
  // applies one shared actor transform across the assembled body.
  record PreparedContribution(
      ModelContribution contribution,
      int[] modelX,
      int[] modelY,
      int[] modelZ
  ) {
  }

  record PreparedCharacterSource(
      List<PreparedContribution> preparedContributions,
      SourceBounds sourceBounds
  ) {

    PreparedCharacterSource {
      preparedContributions = List.copyOf(preparedContributions);
    }
  }

  record SourceBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {

    float centerX() {
      return (minX + maxX) * 0.5f;
    }

    float centerZ() {
      return (minZ + maxZ) * 0.5f;
    }
  }

  record SequenceEquipmentOverrides(int mainhandAppearanceId, int offhandAppearanceId) {

    static SequenceEquipmentOverrides none() {
      return new SequenceEquipmentOverrides(-1, -1);
    }
  }
}
