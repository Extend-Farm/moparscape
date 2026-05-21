package io.github.ffakira.rsps.protocol;

public sealed interface ClientMessage permits ActionSequenceIntentMessage, DisconnectNotice, HandshakeRequest, LoginRequest,
    MoveIntentMessage {
}
