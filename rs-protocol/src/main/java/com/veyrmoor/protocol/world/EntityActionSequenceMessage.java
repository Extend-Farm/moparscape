package com.veyrmoor.protocol.world;

import com.veyrmoor.protocol.ServerMessage;

public record EntityActionSequenceMessage(int entityId, int actionSequenceId) implements ServerMessage {

  public EntityActionSequenceMessage {
    actionSequenceId = Math.max(-1, actionSequenceId);
  }
}
