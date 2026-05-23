package io.github.ffakira.moparscape.client;

import java.net.MalformedURLException;
import java.net.URL;

record GameServerEndpoint(String host, int port) {

    static final String DEFAULT_ADDRESS = "127.0.0.1:43594";
    private static final int DEFAULT_PORT = 43594;

    static GameServerEndpoint fromConfiguredAddress(String configuredAddress) {
        String normalizedAddress = configuredAddress == null ? "" : configuredAddress.trim();
        if (normalizedAddress.isEmpty()) {
            normalizedAddress = DEFAULT_ADDRESS;
        }
        int separator = normalizedAddress.indexOf(':');
        if (separator <= 0) {
            return new GameServerEndpoint(normalizedAddress, DEFAULT_PORT);
        }
        String host = normalizedAddress.substring(0, separator);
        int port = parseConfiguredPort(normalizedAddress, DEFAULT_PORT);
        return new GameServerEndpoint(host, port);
    }

    URL toCodeBaseUrl() throws MalformedURLException {
        return new URL("http://" + host);
    }

    private static int parseConfiguredPort(String configuredAddress, int fallbackPort) {
        int separator = configuredAddress.lastIndexOf(':');
        if (separator == -1 || separator == configuredAddress.length() - 1) {
            return fallbackPort;
        }
        try {
            return Integer.parseInt(configuredAddress.substring(separator + 1));
        } catch (NumberFormatException _ex) {
            return fallbackPort;
        }
    }
}
