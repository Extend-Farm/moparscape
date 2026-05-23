package com.veyrmoor.protocol;

public sealed interface Packet permits ClientMessage, ServerMessage {
}
