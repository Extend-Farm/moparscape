package io.github.ffakira.moparscape.client;

final class EntityMenuBuilder {

    private EntityMenuBuilder() {
    }

    static int buildNpcMenu(NpcDefinition npcDefinition, int npcIndex, int tileY, int tileX, int menuCount, int itemUseState, String itemUseItemName,
                            int selectedActionState, int selectedActionMask, String selectedActionName, int localPlayerCombatLevel, String[] menuTexts,
                            int[] menuOpcodes, int[] menuArg0, int[] menuArg1, int[] menuArg2)
    {
        if(menuCount >= 400)
            return menuCount;
        if(npcDefinition.anIntArray88 != null)
            npcDefinition = npcDefinition.method161(localPlayerCombatLevel);
        if(npcDefinition == null)
            return menuCount;
        if(!npcDefinition.aBoolean84)
            return menuCount;
        String npcName = npcDefinition.aString65;
        if(npcDefinition.anInt61 != 0)
            npcName = npcName + GameClientCore.method110(localPlayerCombatLevel, npcDefinition.anInt61, true) + " (level-" + npcDefinition.anInt61 + ")";
        if(itemUseState == 1)
        {
            menuTexts[menuCount] = "Use " + itemUseItemName + " with @yel@" + npcName;
            menuOpcodes[menuCount] = 582;
            menuArg0[menuCount] = npcIndex;
            menuArg1[menuCount] = tileX;
            menuArg2[menuCount] = tileY;
            return menuCount + 1;
        }
        if(selectedActionState == 1)
        {
            if((selectedActionMask & 2) == 2)
            {
                menuTexts[menuCount] = selectedActionName + " @yel@" + npcName;
                menuOpcodes[menuCount] = 413;
                menuArg0[menuCount] = npcIndex;
                menuArg1[menuCount] = tileX;
                menuArg2[menuCount] = tileY;
                return menuCount + 1;
            }
            return menuCount;
        }
        if(npcDefinition.aStringArray66 != null)
        {
            for(int action = 4; action >= 0; action--)
                if(npcDefinition.aStringArray66[action] != null && !npcDefinition.aStringArray66[action].equalsIgnoreCase("attack"))
                {
                    menuTexts[menuCount] = npcDefinition.aStringArray66[action] + " @yel@" + npcName;
                    if(action == 0)
                        menuOpcodes[menuCount] = 20;
                    if(action == 1)
                        menuOpcodes[menuCount] = 412;
                    if(action == 2)
                        menuOpcodes[menuCount] = 225;
                    if(action == 3)
                        menuOpcodes[menuCount] = 965;
                    if(action == 4)
                        menuOpcodes[menuCount] = 478;
                    menuArg0[menuCount] = npcIndex;
                    menuArg1[menuCount] = tileX;
                    menuArg2[menuCount] = tileY;
                    menuCount++;
                }

        }
        if(npcDefinition.aStringArray66 != null)
        {
            for(int attackAction = 4; attackAction >= 0; attackAction--)
                if(npcDefinition.aStringArray66[attackAction] != null && npcDefinition.aStringArray66[attackAction].equalsIgnoreCase("attack"))
                {
                    char attackPriority = '\0';
                    if(npcDefinition.anInt61 > localPlayerCombatLevel)
                        attackPriority = '\u07D0';
                    menuTexts[menuCount] = npcDefinition.aStringArray66[attackAction] + " @yel@" + npcName;
                    if(attackAction == 0)
                        menuOpcodes[menuCount] = 20 + attackPriority;
                    if(attackAction == 1)
                        menuOpcodes[menuCount] = 412 + attackPriority;
                    if(attackAction == 2)
                        menuOpcodes[menuCount] = 225 + attackPriority;
                    if(attackAction == 3)
                        menuOpcodes[menuCount] = 965 + attackPriority;
                    if(attackAction == 4)
                        menuOpcodes[menuCount] = 478 + attackPriority;
                    menuArg0[menuCount] = npcIndex;
                    menuArg1[menuCount] = tileX;
                    menuArg2[menuCount] = tileY;
                    menuCount++;
                }

        }
        menuTexts[menuCount] = "Examine @yel@" + npcName;
        menuOpcodes[menuCount] = 1025;
        menuArg0[menuCount] = npcIndex;
        menuArg1[menuCount] = tileX;
        menuArg2[menuCount] = tileY;
        return menuCount + 1;
    }

    static int buildPlayerMenu(int tileX, int playerIndex, int tileY, Player targetPlayer, Player localPlayer, int menuCount, int itemUseState,
                               String itemUseItemName, int selectedActionState, int selectedActionMask, String selectedActionName,
                               String[] playerActionTexts, boolean[] playerActionHighPriority, String[] menuTexts, int[] menuOpcodes, int[] menuArg0,
                               int[] menuArg1, int[] menuArg2)
    {
        if(targetPlayer == localPlayer)
            return menuCount;
        if(menuCount >= 400)
            return menuCount;
        String targetName;
        if(targetPlayer.anInt1723 == 0)
            targetName = targetPlayer.aString1703 + GameClientCore.method110(localPlayer.anInt1705, targetPlayer.anInt1705, true) + " (level-" + targetPlayer.anInt1705 + ")";
        else
            targetName = targetPlayer.aString1703 + " (skill-" + targetPlayer.anInt1723 + ")";
        if(itemUseState == 1)
        {
            menuTexts[menuCount] = "Use " + itemUseItemName + " with @whi@" + targetName;
            menuOpcodes[menuCount] = 491;
            menuArg0[menuCount] = playerIndex;
            menuArg1[menuCount] = tileX;
            menuArg2[menuCount] = tileY;
            return menuCount + 1;
        }
        if(selectedActionState == 1)
        {
            if((selectedActionMask & 8) == 8)
            {
                menuTexts[menuCount] = selectedActionName + " @whi@" + targetName;
                menuOpcodes[menuCount] = 365;
                menuArg0[menuCount] = playerIndex;
                menuArg1[menuCount] = tileX;
                menuArg2[menuCount] = tileY;
                return menuCount + 1;
            }
            return menuCount;
        }
        for(int action = 4; action >= 0; action--)
            if(playerActionTexts[action] != null)
            {
                menuTexts[menuCount] = playerActionTexts[action] + " @whi@" + targetName;
                char priority = '\0';
                if(playerActionTexts[action].equalsIgnoreCase("attack"))
                {
                    if(targetPlayer.anInt1705 > localPlayer.anInt1705)
                        priority = '\u07D0';
                    if(localPlayer.anInt1701 != 0 && targetPlayer.anInt1701 != 0)
                        if(localPlayer.anInt1701 == targetPlayer.anInt1701)
                            priority = '\u07D0';
                        else
                            priority = '\0';
                } else if(playerActionHighPriority[action])
                    priority = '\u07D0';
                if(action == 0)
                    menuOpcodes[menuCount] = 561 + priority;
                if(action == 1)
                    menuOpcodes[menuCount] = 779 + priority;
                if(action == 2)
                    menuOpcodes[menuCount] = 27 + priority;
                if(action == 3)
                    menuOpcodes[menuCount] = 577 + priority;
                if(action == 4)
                    menuOpcodes[menuCount] = 729 + priority;
                menuArg0[menuCount] = playerIndex;
                menuArg1[menuCount] = tileX;
                menuArg2[menuCount] = tileY;
                menuCount++;
            }

        for(int menuIndex = 0; menuIndex < menuCount; menuIndex++)
            if(menuOpcodes[menuIndex] == 516)
            {
                menuTexts[menuIndex] = "Walk here @whi@" + targetName;
                return menuCount;
            }

        return menuCount;
    }
}
