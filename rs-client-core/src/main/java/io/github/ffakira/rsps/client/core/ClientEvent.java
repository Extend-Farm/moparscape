package io.github.ffakira.rsps.client.core;

public sealed interface ClientEvent permits CharacterBootstrappedEvent, ClientBootstrappedEvent, ClientDisconnectedEvent, ConnectedEvent,
    LoginSucceededEvent, WorldLoadedEvent {
}
