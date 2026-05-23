package com.veyrmoor.server.runtime;

import com.veyrmoor.model.WorldPoint;
import com.veyrmoor.persistence.AccountRecord;
import com.veyrmoor.persistence.CharacterSnapshot;
import java.util.List;
import java.util.Objects;

public record CharacterBootstrap(
    AccountRecord accountRecord,
    CharacterSnapshot characterSnapshot,
    CharacterProfile profile,
    CharacterAppearance appearance,
    List<CharacterSkill> skills,
    List<CharacterItemSlot> equipment,
    List<CharacterItemSlot> inventory,
    List<CharacterItemSlot> bank,
    List<Long> friends,
    List<Long> ignores
) {

  public CharacterBootstrap {
    Objects.requireNonNull(accountRecord, "accountRecord");
    Objects.requireNonNull(characterSnapshot, "characterSnapshot");
    profile = profile == null ? CharacterProfile.minimal() : profile;
    appearance = appearance == null ? CharacterAppearance.minimal() : appearance;
    skills = List.copyOf(skills);
    equipment = List.copyOf(equipment);
    inventory = List.copyOf(inventory);
    bank = List.copyOf(bank);
    friends = List.copyOf(friends);
    ignores = List.copyOf(ignores);
  }

  public static CharacterBootstrap minimal(AccountRecord accountRecord, CharacterSnapshot characterSnapshot) {
    return new CharacterBootstrap(
        accountRecord,
        characterSnapshot,
        CharacterProfile.minimal(),
        CharacterAppearance.minimal(),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of()
    );
  }

  public String displayName() {
    return characterSnapshot.displayName();
  }

  public WorldPoint worldPoint() {
    return characterSnapshot.worldPoint();
  }

  public String regionKey() {
    WorldPoint worldPoint = worldPoint();
    return (worldPoint.x() >> 6) + "_" + (worldPoint.y() >> 6) + "_" + worldPoint.plane();
  }
}
