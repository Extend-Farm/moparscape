package io.github.ffakira.moparscape.client;

final class GameTickProcessor {

    private GameTickProcessor() {
    }

    static boolean processInboundPackets(GameClientCore core)
    {
        if(core.getSystemUpdateTimer() > 1)
            core.setSystemUpdateTimer(core.getSystemUpdateTimer() - 1);
        if(core.getLoginThrottleTicks() > 0)
            core.setLoginThrottleTicks(core.getLoginThrottleTicks() - 1);
        for(int packetAttempt = 0; packetAttempt < 5; packetAttempt++)
            if(!core.method145(true))
                break;
        return core.isGameReadyForTick();
    }
}
