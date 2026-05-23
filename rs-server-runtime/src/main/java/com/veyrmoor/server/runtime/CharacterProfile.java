package com.veyrmoor.server.runtime;

import com.veyrmoor.model.StaffRole;

public record CharacterProfile(
    StaffRole staffRole,
    boolean member,
    int messageCount,
    int runEnergy,
    int gameTime,
    int gameCount
) {

  public static CharacterProfile minimal() {
    return new CharacterProfile(StaffRole.NONE, false, 0, 100, 0, 0);
  }
}
