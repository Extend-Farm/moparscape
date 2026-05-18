package io.github.ffakira.rsps.protocol;

import java.util.UUID;

public interface ProtocolSession {

  UUID sessionId();

  void send(ServerMessage message);

  default void sendAll(Iterable<? extends ServerMessage> messages) {
    messages.forEach(this::send);
  }

  void close();
}
