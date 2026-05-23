package com.veyrmoor.protocol.input;

import com.veyrmoor.model.MovementMode;
import com.veyrmoor.protocol.ClientMessage;

public record MoveIntentMessage(int deltaX, int deltaY, MovementMode movementMode) implements ClientMessage {
}
