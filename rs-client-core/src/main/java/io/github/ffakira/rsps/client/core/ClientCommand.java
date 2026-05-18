package io.github.ffakira.rsps.client.core;

public sealed interface ClientCommand permits ConnectCommand, MoveLocalPlayerCommand, SubmitLoginCommand {
}
