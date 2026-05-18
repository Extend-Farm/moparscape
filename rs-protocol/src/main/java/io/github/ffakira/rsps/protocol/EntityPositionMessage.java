package io.github.ffakira.rsps.protocol;

import io.github.ffakira.rsps.model.WorldPoint;

public record EntityPositionMessage(int entityId, WorldPoint worldPoint) implements ServerMessage {
}
