package io.github.ffakira.rsps.server.runtime;

public enum SessionPhase {
  CONNECTED,
  HANDSHAKE_IN_PROGRESS,
  READY_FOR_LOGIN,
  LOGIN_IN_PROGRESS,
  IN_WORLD,
  CLOSED
}
