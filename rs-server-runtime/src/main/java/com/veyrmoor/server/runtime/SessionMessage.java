package com.veyrmoor.server.runtime;

import com.veyrmoor.persistence.AccountRecord;
import com.veyrmoor.protocol.ClientMessage;
import com.veyrmoor.protocol.session.HandshakeAccepted;
import com.veyrmoor.sim.WorldEvent;
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

  record PublicChatBroadcastMessage(String speakerDisplayName, String text) implements SessionMessage {
  }

  record CloseSessionMessage(String reason) implements SessionMessage {
  }
}
