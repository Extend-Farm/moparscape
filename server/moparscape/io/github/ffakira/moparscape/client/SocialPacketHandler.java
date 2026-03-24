package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.net.PacketBuffer;

final class SocialPacketHandler {

    private SocialPacketHandler() {
    }

    static void handleSocialRequestMessage(
        GameClient gameClient,
        PacketBuffer packetBuffer,
        int ignoreCount,
        long[] ignoredNameHashes,
        int chatPrivacyMode,
        boolean messageFilterFlag
    ) {
        String message = packetBuffer.method415();
        if(message.endsWith(":tradereq:"))
        {
            String playerName = message.substring(0, message.indexOf(":"));
            long playerNameHash = TextUtils.method583(playerName);
            if(!isIgnored(playerNameHash, ignoreCount, ignoredNameHashes) && chatPrivacyMode == 0)
                gameClient.method77("wishes to trade with you.", 4, playerName, messageFilterFlag);
            return;
        }
        if(message.endsWith(":duelreq:"))
        {
            String playerName = message.substring(0, message.indexOf(":"));
            long playerNameHash = TextUtils.method583(playerName);
            if(!isIgnored(playerNameHash, ignoreCount, ignoredNameHashes) && chatPrivacyMode == 0)
                gameClient.method77("wishes to duel with you.", 8, playerName, messageFilterFlag);
            return;
        }
        if(message.endsWith(":chalreq:"))
        {
            String playerName = message.substring(0, message.indexOf(":"));
            long playerNameHash = TextUtils.method583(playerName);
            if(!isIgnored(playerNameHash, ignoreCount, ignoredNameHashes) && chatPrivacyMode == 0)
            {
                String challengeText = message.substring(message.indexOf(":") + 1, message.length() - 9);
                gameClient.method77(challengeText, 8, playerName, messageFilterFlag);
            }
            return;
        }
        gameClient.method77(message, 0, "", messageFilterFlag);
    }

    private static boolean isIgnored(long playerNameHash, int ignoreCount, long[] ignoredNameHashes)
    {
        for(int index = 0; index < ignoreCount; index++)
            if(ignoredNameHashes[index] == playerNameHash)
                return true;
        return false;
    }
}
