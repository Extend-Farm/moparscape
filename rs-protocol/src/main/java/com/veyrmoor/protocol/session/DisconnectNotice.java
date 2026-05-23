package com.veyrmoor.protocol.session;

import com.veyrmoor.protocol.ClientMessage;

public record DisconnectNotice(String reason) implements ClientMessage {
}
