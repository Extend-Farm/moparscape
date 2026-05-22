package io.github.ffakira.rsps.protocol.session;

import io.github.ffakira.rsps.protocol.ClientMessage;
import io.github.ffakira.rsps.protocol.ProtocolVersion;

public record HandshakeRequest(ProtocolVersion protocolVersion, String clientBuild) implements ClientMessage {
}
