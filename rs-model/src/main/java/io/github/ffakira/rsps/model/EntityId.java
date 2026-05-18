package io.github.ffakira.rsps.model;

public record EntityId(int value) {

  public EntityId {
    if (value <= 0) {
      throw new IllegalArgumentException("Entity id must be positive");
    }
  }
}
