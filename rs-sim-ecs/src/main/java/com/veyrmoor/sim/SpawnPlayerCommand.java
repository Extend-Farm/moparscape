package com.veyrmoor.sim;

import com.veyrmoor.model.CharacterId;
import com.veyrmoor.model.WorldPoint;

public record SpawnPlayerCommand(CharacterId characterId, WorldPoint spawnPoint) implements WorldCommand {
}
