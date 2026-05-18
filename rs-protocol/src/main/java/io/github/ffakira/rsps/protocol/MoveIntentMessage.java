package io.github.ffakira.rsps.protocol;

import io.github.ffakira.rsps.model.MovementMode;

public record MoveIntentMessage(int deltaX, int deltaY, MovementMode movementMode) implements ClientMessage {
}
