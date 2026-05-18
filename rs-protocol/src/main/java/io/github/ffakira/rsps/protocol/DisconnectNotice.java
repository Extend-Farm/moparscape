package io.github.ffakira.rsps.protocol;

public record DisconnectNotice(String reason) implements ClientMessage {
}
