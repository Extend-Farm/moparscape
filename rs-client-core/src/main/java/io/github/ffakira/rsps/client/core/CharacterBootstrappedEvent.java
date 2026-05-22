package io.github.ffakira.rsps.client.core;

import io.github.ffakira.rsps.protocol.bootstrap.CharacterBootstrapPayload;

public record CharacterBootstrappedEvent(CharacterBootstrapPayload bootstrap) implements ClientEvent {
}
