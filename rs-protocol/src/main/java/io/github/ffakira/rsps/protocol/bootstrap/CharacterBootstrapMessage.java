package io.github.ffakira.rsps.protocol.bootstrap;

import io.github.ffakira.rsps.protocol.ServerMessage;

public record CharacterBootstrapMessage(CharacterBootstrapPayload bootstrap) implements ServerMessage {
}
