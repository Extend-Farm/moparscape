package io.github.ffakira.rsps.sim;

import io.github.ffakira.rsps.model.CharacterId;
import io.github.ffakira.rsps.model.WorldPoint;

public record SpawnPlayerCommand(CharacterId characterId, WorldPoint spawnPoint) implements WorldCommand {
}
