package io.github.ffakira.rsps.client.core;

public record ConnectCommand(String host, int port) implements ClientCommand {
}
