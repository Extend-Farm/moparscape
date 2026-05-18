package io.github.ffakira.rsps.persistence;

public record CharacterProfile(
    short rights,
    boolean member,
    int runEnergy,
    Integer lastLoginDay,
    long gameTimeCounter,
    long gameCountCounter
) {

  public CharacterProfile {
    if (runEnergy < 0 || runEnergy > 100) {
      throw new IllegalArgumentException("Run energy must be between 0 and 100");
    }
  }

  public static CharacterProfile defaults() {
    return new CharacterProfile((short) 0, false, 100, null, 0L, 0L);
  }
}
