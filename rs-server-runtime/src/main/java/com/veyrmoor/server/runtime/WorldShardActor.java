package com.veyrmoor.server.runtime;

import com.veyrmoor.model.WorldPoint;
import com.veyrmoor.sim.GameSystem;
import com.veyrmoor.sim.MovementSystem;
import com.veyrmoor.sim.PlayerSpawnedEvent;
import com.veyrmoor.sim.SimulationWorld;
import com.veyrmoor.sim.SpawnPlayerCommand;
import com.veyrmoor.sim.WorldCommand;
import com.veyrmoor.sim.WorldCommandProcessor;
import com.veyrmoor.sim.WorldEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WorldShardActor extends MailboxActor<WorldShardMessage> {

  private final SimulationWorld simulationWorld = new SimulationWorld();
  private final WorldCommandProcessor commandProcessor = new WorldCommandProcessor();
  private final List<GameSystem> systems = List.of(new MovementSystem());
  private final List<WorldEvent> emittedEvents = new ArrayList<>();

  public List<WorldEvent> drainEvents() {
    synchronized (emittedEvents) {
      List<WorldEvent> snapshot = List.copyOf(emittedEvents);
      emittedEvents.clear();
      return snapshot;
    }
  }

  public void applyWorldCommand(WorldCommand command) {
    tell(new WorldShardMessage.ApplyWorldCommandMessage(command));
  }

  public CompletableFuture<List<WorldEvent>> applyWorldCommandAndTick(WorldCommand command) {
    CompletableFuture<List<WorldEvent>> responseFuture = new CompletableFuture<>();
    tell(new WorldShardMessage.ApplyWorldCommandAndTickMessage(command, responseFuture));
    return responseFuture;
  }

  public CompletableFuture<WorldShardAdmission> enterCharacter(
      com.veyrmoor.persistence.CharacterSnapshot characterSnapshot
  ) {
    CompletableFuture<WorldShardAdmission> responseFuture = new CompletableFuture<>();
    tell(new WorldShardMessage.EnterCharacterMessage(characterSnapshot, responseFuture));
    return responseFuture;
  }

  public void tickOnce() {
    tell(new WorldShardMessage.TickWorldMessage());
  }

  @Override
  protected void onMessage(WorldShardMessage message) {
    switch (message) {
      case WorldShardMessage.ApplyWorldCommandMessage applyWorldCommandMessage ->
          recordEvents(commandProcessor.apply(simulationWorld, applyWorldCommandMessage.command()));
      case WorldShardMessage.ApplyWorldCommandAndTickMessage applyWorldCommandAndTickMessage ->
          handleApplyWorldCommandAndTick(applyWorldCommandAndTickMessage);
      case WorldShardMessage.EnterCharacterMessage enterCharacterMessage -> handleEnterCharacter(enterCharacterMessage);
      case WorldShardMessage.TickWorldMessage ignored -> recordEvents(simulationWorld.advance(systems));
    }
  }

  private void handleApplyWorldCommandAndTick(WorldShardMessage.ApplyWorldCommandAndTickMessage message) {
    try {
      List<WorldEvent> newEvents = new ArrayList<>(commandProcessor.apply(simulationWorld, message.command()));
      newEvents.addAll(simulationWorld.advance(systems));
      recordEvents(newEvents);
      message.responseFuture().complete(List.copyOf(newEvents));
    } catch (RuntimeException runtimeException) {
      message.responseFuture().completeExceptionally(runtimeException);
    }
  }

  private void handleEnterCharacter(WorldShardMessage.EnterCharacterMessage message) {
    try {
      List<WorldEvent> newEvents = commandProcessor.apply(
          simulationWorld,
          new SpawnPlayerCommand(message.characterSnapshot().id(), message.characterSnapshot().worldPoint())
      );
      recordEvents(newEvents);
      message.responseFuture().complete(buildAdmission(message.characterSnapshot(), newEvents));
    } catch (RuntimeException runtimeException) {
      message.responseFuture().completeExceptionally(runtimeException);
    }
  }

  private WorldShardAdmission buildAdmission(
      com.veyrmoor.persistence.CharacterSnapshot characterSnapshot,
      List<WorldEvent> newEvents
  ) {
    for (WorldEvent newEvent : newEvents) {
      if (newEvent instanceof PlayerSpawnedEvent playerSpawnedEvent) {
        return new WorldShardAdmission(
            characterSnapshot,
            playerSpawnedEvent.entityId(),
            regionKey(characterSnapshot.worldPoint())
        );
      }
    }
    throw new IllegalStateException("World shard did not emit a spawn event for " + characterSnapshot.displayName());
  }

  private void recordEvents(List<WorldEvent> newEvents) {
    if (newEvents.isEmpty()) {
      return;
    }
    synchronized (emittedEvents) {
      emittedEvents.addAll(newEvents);
    }
  }

  private String regionKey(WorldPoint worldPoint) {
    return (worldPoint.x() >> 6) + "_" + (worldPoint.y() >> 6);
  }
}
