package io.github.ffakira.rsps.client.desktop.core;

import io.github.ffakira.rsps.client.core.GameplayClientSession;
import io.github.ffakira.rsps.protocol.ServerMessage;
import io.github.ffakira.rsps.server.runtime.InProcessServerRuntime;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

final class NativeClientRuntimeContext implements AutoCloseable {

  private final InProcessServerRuntime runtime;
  private final GameplayClientSession session;
  private final ConcurrentLinkedQueue<ServerMessage> inboundMessages;

  NativeClientRuntimeContext(
      InProcessServerRuntime runtime,
      GameplayClientSession session,
      ConcurrentLinkedQueue<ServerMessage> inboundMessages
  ) {
    this.runtime = Objects.requireNonNull(runtime, "runtime");
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
      runtime.close();
    }
  }
}
