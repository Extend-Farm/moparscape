package com.veyrmoor.sim;

import com.veyrmoor.model.MovementMode;

public record StepIntentComponent(int deltaX, int deltaY, MovementMode movementMode) {
}
