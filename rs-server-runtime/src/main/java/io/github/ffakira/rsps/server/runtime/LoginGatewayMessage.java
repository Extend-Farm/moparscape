package io.github.ffakira.rsps.server.runtime;

import io.github.ffakira.rsps.protocol.HandshakeRequest;
import io.github.ffakira.rsps.protocol.LoginRequest;

public sealed interface LoginGatewayMessage extends ActorMessage {

  record HandleHandshakeMessage(ActorRef<SessionMessage> replyTo, HandshakeRequest request)
      implements LoginGatewayMessage {
  }

  record HandleLoginMessage(ActorRef<SessionMessage> replyTo, LoginRequest request) implements LoginGatewayMessage {
  }
}
