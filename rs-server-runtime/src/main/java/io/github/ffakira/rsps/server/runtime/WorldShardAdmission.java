package io.github.ffakira.rsps.server.runtime;

import io.github.ffakira.rsps.model.EntityId;
import io.github.ffakira.rsps.model.WorldPoint;
import io.github.ffakira.rsps.persistence.CharacterSnapshot;

public record WorldShardAdmission(CharacterSnapshot characterSnapshot, EntityId entityId, String regionKey) {

  public WorldPoint spawnPoint() {
    return characterSnapshot.worldPoint();
  }
}
