package io.github.ffakira.rsps.sim;

import io.github.ffakira.rsps.model.EntityId;
import io.github.ffakira.rsps.model.WorldPoint;

public record EntityMovedEvent(EntityId entityId, WorldPoint from, WorldPoint to) implements WorldEvent {
}
