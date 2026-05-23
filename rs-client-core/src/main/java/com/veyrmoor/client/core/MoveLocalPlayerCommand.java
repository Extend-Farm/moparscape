package com.veyrmoor.client.core;

import com.veyrmoor.model.MovementMode;

public record MoveLocalPlayerCommand(int deltaX, int deltaY, MovementMode movementMode) implements ClientCommand {
}
