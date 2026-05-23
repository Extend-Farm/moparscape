package com.veyrmoor.client.core;

public record ConnectCommand(String host, int port) implements ClientCommand {
}
