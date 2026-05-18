package io.github.ffakira.rsps.protocol;

public record HandshakeAccepted(ProtocolVersion protocolVersion, String motd) implements ServerMessage {
}
