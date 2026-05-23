package com.veyrmoor.server.runtime;

import com.veyrmoor.model.EntityId;
import com.veyrmoor.persistence.AccountRecord;
import com.veyrmoor.persistence.CharacterSnapshot;
import java.util.UUID;

public record SessionState(
    UUID sessionId,
    AccountRecord account,
    CharacterSnapshot character,
    EntityId entityId,
    int actionSequenceId
) {

  public SessionState withActionSequenceId(int actionSequenceId) {
    return new SessionState(sessionId, account, character, entityId, Math.max(-1, actionSequenceId));
  }
}
