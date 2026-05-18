package io.github.ffakira.rsps.protocol;

public record HandshakeRequest(ProtocolVersion protocolVersion, String clientBuild) implements ClientMessage {
}
