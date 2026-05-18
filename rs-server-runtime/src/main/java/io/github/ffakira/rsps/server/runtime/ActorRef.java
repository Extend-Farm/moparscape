package io.github.ffakira.rsps.server.runtime;

public interface ActorRef<T extends ActorMessage> {

  void tell(T message);
}
