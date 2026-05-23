package com.veyrmoor.persistence;

import java.util.List;

public record CharacterAppearance(List<Integer> lookValues) {

  public CharacterAppearance {
    lookValues = List.copyOf(lookValues);
  }

  public static CharacterAppearance defaults() {
    return new CharacterAppearance(List.of(-1, -1, -1, -1, -1, -1));
  }
}
