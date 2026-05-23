package com.veyrmoor.client.core;

public record SubmitLoginCommand(String username, String password) implements ClientCommand {
}
