package io.github.ffakira.rsps.client.core;

public record ClientDisconnectedEvent(String reason) implements ClientEvent {
}
