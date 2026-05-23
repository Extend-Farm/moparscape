package com.veyrmoor.protocol.session;

import com.veyrmoor.model.WorldPoint;
import com.veyrmoor.protocol.ServerMessage;

public record LoginAccepted(long accountId, long characterId, WorldPoint spawnPoint) implements ServerMessage {
}
