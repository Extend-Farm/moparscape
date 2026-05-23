package com.veyrmoor.server.runtime;

import com.veyrmoor.protocol.session.HandshakeRequest;
import com.veyrmoor.protocol.session.LoginRequest;

public sealed interface LoginGatewayMessage extends ActorMessage {

  record HandleHandshakeMessage(ActorRef<SessionMessage> replyTo, HandshakeRequest request)
      implements LoginGatewayMessage {
  }

  record HandleLoginMessage(ActorRef<SessionMessage> replyTo, LoginRequest request) implements LoginGatewayMessage {
  }
}
