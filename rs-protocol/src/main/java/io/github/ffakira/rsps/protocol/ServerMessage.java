package io.github.ffakira.rsps.protocol;

public sealed interface ServerMessage permits CharacterBootstrapMessage, EntityActionSequenceMessage, EntityPositionMessage,
    HandshakeAccepted, LoginAccepted, LoginRejected, WorldSnapshotMessage {
}
