package io.github.ffakira.rsps.server.runtime;

public record CharacterProfile(
    int rights,
    boolean member,
    int messageCount,
    int runEnergy,
    int gameTime,
    int gameCount
) {

  public static CharacterProfile minimal() {
    return new CharacterProfile(0, false, 0, 100, 0, 0);
  }
}
