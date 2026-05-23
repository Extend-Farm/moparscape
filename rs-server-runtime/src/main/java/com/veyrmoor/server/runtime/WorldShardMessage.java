package com.veyrmoor.server.runtime;

import com.veyrmoor.persistence.CharacterSnapshot;
import com.veyrmoor.sim.WorldCommand;
import com.veyrmoor.sim.WorldEvent;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public sealed interface WorldShardMessage extends ActorMessage {

  record ApplyWorldCommandMessage(WorldCommand command) implements WorldShardMessage {
  }

  record ApplyWorldCommandAndTickMessage(WorldCommand command, CompletableFuture<List<WorldEvent>> responseFuture)
      implements WorldShardMessage {
  }

  record EnterCharacterMessage(CharacterSnapshot characterSnapshot, CompletableFuture<WorldShardAdmission> responseFuture)
      implements WorldShardMessage {
  }

  record TickWorldMessage() implements WorldShardMessage {
  }
}
