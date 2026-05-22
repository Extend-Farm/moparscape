package io.github.ffakira.rsps.protocol.input;

import io.github.ffakira.rsps.model.MovementMode;
import io.github.ffakira.rsps.protocol.ClientMessage;

public record MoveIntentMessage(int deltaX, int deltaY, MovementMode movementMode) implements ClientMessage {
}
