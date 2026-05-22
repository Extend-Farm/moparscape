package io.github.ffakira.rsps.protocol;

public sealed interface Packet permits ClientMessage, ServerMessage {
}
