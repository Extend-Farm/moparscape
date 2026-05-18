package io.github.ffakira.rsps.protocol;

public record LoginRequest(String username, String password) implements ClientMessage {
}
