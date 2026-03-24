package io.github.ffakira.moparscape.client;

final class LoginProtocolHandler {

    private LoginProtocolHandler() {
    }

    static boolean shouldShowConnectingUi(boolean reconnecting)
    {
        return !reconnecting;
    }
}
