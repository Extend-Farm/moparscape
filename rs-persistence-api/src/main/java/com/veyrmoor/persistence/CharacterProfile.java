package com.veyrmoor.persistence;

import com.veyrmoor.model.StaffRole;

public record CharacterProfile(
    StaffRole staffRole,
    boolean member,
    int runEnergy,
    Integer lastLoginDay,
    long gameTimeCounter,
    long gameCountCounter
) {

  public CharacterProfile {
    staffRole = staffRole == null ? StaffRole.NONE : staffRole;
    if (runEnergy < 0 || runEnergy > 100) {
      throw new IllegalArgumentException("Run energy must be between 0 and 100");
    }
  }

  public static CharacterProfile defaults() {
    return new CharacterProfile(StaffRole.NONE, false, 100, null, 0L, 0L);
  }
}
