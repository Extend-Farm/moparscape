package com.veyrmoor.server.runtime;

public interface ActorRef<T extends ActorMessage> {

  void tell(T message);
}
