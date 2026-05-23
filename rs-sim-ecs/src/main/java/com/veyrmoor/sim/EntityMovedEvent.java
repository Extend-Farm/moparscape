package com.veyrmoor.sim;

import com.veyrmoor.model.EntityId;
import com.veyrmoor.model.WorldPoint;

public record EntityMovedEvent(EntityId entityId, WorldPoint from, WorldPoint to) implements WorldEvent {
}
