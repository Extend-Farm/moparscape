package io.github.ffakira.rsps.client.core;

public record SubmitLoginCommand(String username, String password) implements ClientCommand {
}
