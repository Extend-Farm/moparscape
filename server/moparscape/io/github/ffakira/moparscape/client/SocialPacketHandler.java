package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.net.PacketBuffer;
import io.github.ffakira.moparscape.sign.SignLink;

final class SocialPacketHandler {

    private SocialPacketHandler() {
    }

    static void handleSocialRequestMessage(
        SocialOutputPort socialOutputPort,
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
                socialOutputPort.addChatMessage("wishes to trade with you.", 4, playerName, messageFilterFlag);
            return;
        }
        if(message.endsWith(":duelreq:"))
        {
            String playerName = message.substring(0, message.indexOf(":"));
            long playerNameHash = TextUtils.method583(playerName);
            if(!isIgnored(playerNameHash, ignoreCount, ignoredNameHashes) && chatPrivacyMode == 0)
                socialOutputPort.addChatMessage("wishes to duel with you.", 8, playerName, messageFilterFlag);
            return;
        }
        if(message.endsWith(":chalreq:"))
        {
            String playerName = message.substring(0, message.indexOf(":"));
            long playerNameHash = TextUtils.method583(playerName);
            if(!isIgnored(playerNameHash, ignoreCount, ignoredNameHashes) && chatPrivacyMode == 0)
            {
                String challengeText = message.substring(message.indexOf(":") + 1, message.length() - 9);
                socialOutputPort.addChatMessage(challengeText, 8, playerName, messageFilterFlag);
            }
            return;
        }
        socialOutputPort.addChatMessage(message, 0, "", messageFilterFlag);
    }

    static int[] handleFriendStatusUpdate(
        SocialOutputPort socialOutputPort,
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
                    socialOutputPort.addChatMessage(friendName + " has logged in.", 5, "", messageFilterFlag);
                if(friendWorld == 0)
                    socialOutputPort.addChatMessage(friendName + " has logged out.", 5, "", messageFilterFlag);
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

    // Exact extraction of legacy public-chat packet (opcode 196) logic.
    static int handlePublicChatPacket(
        SocialOutputPort socialOutputPort,
        PacketBuffer packetBuffer,
        int packetSize,
        int[] recentChatIds,
        int recentChatWriteIndex,
        int ignoreCount,
        long[] ignoredNameHashes,
        int chatPrivacyMode,
        boolean messageFilterFlag
    ) {
        long senderNameHash = packetBuffer.method414(-35089);
        int chatId = packetBuffer.method413();
        int chatPrivilege = packetBuffer.method408();
        boolean shouldSkip = false;
        for(int index = 0; index < 100; index++)
        {
            if(recentChatIds[index] != chatId)
                continue;
            shouldSkip = true;
            break;
        }

        if(chatPrivilege <= 1)
        {
            for(int ignoredIndex = 0; ignoredIndex < ignoreCount; ignoredIndex++)
            {
                if(ignoredNameHashes[ignoredIndex] != senderNameHash)
                    continue;
                shouldSkip = true;
                break;
            }

        }
        if(!shouldSkip && chatPrivacyMode == 0)
            try
            {
                recentChatIds[recentChatWriteIndex] = chatId;
                recentChatWriteIndex = (recentChatWriteIndex + 1) % 100;
                String message = ChatMessageCodec.method525(packetSize - 13, true, packetBuffer);
                if(chatPrivilege != 3)
                    message = ChatCensor.method497(message, 0);
                if(chatPrivilege == 2 || chatPrivilege == 3)
                    socialOutputPort.addChatMessage(message, 7, "@cr2@" + TextUtils.method587(-45804, TextUtils.method584(senderNameHash, (byte)-99)), messageFilterFlag);
                else
                if(chatPrivilege == 1)
                    socialOutputPort.addChatMessage(message, 7, "@cr1@" + TextUtils.method587(-45804, TextUtils.method584(senderNameHash, (byte)-99)), messageFilterFlag);
                else
                    socialOutputPort.addChatMessage(message, 3, TextUtils.method587(-45804, TextUtils.method584(senderNameHash, (byte)-99)), messageFilterFlag);
            }
            catch(Exception exception)
            {
                SignLink.reporterror("cde1");
            }
        return recentChatWriteIndex;
    }

    private static boolean isIgnored(long playerNameHash, int ignoreCount, long[] ignoredNameHashes)
    {
        for(int index = 0; index < ignoreCount; index++)
            if(ignoredNameHashes[index] == playerNameHash)
                return true;
        return false;
    }
}
