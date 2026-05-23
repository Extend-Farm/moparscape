package com.veyrmoor.client.core;

public record LoginSucceededEvent(long accountId, long characterId) implements ClientEvent {
}
