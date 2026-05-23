package com.veyrmoor.protocol.world;

import com.veyrmoor.model.WorldPoint;
import com.veyrmoor.protocol.ServerMessage;

public record EntityPositionMessage(int entityId, WorldPoint worldPoint) implements ServerMessage {
}
