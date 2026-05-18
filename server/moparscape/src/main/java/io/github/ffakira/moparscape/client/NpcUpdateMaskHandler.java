package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.net.PacketBuffer;

final class NpcUpdateMaskHandler {

    private NpcUpdateMaskHandler() {
    }

    static boolean applyNpcUpdateMasks(
        int npcUpdateCount,
        int[] updatedNpcIndices,
        Npc[] npcsByIndex,
        int gameTick,
        PacketBuffer packetBuffer,
        boolean preserveConnectionFlag,
        boolean currentConnectionFlag
    ) {
        for(int index = 0; index < npcUpdateCount; index++)
        {
            int npcIndex = updatedNpcIndices[index];
            Npc npc = npcsByIndex[npcIndex];
            int updateMask = packetBuffer.method408();
            if((updateMask & EntityUpdateMasks.Npc.ANIMATION) != 0)
                applyActorAnimationUpdate(npc, packetBuffer);
            if((updateMask & EntityUpdateMasks.Npc.HIT_PRIMARY) != 0)
                applyNpcHitPrimaryUpdate(npc, packetBuffer, gameTick);
            if((updateMask & EntityUpdateMasks.Npc.GRAPHIC) != 0)
                applyNpcGraphicUpdate(npc, packetBuffer, gameTick);
            if((updateMask & EntityUpdateMasks.Npc.INTERACTING_ENTITY) != 0)
                applyNpcInteractingEntityUpdate(npc, packetBuffer);
            if((updateMask & EntityUpdateMasks.Npc.FORCED_CHAT) != 0)
                applyNpcForcedChatUpdate(npc, packetBuffer);
            if((updateMask & EntityUpdateMasks.Npc.HIT_SECONDARY) != 0)
                applyNpcHitSecondaryUpdate(npc, packetBuffer, gameTick);
            if((updateMask & EntityUpdateMasks.Npc.TRANSFORM) != 0)
                applyNpcTransformUpdate(npc, packetBuffer);
            if((updateMask & EntityUpdateMasks.Npc.FACE_COORDINATES) != 0)
                applyNpcFaceCoordinatesUpdate(npc, packetBuffer);
        }
        return currentConnectionFlag & preserveConnectionFlag;
    }

    private static void applyActorAnimationUpdate(Actor actor, PacketBuffer packetBuffer)
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

    private static void applyNpcHitPrimaryUpdate(Npc npc, PacketBuffer packetBuffer, int gameTick)
    {
        int damage = packetBuffer.method426(0);
        int hitType = packetBuffer.method427(false);
        npc.method447(-35698, hitType, damage, gameTick);
        npc.anInt1532 = gameTick + 300;
        npc.anInt1533 = packetBuffer.method426(0);
        npc.anInt1534 = packetBuffer.method408();
    }

    private static void applyNpcGraphicUpdate(Npc npc, PacketBuffer packetBuffer, int gameTick)
    {
        npc.anInt1520 = packetBuffer.method410();
        int graphicData = packetBuffer.method413();
        npc.anInt1524 = graphicData >> 16;
        npc.anInt1523 = gameTick + (graphicData & 0xffff);
        npc.anInt1521 = 0;
        npc.anInt1522 = 0;
        if(npc.anInt1523 > gameTick)
            npc.anInt1521 = -1;
        if(npc.anInt1520 == 65535)
            npc.anInt1520 = -1;
    }

    private static void applyNpcInteractingEntityUpdate(Npc npc, PacketBuffer packetBuffer)
    {
        npc.anInt1502 = packetBuffer.method410();
        if(npc.anInt1502 == 65535)
            npc.anInt1502 = -1;
    }

    private static void applyNpcForcedChatUpdate(Npc npc, PacketBuffer packetBuffer)
    {
        npc.aString1506 = packetBuffer.method415();
        npc.anInt1535 = 100;
    }

    private static void applyNpcHitSecondaryUpdate(Npc npc, PacketBuffer packetBuffer, int gameTick)
    {
        int damage = packetBuffer.method427(false);
        int hitType = packetBuffer.method428(2);
        npc.method447(-35698, hitType, damage, gameTick);
        npc.anInt1532 = gameTick + 300;
        npc.anInt1533 = packetBuffer.method428(2);
        npc.anInt1534 = packetBuffer.method427(false);
    }

    private static void applyNpcTransformUpdate(Npc npc, PacketBuffer packetBuffer)
    {
        npc.aClass5_1696 = NpcDefinition.method159(packetBuffer.method436((byte)-74));
        npc.anInt1540 = npc.aClass5_1696.aByte68;
        npc.anInt1504 = npc.aClass5_1696.anInt79;
        npc.anInt1554 = npc.aClass5_1696.anInt67;
        npc.anInt1555 = npc.aClass5_1696.anInt58;
        npc.anInt1556 = npc.aClass5_1696.anInt83;
        npc.anInt1557 = npc.aClass5_1696.anInt55;
        npc.anInt1511 = npc.aClass5_1696.anInt77;
    }

    private static void applyNpcFaceCoordinatesUpdate(Npc npc, PacketBuffer packetBuffer)
    {
        npc.anInt1538 = packetBuffer.method434((byte)108);
        npc.anInt1539 = packetBuffer.method434((byte)108);
    }
}
