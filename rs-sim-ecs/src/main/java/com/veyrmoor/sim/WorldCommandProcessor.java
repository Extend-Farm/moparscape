package com.veyrmoor.sim;

import java.util.List;

public class WorldCommandProcessor {

  public List<WorldEvent> apply(SimulationWorld world, WorldCommand command) {
    return switch (command) {
      case SpawnPlayerCommand spawnPlayerCommand -> List.of(applySpawn(world, spawnPlayerCommand));
      case MoveEntityCommand moveEntityCommand -> {
        world.putComponent(
            moveEntityCommand.entityId(),
            new StepIntentComponent(
                moveEntityCommand.deltaX(),
                moveEntityCommand.deltaY(),
                moveEntityCommand.movementMode()
            )
        );
        yield List.of();
      }
    };
  }

  private PlayerSpawnedEvent applySpawn(SimulationWorld world, SpawnPlayerCommand command) {
    var entityId = world.spawnPlayer(command.characterId(), command.spawnPoint());
    return new PlayerSpawnedEvent(entityId, command.characterId(), command.spawnPoint());
  }
}
