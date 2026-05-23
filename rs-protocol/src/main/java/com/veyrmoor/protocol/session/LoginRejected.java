package com.veyrmoor.protocol.session;

import com.veyrmoor.protocol.ServerMessage;

public record LoginRejected(String reason) implements ServerMessage {
}
