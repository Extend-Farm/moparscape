package com.veyrmoor.sim;

import com.veyrmoor.model.CharacterId;
import com.veyrmoor.model.EntityId;
import com.veyrmoor.model.GameTick;
import com.veyrmoor.model.WorldPoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulationWorld {

  private final Map<Class<?>, ComponentStore<?>> componentStores = new HashMap<>();
  private int nextEntityValue = 1;
  private GameTick currentTick = GameTick.initial();

  public GameTick currentTick() {
    return currentTick;
  }

  public EntityId createEntity() {
    return new EntityId(nextEntityValue++);
  }

  public EntityId spawnPlayer(CharacterId characterId, WorldPoint spawnPoint) {
    EntityId entityId = createEntity();
    putComponent(entityId, new PlayerComponent(characterId));
    putComponent(entityId, new TransformComponent(spawnPoint));
    return entityId;
  }

  public <T> void putComponent(EntityId entityId, T component) {
    componentStoreForInstance(component).put(entityId, component);
  }

  public <T> T getComponent(EntityId entityId, Class<T> componentType) {
    return componentStore(componentType).get(entityId);
  }

  public <T> void removeComponent(EntityId entityId, Class<T> componentType) {
    componentStore(componentType).remove(entityId);
  }

  public <T> Map<EntityId, T> snapshot(Class<T> componentType) {
    Map<EntityId, T> snapshot = new HashMap<>();
    for (Map.Entry<EntityId, T> entry : componentStore(componentType).snapshot().entrySet()) {
      snapshot.put(entry.getKey(), entry.getValue());
    }
    return snapshot;
  }

  public List<WorldEvent> advance(List<GameSystem> systems) {
    currentTick = currentTick.next();
    List<WorldEvent> events = new ArrayList<>();
    for (GameSystem system : systems) {
      events.addAll(system.update(this, currentTick));
    }
    return List.copyOf(events);
  }

  @SuppressWarnings("unchecked")
  private <T> ComponentStore<T> componentStore(Class<T> componentType) {
    return (ComponentStore<T>) componentStores.computeIfAbsent(componentType, ignored -> new ComponentStore<>());
  }

  @SuppressWarnings("unchecked")
  private <T> ComponentStore<T> componentStoreForInstance(T component) {
    return (ComponentStore<T>) componentStores.computeIfAbsent(component.getClass(), ignored -> new ComponentStore<>());
  }
}
