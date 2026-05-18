package io.github.ffakira.rsps.protocol;

import io.github.ffakira.rsps.model.WorldPoint;

public record LoginAccepted(long accountId, long characterId, WorldPoint spawnPoint) implements ServerMessage {
}
