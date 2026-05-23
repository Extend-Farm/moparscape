package com.veyrmoor.server.runtime;

import com.veyrmoor.persistence.CharacterItemSlot;
import com.veyrmoor.persistence.CharacterSkill;
import com.veyrmoor.persistence.CharacterSnapshot;
import com.veyrmoor.protocol.bootstrap.BootstrapAppearance;
import com.veyrmoor.protocol.bootstrap.BootstrapAnimationProfile;
import com.veyrmoor.protocol.bootstrap.BootstrapItemSlot;
import com.veyrmoor.protocol.bootstrap.BootstrapProfile;
import com.veyrmoor.protocol.bootstrap.BootstrapSkill;
import com.veyrmoor.protocol.bootstrap.CharacterBootstrapMessage;
import com.veyrmoor.protocol.bootstrap.CharacterBootstrapPayload;
import com.veyrmoor.protocol.world.EntityPositionMessage;
import com.veyrmoor.protocol.session.LoginAccepted;
import com.veyrmoor.protocol.ServerMessage;
import com.veyrmoor.protocol.world.WorldSnapshotMessage;
import java.util.List;

final class BootstrapMessageBatch {

  private BootstrapMessageBatch() {
  }

  static List<ServerMessage> create(WorldShardAdmission admission) {
    CharacterSnapshot characterSnapshot = admission.characterSnapshot();
    CharacterBootstrapPayload bootstrap = new CharacterBootstrapPayload(
        characterSnapshot.accountId().value(),
        characterSnapshot.id().value(),
        characterSnapshot.displayName(),
        admission.regionKey(),
        characterSnapshot.worldPoint(),
        new BootstrapProfile(
            characterSnapshot.profile().staffRole(),
            characterSnapshot.profile().member(),
            characterSnapshot.profile().runEnergy()
        ),
        new BootstrapAppearance(characterSnapshot.appearance().lookValues(), BootstrapAnimationProfile.referencePlayer()),
        mapItemSlots(characterSnapshot.inventorySlots()),
        mapItemSlots(characterSnapshot.equipmentSlots()),
        mapSkills(characterSnapshot.skills())
    );
    return List.of(
        new LoginAccepted(characterSnapshot.accountId().value(), characterSnapshot.id().value(), admission.spawnPoint()),
        new CharacterBootstrapMessage(bootstrap),
        new WorldSnapshotMessage(admission.regionKey(), admission.spawnPoint()),
        new EntityPositionMessage(admission.entityId().value(), admission.spawnPoint())
    );
  }

  private static List<BootstrapItemSlot> mapItemSlots(List<CharacterItemSlot> itemSlots) {
    return itemSlots.stream()
        .map(itemSlot -> new BootstrapItemSlot(itemSlot.slotIndex(), itemSlot.itemId(), itemSlot.quantity()))
        .toList();
  }

  private static List<BootstrapSkill> mapSkills(List<CharacterSkill> skills) {
    return skills.stream()
        .map(skill -> new BootstrapSkill(skill.skillId(), skill.currentLevel(), skill.experience()))
        .toList();
  }
}
