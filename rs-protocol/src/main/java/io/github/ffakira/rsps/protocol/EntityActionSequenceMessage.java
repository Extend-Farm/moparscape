package io.github.ffakira.rsps.protocol;

public record EntityActionSequenceMessage(int entityId, int actionSequenceId) implements ServerMessage {

  public EntityActionSequenceMessage {
    actionSequenceId = Math.max(-1, actionSequenceId);
  }
}
