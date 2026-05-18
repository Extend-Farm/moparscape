package io.github.ffakira.rsps.server.runtime;

import io.github.ffakira.rsps.persistence.CharacterSnapshot;
import io.github.ffakira.rsps.sim.WorldCommand;
import io.github.ffakira.rsps.sim.WorldEvent;
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
