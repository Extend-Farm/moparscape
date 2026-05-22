package io.github.ffakira.rsps.protocol.session;

import io.github.ffakira.rsps.model.WorldPoint;
import io.github.ffakira.rsps.protocol.ServerMessage;

public record LoginAccepted(long accountId, long characterId, WorldPoint spawnPoint) implements ServerMessage {
}
