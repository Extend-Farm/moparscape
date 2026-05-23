package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.net.PacketBuffer;

final class MenuActionExecutor {

    private MenuActionExecutor() {
    }

    static void applyDragSelection(GameClientCore core, int widgetId, int slot)
    {
        core.updateSelectedDragTarget(widgetId, slot);
    }

    static void applySocialListAction(GameClientCore core, int actionOpcode, String actionText)
    {
        int nameStart = actionText.indexOf("@whi@");
        if(nameStart == -1)
            return;
        long targetNameHash = TextUtils.method583(actionText.substring(nameStart + 5).trim());
        if(actionOpcode == 337)
            core.method41((byte)68, targetNameHash);
        if(actionOpcode == 42)
            core.method113(targetNameHash, 4);
        if(actionOpcode == 792)
            core.method35(false, targetNameHash);
        if(actionOpcode == 322)
            core.method122(3, targetNameHash);
    }

    static void handlePlayerAction(GameClientCore core, Player targetPlayer, int targetIndex, int packetOpcode)
    {
        if(targetPlayer == null)
            return;
        core.routeToActor(targetPlayer);
        core.markMenuInteractionCursor();
        PacketBuffer outboundBuffer = core.getOutboundBuffer();
        outboundBuffer.method397((byte)6, packetOpcode);
        outboundBuffer.method431(true, targetIndex);
    }

    static void handleNpcAction(GameClientCore core, Npc targetNpc, int targetIndex, int packetOpcode, int widgetSelectionId)
    {
        if(targetNpc == null)
            return;
        core.routeToActor(targetNpc);
        core.markMenuInteractionCursor();
        PacketBuffer outboundBuffer = core.getOutboundBuffer();
        outboundBuffer.method397((byte)6, packetOpcode);
        if(packetOpcode == 131)
        {
            outboundBuffer.method433(0, targetIndex);
            outboundBuffer.method432(-431, widgetSelectionId);
            return;
        }
        if(packetOpcode == 72)
        {
            outboundBuffer.method432(-431, targetIndex);
            return;
        } else
        {
            outboundBuffer.method431(true, targetIndex);
            return;
        }
    }

    static boolean handleWidgetSelectionAction(GameClientCore core, int actionOpcode, int widgetId)
    {
        if(actionOpcode != 626)
            return false;
        core.beginWidgetSelection(widgetId);
        return true;
    }

    static boolean handleReportAbuseAction(GameClientCore core, int actionOpcode, String actionText)
    {
        if(actionOpcode != 606)
            return false;
        core.beginReportAbuseFromAction(actionText);
        return true;
    }

    static boolean handlePrivateMessageAction(GameClientCore core, int actionOpcode, String actionText)
    {
        if(actionOpcode != 639)
            return false;
        int nameStart = actionText.indexOf("@whi@");
        if(nameStart == -1)
            return true;
        long targetNameHash = TextUtils.method583(actionText.substring(nameStart + 5).trim());
        core.beginPrivateMessagePrompt(targetNameHash);
        return true;
    }

    static int handlePlayerNameAction(GameClientCore core, int actionOpcode, String actionText, int actionValue, Player[] players, int[] trackedPlayerIndexes, int trackedPlayerCount, PacketBuffer outboundBuffer, int antiSpamCounter, boolean feedbackEnabled)
    {
        String targetName = extractTargetName(actionText);
        if(targetName == null)
            return antiSpamCounter;
        boolean found = false;
        for(int index = 0; index < trackedPlayerCount; index++)
        {
            int trackedIndex = trackedPlayerIndexes[index];
            Player targetPlayer = players[trackedIndex];
            if(targetPlayer == null || targetPlayer.aString1703 == null || !targetPlayer.aString1703.equalsIgnoreCase(targetName))
                continue;
            core.routeToActor(targetPlayer);
            core.markMenuInteractionCursor();
            if(actionOpcode == 484)
            {
                outboundBuffer.method397((byte)6, 139);
                outboundBuffer.method431(true, trackedIndex);
            }
            if(actionOpcode == 6)
            {
                antiSpamCounter += actionValue;
                if(antiSpamCounter >= 90)
                {
                    outboundBuffer.method397((byte)6, 136);
                    antiSpamCounter = 0;
                }
                outboundBuffer.method397((byte)6, 128);
                outboundBuffer.method399(trackedIndex);
            }
            found = true;
            break;
        }

        if(!found)
            core.method77("Unable to find " + targetName, 0, "", feedbackEnabled);
        return antiSpamCounter;
    }

    private static String extractTargetName(String actionText)
    {
        int nameStart = actionText.indexOf("@whi@");
        if(nameStart == -1)
            return null;
        String rawName = actionText.substring(nameStart + 5).trim();
        return TextUtils.method587(-45804, TextUtils.method584(TextUtils.method583(rawName), (byte)-99));
    }
}
