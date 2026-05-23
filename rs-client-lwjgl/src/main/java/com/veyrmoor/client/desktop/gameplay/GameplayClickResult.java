package com.veyrmoor.client.desktop.gameplay;

public record GameplayClickResult(
    boolean handled,
    MovementDelta movementDelta,
    boolean closeClientRequested,
    boolean logoutRequested
) {

  public static GameplayClickResult ignored() {
    return new GameplayClickResult(false, null, false, false);
  }

  public static GameplayClickResult handledClick() {
    return new GameplayClickResult(true, null, false, false);
  }

  public static GameplayClickResult move(int deltaX, int deltaY) {
    return new GameplayClickResult(true, new MovementDelta(deltaX, deltaY), false, false);
  }

  public static GameplayClickResult closeClient() {
    return new GameplayClickResult(true, null, true, false);
  }

  public static GameplayClickResult logout() {
    return new GameplayClickResult(true, null, false, true);
  }

  public boolean hasMovementDelta() {
    return movementDelta != null;
  }

  public boolean requestsClientClose() {
    return closeClientRequested;
  }

  public boolean requestsLogout() {
    return logoutRequested;
  }

  public record MovementDelta(int deltaX, int deltaY) {
  }
}
