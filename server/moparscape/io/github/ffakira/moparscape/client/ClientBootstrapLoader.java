package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.sign.SignLink;

final class ClientBootstrapLoader {

    private ClientBootstrapLoader() {
    }

    static boolean shouldUseSunJavaLoopTuning()
    {
        return SignLink.sunjava;
    }
}
