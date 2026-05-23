package com.veyrmoor.client.core;

public record ConnectedEvent(String host, int port) implements ClientEvent {
}
