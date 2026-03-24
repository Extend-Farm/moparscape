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

    static void applyActorAnimationUpdate(Actor actor, PacketBuffer packetBuffer)
    {
        int animationId = packetBuffer.method434((byte)108);
        if(animationId == 65535)
            animationId = -1;
        int animationDelay = packetBuffer.method408();
        if(animationId == actor.anInt1526 && animationId != -1)
        {
            int replayMode = SequenceDefinition.aClass20Array351[animationId].anInt365;
            if(replayMode == 1)
            {
                actor.anInt1527 = 0;
                actor.anInt1528 = 0;
                actor.anInt1529 = animationDelay;
                actor.anInt1530 = 0;
            }
            if(replayMode == 2)
                actor.anInt1530 = 0;
        } else
        if(animationId == -1 || actor.anInt1526 == -1 || SequenceDefinition.aClass20Array351[animationId].anInt359 >= SequenceDefinition.aClass20Array351[actor.anInt1526].anInt359)
        {
            actor.anInt1526 = animationId;
            actor.anInt1527 = 0;
            actor.anInt1528 = 0;
            actor.anInt1529 = animationDelay;
            actor.anInt1530 = 0;
            actor.anInt1542 = actor.anInt1525;
        }
    }

    static void applyPlayerForceMovementUpdate(Player player, PacketBuffer packetBuffer, int gameTick)
    {
        player.anInt1543 = packetBuffer.method428(2);
        player.anInt1545 = packetBuffer.method428(2);
        player.anInt1544 = packetBuffer.method428(2);
        player.anInt1546 = packetBuffer.method428(2);
        player.anInt1547 = packetBuffer.method436((byte)-74) + gameTick;
        player.anInt1548 = packetBuffer.method435(true) + gameTick;
        player.anInt1549 = packetBuffer.method428(2);
        player.method446(true);
    }

    static void applyPlayerGraphicUpdate(Player player, PacketBuffer packetBuffer, int gameTick)
    {
        player.anInt1520 = packetBuffer.method434((byte)108);
        int graphicData = packetBuffer.method413();
        player.anInt1524 = graphicData >> 16;
        player.anInt1523 = gameTick + (graphicData & 0xffff);
        player.anInt1521 = 0;
        player.anInt1522 = 0;
        if(player.anInt1523 > gameTick)
            player.anInt1521 = -1;
        if(player.anInt1520 == 65535)
            player.anInt1520 = -1;
    }

    static void applyPlayerInteractingEntityUpdate(Player player, PacketBuffer packetBuffer)
    {
        player.anInt1502 = packetBuffer.method434((byte)108);
        if(player.anInt1502 == 65535)
            player.anInt1502 = -1;
    }

    static void applyPlayerFaceCoordinatesUpdate(Player player, PacketBuffer packetBuffer)
    {
        player.anInt1538 = packetBuffer.method436((byte)-74);
        player.anInt1539 = packetBuffer.method434((byte)108);
    }

    static void applyPlayerHitPrimaryUpdate(Player player, PacketBuffer packetBuffer, int gameTick)
    {
        int damage = packetBuffer.method408();
        int hitType = packetBuffer.method426(0);
        player.method447(-35698, hitType, damage, gameTick);
        player.anInt1532 = gameTick + 300;
        player.anInt1533 = packetBuffer.method427(false);
        player.anInt1534 = packetBuffer.method408();
    }

    static void applyPlayerHitSecondaryUpdate(Player player, PacketBuffer packetBuffer, int gameTick)
    {
        int damage = packetBuffer.method408();
        int hitType = packetBuffer.method428(2);
        player.method447(-35698, hitType, damage, gameTick);
        player.anInt1532 = gameTick + 300;
        player.anInt1533 = packetBuffer.method408();
        player.anInt1534 = packetBuffer.method427(false);
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
