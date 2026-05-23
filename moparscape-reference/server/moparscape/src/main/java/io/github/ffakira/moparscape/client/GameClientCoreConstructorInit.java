package io.github.ffakira.moparscape.client;

final class GameClientCoreConstructorInit {

    private GameClientCoreConstructorInit() {}

    static void initialize(GameClientCore core) {
        core.constructorInitWorldEntityAndBuffers();
        core.constructorInitRuntimeCachesAndChat();
        core.constructorInitUiWidgetsAndDeques();
        core.constructorInitNetworkingAndContextMenus();
        core.constructorInitLateUiAndCollision();
        core.constructorInitScratchArraysAndDefaults();
    }
}
