package io.github.ffakira.rsps.server.runtime;

import io.github.ffakira.rsps.persistence.AccountRecord;
import io.github.ffakira.rsps.protocol.ClientMessage;
import io.github.ffakira.rsps.protocol.session.HandshakeAccepted;
import io.github.ffakira.rsps.sim.WorldEvent;
import java.util.List;

public sealed interface SessionMessage extends ActorMessage {

  record InboundClientMessage(ClientMessage message) implements SessionMessage {
  }

  record HandshakeCompletedMessage(HandshakeAccepted response) implements SessionMessage {
  }

  record LoginSucceededMessage(AccountRecord accountRecord, WorldShardAdmission admission) implements SessionMessage {
  }

  record LoginRejectedMessage(String reason, boolean closeSession) implements SessionMessage {
  }

  record WorldCommandAppliedMessage(List<WorldEvent> worldEvents) implements SessionMessage {
  }

  record CloseSessionMessage(String reason) implements SessionMessage {
  }
}
