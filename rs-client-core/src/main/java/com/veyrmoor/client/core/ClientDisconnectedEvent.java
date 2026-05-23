package com.veyrmoor.client.core;

public record ClientDisconnectedEvent(String reason) implements ClientEvent {
}
