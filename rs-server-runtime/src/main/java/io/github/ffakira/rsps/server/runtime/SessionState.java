package io.github.ffakira.rsps.server.runtime;

import io.github.ffakira.rsps.model.EntityId;
import io.github.ffakira.rsps.persistence.AccountRecord;
import io.github.ffakira.rsps.persistence.CharacterSnapshot;
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
