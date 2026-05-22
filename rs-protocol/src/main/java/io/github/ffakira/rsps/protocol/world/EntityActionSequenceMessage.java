package io.github.ffakira.rsps.protocol.world;

import io.github.ffakira.rsps.protocol.ServerMessage;

public record EntityActionSequenceMessage(int entityId, int actionSequenceId) implements ServerMessage {

  public EntityActionSequenceMessage {
    actionSequenceId = Math.max(-1, actionSequenceId);
  }
}
