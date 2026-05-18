package io.github.ffakira.rsps.protocol;

public sealed interface ServerMessage permits CharacterBootstrapMessage, EntityPositionMessage, HandshakeAccepted, LoginAccepted,
    LoginRejected, WorldSnapshotMessage {
}
