package com.veyrmoor.protocol.session;

import com.veyrmoor.protocol.ProtocolVersion;
import com.veyrmoor.protocol.ServerMessage;

public record HandshakeAccepted(ProtocolVersion protocolVersion, String motd) implements ServerMessage {
}
