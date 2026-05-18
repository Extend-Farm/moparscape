package io.github.ffakira.rsps.client.core;

import io.github.ffakira.rsps.model.WorldPoint;
import io.github.ffakira.rsps.protocol.CharacterBootstrapPayload;
import java.util.List;

public record ClientViewModel(
    String statusText,
    boolean loggedIn,
    WorldPoint localPlayerPosition,
    Long accountId,
    Long characterId,
    String regionKey,
    CharacterBootstrapPayload bootstrap,
    BootstrapCharacterPresentation bootstrapPresentation
) {

  public ClientViewModel(String statusText, boolean loggedIn, WorldPoint localPlayerPosition) {
    this(statusText, loggedIn, localPlayerPosition, null, null, null, null, null);
  }

  public List<io.github.ffakira.rsps.protocol.BootstrapItemSlot> inventory() {
    return bootstrap == null ? List.of() : bootstrap.inventory();
  }

  public List<io.github.ffakira.rsps.protocol.BootstrapItemSlot> equipment() {
    return bootstrap == null ? List.of() : bootstrap.equipment();
  }

  public List<io.github.ffakira.rsps.protocol.BootstrapSkill> skills() {
    return bootstrap == null ? List.of() : bootstrap.skills();
  }

  public List<BootstrapInventoryItemPresentation> inventoryPresentation() {
    return bootstrapPresentation == null ? List.of() : bootstrapPresentation.inventory();
  }

  public List<BootstrapEquipmentItemPresentation> equipmentPresentation() {
    return bootstrapPresentation == null ? List.of() : bootstrapPresentation.equipment();
  }

  public List<BootstrapSkillPresentation> skillPresentation() {
    return bootstrapPresentation == null ? List.of() : bootstrapPresentation.skills();
  }
}
