package io.github.ffakira.rsps.client.desktop.core;

record GameplayClickResult(boolean handled, MovementDelta movementDelta) {

  static GameplayClickResult ignored() {
    return new GameplayClickResult(false, null);
  }

  static GameplayClickResult handledClick() {
    return new GameplayClickResult(true, null);
  }

  static GameplayClickResult move(int deltaX, int deltaY) {
    return new GameplayClickResult(true, new MovementDelta(deltaX, deltaY));
  }

  boolean hasMovementDelta() {
    return movementDelta != null;
  }

  record MovementDelta(int deltaX, int deltaY) {
  }
}
