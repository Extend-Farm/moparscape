package io.github.ffakira.rsps.sim;

public sealed interface WorldCommand permits MoveEntityCommand, SpawnPlayerCommand {
}
