package io.github.ffakira.rsps.sim;

import io.github.ffakira.rsps.model.EntityId;
import io.github.ffakira.rsps.model.GameTick;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MovementSystem implements GameSystem {

  @Override
  public List<WorldEvent> update(SimulationWorld world, GameTick tick) {
    List<WorldEvent> events = new ArrayList<>();
    Map<EntityId, StepIntentComponent> movementIntents = world.snapshot(StepIntentComponent.class);
    for (Map.Entry<EntityId, StepIntentComponent> entry : movementIntents.entrySet()) {
      EntityId entityId = entry.getKey();
      StepIntentComponent movementIntent = entry.getValue();
      TransformComponent transform = world.getComponent(entityId, TransformComponent.class);
      if (transform == null) {
        throw new IllegalStateException("Entity has movement but no transform: " + entityId);
      }
      var from = transform.position();
      var to = from.translate(movementIntent.deltaX(), movementIntent.deltaY());
      world.putComponent(entityId, new TransformComponent(to));
      world.removeComponent(entityId, StepIntentComponent.class);
      events.add(new EntityMovedEvent(entityId, from, to));
    }
    return events;
  }
}
