package io.github.ffakira.rsps.protocol;

public record LoginRejected(String reason) implements ServerMessage {
}
