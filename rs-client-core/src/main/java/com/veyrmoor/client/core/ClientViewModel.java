package com.veyrmoor.client.core;

import com.veyrmoor.model.WorldPoint;
import com.veyrmoor.protocol.bootstrap.CharacterBootstrapPayload;
import java.util.List;

public record ClientViewModel(
    String statusText,
    boolean loggedIn,
    WorldPoint localPlayerPosition,
    int localPlayerActionSequenceId,
    Long accountId,
    Long characterId,
    String regionKey,
    CharacterBootstrapPayload bootstrap,
    BootstrapCharacterPresentation bootstrapPresentation,
    List<ClientChatMessage> chatMessages
) {

  public ClientViewModel(String statusText, boolean loggedIn, WorldPoint localPlayerPosition) {
    this(statusText, loggedIn, localPlayerPosition, -1, null, null, null, null, null, List.of());
  }

  public ClientViewModel(
      String statusText,
      boolean loggedIn,
      WorldPoint localPlayerPosition,
      Long accountId,
      Long characterId,
      String regionKey,
      CharacterBootstrapPayload bootstrap,
      BootstrapCharacterPresentation bootstrapPresentation
  ) {
    this(
        statusText,
        loggedIn,
        localPlayerPosition,
        -1,
        accountId,
        characterId,
        regionKey,
        bootstrap,
        bootstrapPresentation,
        List.of()
    );
  }

  public ClientViewModel {
    localPlayerActionSequenceId = Math.max(-1, localPlayerActionSequenceId);
    chatMessages = List.copyOf(chatMessages == null ? List.of() : chatMessages);
  }

  public List<com.veyrmoor.protocol.bootstrap.BootstrapItemSlot> inventory() {
    return bootstrap == null ? List.of() : bootstrap.inventory();
  }

  public List<com.veyrmoor.protocol.bootstrap.BootstrapItemSlot> equipment() {
    return bootstrap == null ? List.of() : bootstrap.equipment();
  }

  public EquipmentLoadout equipmentLoadout() {
    return EquipmentLoadout.from(equipment());
  }

  public List<com.veyrmoor.protocol.bootstrap.BootstrapSkill> skills() {
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
