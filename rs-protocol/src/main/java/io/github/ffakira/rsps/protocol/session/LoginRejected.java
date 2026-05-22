package io.github.ffakira.rsps.protocol.session;

import io.github.ffakira.rsps.protocol.ServerMessage;

public record LoginRejected(String reason) implements ServerMessage {
}
