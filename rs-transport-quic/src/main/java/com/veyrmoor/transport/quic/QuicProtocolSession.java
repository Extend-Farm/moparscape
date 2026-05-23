package com.veyrmoor.transport.quic;

import com.veyrmoor.protocol.ProtocolBinaryCodec;
import com.veyrmoor.protocol.ProtocolSession;
import com.veyrmoor.protocol.ServerMessage;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

final class QuicProtocolSession implements ProtocolSession {

  private final UUID sessionId = UUID.randomUUID();
  private final AtomicBoolean closed = new AtomicBoolean();
  private final Channel streamChannel;

  QuicProtocolSession(Channel streamChannel) {
    this.streamChannel = streamChannel;
  }

  @Override
  public UUID sessionId() {
    return sessionId;
  }

  @Override
  public void send(ServerMessage message) {
    if (closed.get() || !streamChannel.isActive()) {
      return;
    }
    streamChannel.writeAndFlush(Unpooled.wrappedBuffer(ProtocolBinaryCodec.encodeServerMessage(message)));
  }

  @Override
  public void close() {
    if (!closed.compareAndSet(false, true)) {
      return;
    }
    streamChannel.close();
  }
}
