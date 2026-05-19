package io.github.ffakira.rsps.client.desktop.gameplay;

public record GameplayClickResult(boolean handled, MovementDelta movementDelta) {

  public static GameplayClickResult ignored() {
    return new GameplayClickResult(false, null);
  }

  public static GameplayClickResult handledClick() {
    return new GameplayClickResult(true, null);
  }

  public static GameplayClickResult move(int deltaX, int deltaY) {
    return new GameplayClickResult(true, new MovementDelta(deltaX, deltaY));
  }

  public boolean hasMovementDelta() {
    return movementDelta != null;
  }

  public record MovementDelta(int deltaX, int deltaY) {
  }
}
