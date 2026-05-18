package io.github.ffakira.rsps.persistence.sql;

import io.github.ffakira.rsps.model.WorldPoint;
import java.util.List;

record CharacterFileDocument(
    String username,
    String password,
    WorldPoint worldPoint,
    short rights,
    boolean member,
    int runEnergy,
    Integer lastLoginDay,
    long gameTimeCounter,
    long gameCountCounter,
    int[] lookValues,
    short[] skillLevels,
    int[] skillExperience,
    List<CharacterItemSlot> itemSlots,
    List<CharacterSocialLink> socialLinks
) {

  record CharacterItemSlot(short containerKind, short slotIndex, int itemId, int quantity) {
  }

  record CharacterSocialLink(short linkKind, long targetValue) {
  }
}
