package io.github.ffakira.rsps.sim;

import io.github.ffakira.rsps.model.CharacterId;
import io.github.ffakira.rsps.model.EntityId;
import io.github.ffakira.rsps.model.WorldPoint;

public record PlayerSpawnedEvent(
    EntityId entityId,
    CharacterId characterId,
    WorldPoint spawnPoint
) implements WorldEvent {
}
