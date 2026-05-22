package io.github.ffakira.rsps.server.runtime;

import io.github.ffakira.rsps.persistence.CharacterItemSlot;
import io.github.ffakira.rsps.persistence.CharacterSkill;
import io.github.ffakira.rsps.persistence.CharacterSnapshot;
import io.github.ffakira.rsps.protocol.bootstrap.BootstrapAppearance;
import io.github.ffakira.rsps.protocol.bootstrap.BootstrapAnimationProfile;
import io.github.ffakira.rsps.protocol.bootstrap.BootstrapItemSlot;
import io.github.ffakira.rsps.protocol.bootstrap.BootstrapProfile;
import io.github.ffakira.rsps.protocol.bootstrap.BootstrapSkill;
import io.github.ffakira.rsps.protocol.bootstrap.CharacterBootstrapMessage;
import io.github.ffakira.rsps.protocol.bootstrap.CharacterBootstrapPayload;
import io.github.ffakira.rsps.protocol.world.EntityPositionMessage;
import io.github.ffakira.rsps.protocol.session.LoginAccepted;
import io.github.ffakira.rsps.protocol.ServerMessage;
import io.github.ffakira.rsps.protocol.world.WorldSnapshotMessage;
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
            characterSnapshot.profile().rights(),
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
