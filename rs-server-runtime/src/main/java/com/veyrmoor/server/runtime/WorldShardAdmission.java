package com.veyrmoor.server.runtime;

import com.veyrmoor.model.EntityId;
import com.veyrmoor.model.WorldPoint;
import com.veyrmoor.persistence.CharacterSnapshot;

public record WorldShardAdmission(CharacterSnapshot characterSnapshot, EntityId entityId, String regionKey) {

  public WorldPoint spawnPoint() {
    return characterSnapshot.worldPoint();
  }
}
