package io.github.ffakira.rsps.client.core;

import io.github.ffakira.rsps.protocol.ClientMessage;

public interface ClientTransport extends AutoCloseable {

  void send(ClientMessage message);

  @Override
  void close();
}
