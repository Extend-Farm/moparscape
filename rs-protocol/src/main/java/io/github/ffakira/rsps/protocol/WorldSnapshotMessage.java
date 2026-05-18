package io.github.ffakira.rsps.protocol;

import io.github.ffakira.rsps.model.WorldPoint;

public record WorldSnapshotMessage(String regionKey, WorldPoint localPlayerPosition) implements ServerMessage {
}
