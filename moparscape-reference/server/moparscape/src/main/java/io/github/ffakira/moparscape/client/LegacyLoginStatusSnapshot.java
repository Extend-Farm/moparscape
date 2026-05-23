package io.github.ffakira.moparscape.client;

public record LegacyLoginStatusSnapshot(
    boolean loggedIn,
    boolean credentialsScreenVisible,
    boolean autoSubmitPending,
    String primaryMessage,
    String secondaryMessage
) {
}
