package com.veyrmoor.protocol.session;

import com.veyrmoor.protocol.ClientMessage;

public record LoginRequest(String username, String password) implements ClientMessage {
}
