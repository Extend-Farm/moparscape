package io.github.ffakira.rsps.server.runtime;

import java.util.List;

public record CharacterAppearance(List<Integer> lookSlots) {

  public CharacterAppearance {
    lookSlots = List.copyOf(lookSlots);
  }

  public static CharacterAppearance minimal() {
    return new CharacterAppearance(List.of());
  }
}
