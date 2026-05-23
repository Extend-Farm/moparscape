package com.veyrmoor.sim;

public sealed interface WorldEvent permits EntityMovedEvent, PlayerSpawnedEvent {
}
