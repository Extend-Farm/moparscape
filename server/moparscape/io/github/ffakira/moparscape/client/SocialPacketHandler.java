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

    static int[] handleFriendStatusUpdate(
        GameClient gameClient,
        PacketBuffer packetBuffer,
        int friendCount,
        int onlinePriorityStatus,
        long[] friendNameHashes,
        String[] friendDisplayNames,
        int[] friendWorlds,
        boolean messageFilterFlag
    ) {
        long friendHash = packetBuffer.method414(-35089);
        int friendWorld = packetBuffer.method408();
        String friendName = TextUtils.method587(-45804, TextUtils.method584(friendHash, (byte)-99));
        boolean shouldRefreshFriends = false;

        for(int index = 0; index < friendCount; index++)
        {
            if(friendHash != friendNameHashes[index])
                continue;
            if(friendWorlds[index] != friendWorld)
            {
                friendWorlds[index] = friendWorld;
                shouldRefreshFriends = true;
                if(friendWorld > 0)
                    gameClient.method77(friendName + " has logged in.", 5, "", messageFilterFlag);
                if(friendWorld == 0)
                    gameClient.method77(friendName + " has logged out.", 5, "", messageFilterFlag);
            }
            friendName = null;
            break;
        }

        if(friendName != null && friendCount < 200)
        {
            friendNameHashes[friendCount] = friendHash;
            friendDisplayNames[friendCount] = friendName;
            friendWorlds[friendCount] = friendWorld;
            friendCount++;
            shouldRefreshFriends = true;
        }
        for(boolean isSorted = false; !isSorted;)
        {
            isSorted = true;
            for(int index = 0; index < friendCount - 1; index++)
                if(friendWorlds[index] != onlinePriorityStatus && friendWorlds[index + 1] == onlinePriorityStatus || friendWorlds[index] == 0 && friendWorlds[index + 1] != 0)
                {
                    int world = friendWorlds[index];
                    friendWorlds[index] = friendWorlds[index + 1];
                    friendWorlds[index + 1] = world;
                    String displayName = friendDisplayNames[index];
                    friendDisplayNames[index] = friendDisplayNames[index + 1];
                    friendDisplayNames[index + 1] = displayName;
                    long hash = friendNameHashes[index];
                    friendNameHashes[index] = friendNameHashes[index + 1];
                    friendNameHashes[index + 1] = hash;
                    shouldRefreshFriends = true;
                    isSorted = false;
                }

        }
        return new int[] {
            friendCount, shouldRefreshFriends ? 1 : 0
        };
    }

    private static boolean isIgnored(long playerNameHash, int ignoreCount, long[] ignoredNameHashes)
    {
        for(int index = 0; index < ignoreCount; index++)
            if(ignoredNameHashes[index] == playerNameHash)
                return true;
        return false;
    }
}
