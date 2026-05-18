package io.github.ffakira.rsps.protocol;

public record CharacterBootstrapMessage(CharacterBootstrapPayload bootstrap) implements ServerMessage {
}
