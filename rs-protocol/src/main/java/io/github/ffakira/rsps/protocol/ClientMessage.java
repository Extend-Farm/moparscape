package io.github.ffakira.rsps.protocol;

public sealed interface ClientMessage permits DisconnectNotice, HandshakeRequest, LoginRequest, MoveIntentMessage {
}
