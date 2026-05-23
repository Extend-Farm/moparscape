package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.cache.Archive;

final class BootstrapEngineFinalizer {

    private BootstrapEngineFinalizer() {
    }

    static MouseRecorder finalizeStartup(GameClientCore core, Archive wordEncArchive, GameClient gameClient, int mouseRecorderRate)
    {
        ChatCensor.method487(wordEncArchive);
        MouseRecorder mouseRecorder = new MouseRecorder(gameClient, mouseRecorderRate);
        core.method12(mouseRecorder, 10);
        DynamicObject.aClient1609 = gameClient;
        ObjectDefinition.aClient765 = gameClient;
        NpcDefinition.aClient82 = gameClient;
        return mouseRecorder;
    }
}
