package io.github.ffakira.rsps.client.desktop.gameplay;

import io.github.ffakira.rsps.model.WorldPoint;

public record GameplayMenuAction(Kind kind, WorldPoint targetWorldPoint) {

  public static GameplayMenuAction cancel() {
    return new GameplayMenuAction(Kind.CANCEL, null);
  }

  public static GameplayMenuAction walkTo(WorldPoint targetWorldPoint) {
    return new GameplayMenuAction(Kind.WALK_TO_WORLD_POINT, targetWorldPoint);
  }

  public enum Kind {
    CANCEL,
    WALK_TO_WORLD_POINT
  }
}
