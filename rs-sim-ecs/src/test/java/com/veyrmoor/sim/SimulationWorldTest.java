package com.veyrmoor.sim;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.model.CharacterId;
import com.veyrmoor.model.MovementMode;
import com.veyrmoor.model.WorldPoint;
import java.util.List;
import org.junit.jupiter.api.Test;

class SimulationWorldTest {

  @Test
  void advancesMovementDeterministically() {
    SimulationWorld world = new SimulationWorld();
    WorldCommandProcessor commandProcessor = new WorldCommandProcessor();

    var spawned = commandProcessor.apply(
        world,
        new SpawnPlayerCommand(new CharacterId(1), new WorldPoint(3200, 3200, 0))
    );
    var entityId = ((PlayerSpawnedEvent) spawned.getFirst()).entityId();

    commandProcessor.apply(world, new MoveEntityCommand(entityId, 1, 0, MovementMode.WALK));
    List<WorldEvent> events = world.advance(List.of(new MovementSystem()));

    assertThat(world.currentTick().value()).isEqualTo(1);
    assertThat(events).singleElement().isInstanceOf(EntityMovedEvent.class);
    assertThat(world.getComponent(entityId, TransformComponent.class))
        .isEqualTo(new TransformComponent(new WorldPoint(3201, 3200, 0)));
  }
}
