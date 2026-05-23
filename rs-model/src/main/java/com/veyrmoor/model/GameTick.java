package com.veyrmoor.model;

public record GameTick(long value) {

  public GameTick {
    if (value < 0) {
      throw new IllegalArgumentException("Game tick cannot be negative");
    }
  }

  public static GameTick initial() {
    return new GameTick(0);
  }

  public GameTick next() {
    return new GameTick(value + 1);
  }
}
