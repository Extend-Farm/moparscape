package com.veyrmoor.client.core;

import com.veyrmoor.protocol.ClientMessage;

public interface ClientTransport extends AutoCloseable {

  void send(ClientMessage message);

  @Override
  void close();
}
