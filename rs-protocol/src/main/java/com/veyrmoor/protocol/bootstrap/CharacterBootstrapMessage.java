package com.veyrmoor.protocol.bootstrap;

import com.veyrmoor.protocol.ServerMessage;

public record CharacterBootstrapMessage(CharacterBootstrapPayload bootstrap) implements ServerMessage {
}
