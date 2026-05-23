package com.veyrmoor.client.core;

import com.veyrmoor.protocol.bootstrap.CharacterBootstrapPayload;

public record CharacterBootstrappedEvent(CharacterBootstrapPayload bootstrap) implements ClientEvent {
}
