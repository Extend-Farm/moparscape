package com.veyrmoor.sim;

import com.veyrmoor.model.EntityId;
import com.veyrmoor.model.MovementMode;

public record MoveEntityCommand(
    EntityId entityId,
    int deltaX,
    int deltaY,
    MovementMode movementMode
) implements WorldCommand {
}
