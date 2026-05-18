package io.github.ffakira.rsps.client.core;

public record LoginSucceededEvent(long accountId, long characterId) implements ClientEvent {
}
