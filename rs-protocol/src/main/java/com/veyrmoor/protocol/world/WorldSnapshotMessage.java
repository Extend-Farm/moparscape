package com.veyrmoor.protocol.world;

import com.veyrmoor.model.WorldPoint;
import com.veyrmoor.protocol.ServerMessage;

public record WorldSnapshotMessage(String regionKey, WorldPoint localPlayerPosition) implements ServerMessage {
}
