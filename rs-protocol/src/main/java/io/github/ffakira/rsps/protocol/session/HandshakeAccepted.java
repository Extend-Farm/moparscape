package io.github.ffakira.rsps.protocol.session;

import io.github.ffakira.rsps.protocol.ProtocolVersion;
import io.github.ffakira.rsps.protocol.ServerMessage;

public record HandshakeAccepted(ProtocolVersion protocolVersion, String motd) implements ServerMessage {
}
