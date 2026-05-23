package com.veyrmoor.sim;

import com.veyrmoor.model.EntityId;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ComponentStore<T> {

  private final Map<EntityId, T> componentsByEntity = new ConcurrentHashMap<>();

  public void put(EntityId entityId, T component) {
    componentsByEntity.put(entityId, component);
  }

  public T get(EntityId entityId) {
    return componentsByEntity.get(entityId);
  }

  public void remove(EntityId entityId) {
    componentsByEntity.remove(entityId);
  }

  public Map<EntityId, T> snapshot() {
    return Map.copyOf(componentsByEntity);
  }
}
