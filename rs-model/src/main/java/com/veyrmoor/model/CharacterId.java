package com.veyrmoor.model;

public record CharacterId(long value) {

  public CharacterId {
    if (value <= 0) {
      throw new IllegalArgumentException("Character id must be positive");
    }
  }
}
