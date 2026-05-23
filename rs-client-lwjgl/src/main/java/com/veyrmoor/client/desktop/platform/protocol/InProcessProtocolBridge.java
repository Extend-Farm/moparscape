package com.veyrmoor.client.desktop.platform.protocol;

import com.veyrmoor.client.core.ClientTransport;
import com.veyrmoor.protocol.ClientMessage;
import com.veyrmoor.protocol.ProtocolSession;
import com.veyrmoor.protocol.ServerMessage;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public final class InProcessProtocolBridge implements ProtocolSession, ClientTransport {

  private final UUID sessionId = UUID.randomUUID();
  private final AtomicBoolean closed = new AtomicBoolean();
  private volatile Consumer<ServerMessage> inboundConsumer = ignored -> {
  };
  private volatile Consumer<ClientMessage> outboundConsumer = ignored -> {
  };

  public void bindInbound(Consumer<ServerMessage> inboundConsumer) {
    this.inboundConsumer = inboundConsumer;
  }

  public void bindOutbound(Consumer<ClientMessage> outboundConsumer) {
    this.outboundConsumer = outboundConsumer;
  }

  @Override
  public UUID sessionId() {
    return sessionId;
  }

  @Override
  public void send(ServerMessage message) {
    if (closed.get()) {
      return;
    }
    inboundConsumer.accept(message);
  }

  @Override
  public void send(ClientMessage message) {
    if (closed.get()) {
      return;
    }
    outboundConsumer.accept(message);
  }

  @Override
  public void close() {
    closed.set(true);
  }
}
