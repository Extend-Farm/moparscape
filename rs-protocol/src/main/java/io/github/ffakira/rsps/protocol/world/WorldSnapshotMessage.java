package io.github.ffakira.rsps.protocol.world;

import io.github.ffakira.rsps.model.WorldPoint;
import io.github.ffakira.rsps.protocol.ServerMessage;

public record WorldSnapshotMessage(String regionKey, WorldPoint localPlayerPosition) implements ServerMessage {
}
