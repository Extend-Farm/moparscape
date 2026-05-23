package com.veyrmoor.sim;

import com.veyrmoor.model.CharacterId;
import com.veyrmoor.model.EntityId;
import com.veyrmoor.model.WorldPoint;

public record PlayerSpawnedEvent(
    EntityId entityId,
    CharacterId characterId,
    WorldPoint spawnPoint
) implements WorldEvent {
}
