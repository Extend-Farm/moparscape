package io.github.ffakira.rsps.client.desktop.core;

import io.github.ffakira.rsps.client.core.GameplayClientSession;
import io.github.ffakira.rsps.protocol.ServerMessage;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

final class NativeClientRuntimeContext implements AutoCloseable {

  private final AutoCloseable runtimeHandle;
  private final GameplayClientSession session;
  private final ConcurrentLinkedQueue<ServerMessage> inboundMessages;

  NativeClientRuntimeContext(
      AutoCloseable runtimeHandle,
      GameplayClientSession session,
      ConcurrentLinkedQueue<ServerMessage> inboundMessages
  ) {
    this.runtimeHandle = Objects.requireNonNull(runtimeHandle, "runtimeHandle");
    this.session = Objects.requireNonNull(session, "session");
    this.inboundMessages = Objects.requireNonNull(inboundMessages, "inboundMessages");
  }

  GameplayClientSession session() {
    return session;
  }

  ConcurrentLinkedQueue<ServerMessage> inboundMessages() {
    return inboundMessages;
  }

  @Override
  public void close() {
    try {
      session.close();
    } finally {
      try {
        runtimeHandle.close();
      } catch (Exception exception) {
        throw new RuntimeException("Unable to close native runtime context", exception);
      }
    }
  }
}
