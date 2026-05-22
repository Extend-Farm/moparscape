package io.github.ffakira.rsps.protocol.session;

import io.github.ffakira.rsps.protocol.ClientMessage;

public record DisconnectNotice(String reason) implements ClientMessage {
}
