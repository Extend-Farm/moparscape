package com.veyrmoor.protocol.session;

import com.veyrmoor.protocol.ClientMessage;
import com.veyrmoor.protocol.ProtocolVersion;

public record HandshakeRequest(ProtocolVersion protocolVersion, String clientBuild) implements ClientMessage {
}
