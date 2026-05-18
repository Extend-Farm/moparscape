package io.github.ffakira.rsps.sim;

import io.github.ffakira.rsps.model.MovementMode;

public record StepIntentComponent(int deltaX, int deltaY, MovementMode movementMode) {
}
