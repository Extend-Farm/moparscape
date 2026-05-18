package io.github.ffakira.rsps.client.core;

import io.github.ffakira.rsps.model.MovementMode;

public record MoveLocalPlayerCommand(int deltaX, int deltaY, MovementMode movementMode) implements ClientCommand {
}
