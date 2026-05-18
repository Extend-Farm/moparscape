package io.github.ffakira.rsps.client.core;

public record ConnectedEvent(String host, int port) implements ClientEvent {
}
