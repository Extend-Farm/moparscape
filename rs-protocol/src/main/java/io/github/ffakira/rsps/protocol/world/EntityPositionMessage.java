package io.github.ffakira.rsps.protocol.world;

import io.github.ffakira.rsps.model.WorldPoint;
import io.github.ffakira.rsps.protocol.ServerMessage;

public record EntityPositionMessage(int entityId, WorldPoint worldPoint) implements ServerMessage {
}
