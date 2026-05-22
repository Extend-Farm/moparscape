package io.github.ffakira.rsps.protocol.session;

import io.github.ffakira.rsps.protocol.ClientMessage;

public record LoginRequest(String username, String password) implements ClientMessage {
}
