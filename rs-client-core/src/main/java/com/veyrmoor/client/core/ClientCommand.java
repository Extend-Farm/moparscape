package com.veyrmoor.client.core;

public sealed interface ClientCommand permits ConnectCommand, MoveLocalPlayerCommand, SubmitLoginCommand {
}
