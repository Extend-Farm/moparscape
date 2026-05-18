package io.github.ffakira.rsps.model;

public record WorldPoint(int x, int y, int plane) {

  public WorldPoint {
    if (plane < 0) {
      throw new IllegalArgumentException("Plane cannot be negative");
    }
  }

  public WorldPoint translate(int deltaX, int deltaY) {
    return new WorldPoint(x + deltaX, y + deltaY, plane);
  }
}
