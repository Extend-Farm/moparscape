package com.veyrmoor.sim;

public sealed interface WorldCommand permits MoveEntityCommand, SpawnPlayerCommand {
}
