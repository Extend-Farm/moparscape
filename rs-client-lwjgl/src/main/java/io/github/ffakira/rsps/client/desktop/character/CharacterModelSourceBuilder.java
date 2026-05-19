package io.github.ffakira.rsps.client.desktop.character;

import io.github.ffakira.rsps.cache.RawModelData;
import io.github.ffakira.rsps.cache.RawModelRepository;
import io.github.ffakira.rsps.content.IdentityKitDefinition;
import io.github.ffakira.rsps.content.IdentityKitDefinitionCatalog;
import io.github.ffakira.rsps.content.ItemDefinition;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import io.github.ffakira.rsps.protocol.BootstrapAppearance;
import io.github.ffakira.rsps.protocol.BootstrapItemSlot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class CharacterModelSourceBuilder {

  private static final int[] DEFAULT_LOOK_VALUES = {0, 7, 8, 9, 5, 0};

  private static final int SLOT_HAT = 0;
  private static final int SLOT_CAPE = 1;
  private static final int SLOT_AMULET = 2;
  private static final int SLOT_WEAPON = 3;
  private static final int SLOT_CHEST = 4;
  private static final int SLOT_SHIELD = 5;
  private static final int SLOT_LEGS = 7;
  private static final int SLOT_HANDS = 9;
  private static final int SLOT_FEET = 10;

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
    boolean female = lookValues[0] == 1;
    Map<Integer, Integer> equipmentBySlot = equipmentBySlot(equipment);

    ArrayList<ModelContribution> contributions = new ArrayList<>();
    addEquipmentContribution(contributions, equipmentBySlot.get(SLOT_HAT), female);
    addEquipmentContribution(contributions, equipmentBySlot.get(SLOT_CAPE), female);
    addEquipmentContribution(contributions, equipmentBySlot.get(SLOT_AMULET), female);
    addEquipmentContribution(contributions, equipmentBySlot.get(SLOT_WEAPON), female);
    addChestContribution(contributions, equipmentBySlot.get(SLOT_CHEST), female);
    addEquipmentContribution(contributions, equipmentBySlot.get(SLOT_SHIELD), female);
    if (!EquipmentVisibilityRules.isPlateBody(equipmentBySlot.getOrDefault(SLOT_CHEST, -1))) {
      addIdentityKitContribution(contributions, identityKitDefinitions.defaultBodyKitId(2, female));
    }
    addLegsContribution(contributions, equipmentBySlot.get(SLOT_LEGS), female);
    if (!EquipmentVisibilityRules.isFullHelm(equipmentBySlot.getOrDefault(SLOT_HAT, -1))
        && !EquipmentVisibilityRules.isFullMask(equipmentBySlot.getOrDefault(SLOT_HAT, -1))) {
      addIdentityKitContribution(contributions, identityKitDefinitions.defaultBodyKitId(0, female));
    }
    addHandsContribution(contributions, equipmentBySlot.get(SLOT_HANDS), female);
    addFeetContribution(contributions, equipmentBySlot.get(SLOT_FEET), female);

    if (contributions.isEmpty()) {
      return null;
    }

    ArrayList<PreparedContribution> preparedContributions = new ArrayList<>();
    float minX = Float.POSITIVE_INFINITY;
    float minY = Float.POSITIVE_INFINITY;
    float minZ = Float.POSITIVE_INFINITY;
    float maxX = Float.NEGATIVE_INFINITY;
    float maxY = Float.NEGATIVE_INFINITY;
    float maxZ = Float.NEGATIVE_INFINITY;
    for (ModelContribution contribution : contributions) {
      RawModelData rawModelData = contribution.rawModelData();
      float[][] sourceVertices = extractContributionVertices(rawModelData, contribution.translateY());
      preparedContributions.add(new PreparedContribution(
          contribution,
          sourceVertices[0],
          sourceVertices[1],
          sourceVertices[2]
      ));
      for (int vertexIndex = 0; vertexIndex < rawModelData.vertexCount(); vertexIndex++) {
        minX = Math.min(minX, sourceVertices[0][vertexIndex]);
        minY = Math.min(minY, sourceVertices[1][vertexIndex]);
        minZ = Math.min(minZ, sourceVertices[2][vertexIndex]);
        maxX = Math.max(maxX, sourceVertices[0][vertexIndex]);
        maxY = Math.max(maxY, sourceVertices[1][vertexIndex]);
        maxZ = Math.max(maxZ, sourceVertices[2][vertexIndex]);
      }
    }
    return new PreparedCharacterSource(
        preparedContributions,
        new SourceBounds(minX, minY, minZ, maxX, maxY, maxZ)
    );
  }

  private void addChestContribution(List<ModelContribution> contributions, Integer itemId, boolean female) {
    if (itemId != null && itemId >= 0) {
      if (addEquipmentContribution(contributions, itemId, female)) {
        return;
      }
    }
    addIdentityKitContribution(contributions, identityKitDefinitions.defaultBodyKitId(1, female));
  }

  private void addLegsContribution(List<ModelContribution> contributions, Integer itemId, boolean female) {
    if (itemId != null && itemId >= 0 && addEquipmentContribution(contributions, itemId, female)) {
      return;
    }
    addIdentityKitContribution(contributions, identityKitDefinitions.defaultBodyKitId(4, female));
  }

  private void addHandsContribution(List<ModelContribution> contributions, Integer itemId, boolean female) {
    if (itemId != null && itemId >= 0 && addEquipmentContribution(contributions, itemId, female)) {
      return;
    }
    addIdentityKitContribution(contributions, identityKitDefinitions.defaultBodyKitId(3, female));
  }

  private void addFeetContribution(List<ModelContribution> contributions, Integer itemId, boolean female) {
    if (itemId != null && itemId >= 0 && addEquipmentContribution(contributions, itemId, female)) {
      return;
    }
    addIdentityKitContribution(contributions, identityKitDefinitions.defaultBodyKitId(5, female));
  }

  private boolean addEquipmentContribution(List<ModelContribution> contributions, Integer itemId, boolean female) {
    if (itemId == null || itemId < 0) {
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

  private Map<Integer, Integer> equipmentBySlot(List<BootstrapItemSlot> equipment) {
    HashMap<Integer, Integer> equipmentBySlot = new HashMap<>();
    for (BootstrapItemSlot equipmentSlot : equipment) {
      if (equipmentSlot.itemId() >= 0) {
        equipmentBySlot.put(equipmentSlot.slotIndex(), equipmentSlot.itemId());
      }
    }
    return equipmentBySlot;
  }

  private float[][] extractContributionVertices(RawModelData rawModelData, int translateY) {
    int vertexCount = rawModelData.vertexCount();
    float[] worldX = new float[vertexCount];
    float[] worldY = new float[vertexCount];
    float[] worldZ = new float[vertexCount];
    for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
      float x = rawModelData.vertexX()[vertexIndex] / 128.0f;
      float y = -(rawModelData.vertexY()[vertexIndex] + translateY) / 128.0f;
      float z = rawModelData.vertexZ()[vertexIndex] / 128.0f;
      worldX[vertexIndex] = x;
      worldY[vertexIndex] = y;
      worldZ[vertexIndex] = z;
    }
    return new float[][]{worldX, worldY, worldZ};
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
      float[] sourceX,
      float[] sourceY,
      float[] sourceZ
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
}
