package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.net.PacketBuffer;
import io.github.ffakira.moparscape.sign.SignLink;

final class PlayerUpdateMaskHandler {

    private PlayerUpdateMaskHandler() {
    }

    static void applyPlayerPublicChatUpdate(
        GameClient gameClient,
        Player player,
        PacketBuffer packetBuffer,
        PacketBuffer chatDecodeBuffer,
        int ignoreCount,
        long[] ignoredNameHashes,
        int chatPrivacyMode,
        boolean messageFilterFlag
    ) {
        int chatEffects = packetBuffer.method434((byte)108);
        int chatRank = packetBuffer.method408();
        int encodedChatLength = packetBuffer.method427(false);
        int payloadStartOffset = packetBuffer.anInt1406;
        if(player.aString1703 != null && player.aBoolean1710)
        {
            long nameHash = TextUtils.method583(player.aString1703);
            boolean isIgnored = isIgnoredChatSender(nameHash, chatRank, ignoreCount, ignoredNameHashes);
            if(!isIgnored && chatPrivacyMode == 0)
                try
                {
                    chatDecodeBuffer.anInt1406 = 0;
                    packetBuffer.method442(encodedChatLength, 0, true, chatDecodeBuffer.aByteArray1405);
                    chatDecodeBuffer.anInt1406 = 0;
                    String chatText = ChatMessageCodec.method525(encodedChatLength, true, chatDecodeBuffer);
                    chatText = ChatCensor.method497(chatText, 0);
                    player.aString1506 = chatText;
                    player.anInt1513 = chatEffects >> 8;
                    player.anInt1531 = chatEffects & 0xff;
                    player.anInt1535 = 150;
                    if(chatRank == 2 || chatRank == 3)
                        gameClient.method77(chatText, 1, "@cr2@" + player.aString1703, messageFilterFlag);
                    else
                    if(chatRank == 1)
                        gameClient.method77(chatText, 1, "@cr1@" + player.aString1703, messageFilterFlag);
                    else
                        gameClient.method77(chatText, 2, player.aString1703, messageFilterFlag);
                }
                catch(Exception exception)
                {
                    SignLink.reporterror("cde2");
                }
        }
        packetBuffer.anInt1406 = payloadStartOffset + encodedChatLength;
    }

    static void applyPlayerForcedChatUpdate(GameClient gameClient, Player player, PacketBuffer packetBuffer, Player localPlayer, boolean messageFilterFlag)
    {
        player.aString1506 = packetBuffer.method415();
        if(player.aString1506.charAt(0) == '~')
        {
            player.aString1506 = player.aString1506.substring(1);
            gameClient.method77(player.aString1506, 2, player.aString1703, messageFilterFlag);
        } else
        if(player == localPlayer)
            gameClient.method77(player.aString1506, 2, player.aString1703, messageFilterFlag);
        player.anInt1513 = 0;
        player.anInt1531 = 0;
        player.anInt1535 = 150;
    }

    static void applyPlayerAppearanceUpdate(int playerIndex, Player player, PacketBuffer packetBuffer, byte decodeByte, PacketBuffer[] playerAppearanceBuffers)
    {
        int appearanceLength = packetBuffer.method427(false);
        byte appearanceData[] = new byte[appearanceLength];
        PacketBuffer appearanceBuffer = new PacketBuffer(appearanceData, 891);
        packetBuffer.method417(appearanceLength, decodeByte, 0, appearanceData);
        playerAppearanceBuffers[playerIndex] = appearanceBuffer;
        player.method451(0, appearanceBuffer);
    }

    private static boolean isIgnoredChatSender(long senderNameHash, int chatRank, int ignoreCount, long[] ignoredNameHashes)
    {
        if(chatRank > 1)
            return false;
        for(int ignoredIndex = 0; ignoredIndex < ignoreCount; ignoredIndex++)
            if(ignoredNameHashes[ignoredIndex] == senderNameHash)
                return true;
        return false;
    }
}
