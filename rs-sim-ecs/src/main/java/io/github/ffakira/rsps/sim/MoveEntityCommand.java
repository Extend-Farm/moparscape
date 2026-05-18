package io.github.ffakira.rsps.sim;

import io.github.ffakira.rsps.model.EntityId;
import io.github.ffakira.rsps.model.MovementMode;

public record MoveEntityCommand(
    EntityId entityId,
    int deltaX,
    int deltaY,
    MovementMode movementMode
) implements WorldCommand {
}
