package io.github.ffakira.moparscape.client;

import java.awt.Graphics;

final class GameFrameHandler {

    private GameFrameHandler() {
    }

    // Exact extraction of the legacy method102 full-frame redraw prelude.
    static boolean[] applyFullFrameRedrawPrelude(
        boolean shouldRedrawFrame,
        ProducingGraphicsBuffer frame903,
        ProducingGraphicsBuffer frame904,
        ProducingGraphicsBuffer frame905,
        ProducingGraphicsBuffer frame906,
        ProducingGraphicsBuffer frame907,
        ProducingGraphicsBuffer frame908,
        ProducingGraphicsBuffer frame909,
        ProducingGraphicsBuffer frame910,
        ProducingGraphicsBuffer frame911,
        ProducingGraphicsBuffer frame1165,
        ProducingGraphicsBuffer frame1164,
        Graphics graphics,
        int gameState
    ) {
        boolean redrawInterface = false;
        boolean redrawChatbox = false;
        boolean redrawSidebar = false;
        boolean redrawTabArea = false;
        if(shouldRedrawFrame)
        {
            frame903.method238(4, 23680, graphics, 0);
            frame904.method238(357, 23680, graphics, 0);
            frame905.method238(4, 23680, graphics, 722);
            frame906.method238(205, 23680, graphics, 743);
            frame907.method238(0, 23680, graphics, 0);
            frame908.method238(4, 23680, graphics, 516);
            frame909.method238(205, 23680, graphics, 516);
            frame910.method238(357, 23680, graphics, 496);
            frame911.method238(338, 23680, graphics, 0);
            redrawInterface = true;
            redrawChatbox = true;
            redrawSidebar = true;
            redrawTabArea = true;
            if(gameState != 2)
            {
                frame1165.method238(4, 23680, graphics, 4);
                frame1164.method238(4, 23680, graphics, 550);
            }
        }
        return new boolean[] {
            redrawInterface, redrawChatbox, redrawSidebar, redrawTabArea
        };
    }

    // Exact extraction of the legacy method102 redraw trigger update block.
    static boolean applyMainViewportRedrawTriggers(
        int gameState,
        boolean isDragActive,
        int dragMode,
        int activeInterfaceId,
        int animationStep,
        int selectedMenuAction,
        int itemDragState,
        boolean currentRedrawInterface
    ) {
        boolean redrawInterface = currentRedrawInterface;
        if(gameState == 2)
            redrawInterface = true;
        if(isDragActive && dragMode == 1)
            redrawInterface = true;
        if(activeInterfaceId != -1)
            redrawInterface = true;
        if(selectedMenuAction == 2)
            redrawInterface = true;
        if(itemDragState == 2)
            redrawInterface = true;
        return redrawInterface;
    }

    static void drawTabIconsAndHighlights(
        ProducingGraphicsBuffer topTabBuffer,
        ProducingGraphicsBuffer bottomTabBuffer,
        IndexedSprite topTabBackground,
        IndexedSprite bottomTabBackground,
        IndexedSprite topHighlight0,
        IndexedSprite topHighlight1,
        IndexedSprite topHighlight2,
        IndexedSprite topHighlight3,
        IndexedSprite bottomHighlight0,
        IndexedSprite bottomHighlight1,
        IndexedSprite bottomHighlight2,
        IndexedSprite bottomHighlight3,
        IndexedSprite[] tabIconSprites,
        int[] tabInterfaceIds,
        int selectedTabId,
        int flashingTabId,
        int gameTick,
        int openInterfaceId,
        Graphics graphics
    ) {
        topTabBuffer.method237(0);
        topTabBackground.method361(0, 16083, 0);
        if(openInterfaceId == -1)
        {
            if(tabInterfaceIds[selectedTabId] != -1)
            {
                if(selectedTabId == 0)
                    topHighlight0.method361(22, 16083, 10);
                if(selectedTabId == 1)
                    topHighlight1.method361(54, 16083, 8);
                if(selectedTabId == 2)
                    topHighlight1.method361(82, 16083, 8);
                if(selectedTabId == 3)
                    topHighlight2.method361(110, 16083, 8);
                if(selectedTabId == 4)
                    topHighlight3.method361(153, 16083, 8);
                if(selectedTabId == 5)
                    topHighlight3.method361(181, 16083, 8);
                if(selectedTabId == 6)
                    bottomHighlight0.method361(209, 16083, 9);
            }
            if(tabInterfaceIds[0] != -1 && (flashingTabId != 0 || gameTick % 20 < 10))
                tabIconSprites[0].method361(29, 16083, 13);
            if(tabInterfaceIds[1] != -1 && (flashingTabId != 1 || gameTick % 20 < 10))
                tabIconSprites[1].method361(53, 16083, 11);
            if(tabInterfaceIds[2] != -1 && (flashingTabId != 2 || gameTick % 20 < 10))
                tabIconSprites[2].method361(82, 16083, 11);
            if(tabInterfaceIds[3] != -1 && (flashingTabId != 3 || gameTick % 20 < 10))
                tabIconSprites[3].method361(115, 16083, 12);
            if(tabInterfaceIds[4] != -1 && (flashingTabId != 4 || gameTick % 20 < 10))
                tabIconSprites[4].method361(153, 16083, 13);
            if(tabInterfaceIds[5] != -1 && (flashingTabId != 5 || gameTick % 20 < 10))
                tabIconSprites[5].method361(180, 16083, 11);
            if(tabInterfaceIds[6] != -1 && (flashingTabId != 6 || gameTick % 20 < 10))
                tabIconSprites[6].method361(208, 16083, 13);
        }
        topTabBuffer.method238(160, 23680, graphics, 516);

        bottomTabBuffer.method237(0);
        bottomTabBackground.method361(0, 16083, 0);
        if(openInterfaceId == -1)
        {
            if(tabInterfaceIds[selectedTabId] != -1)
            {
                if(selectedTabId == 7)
                    topHighlight0.method361(42, 16083, 0);
                if(selectedTabId == 8)
                    topHighlight1.method361(74, 16083, 0);
                if(selectedTabId == 9)
                    topHighlight1.method361(102, 16083, 0);
                if(selectedTabId == 10)
                    topHighlight2.method361(130, 16083, 1);
                if(selectedTabId == 11)
                    topHighlight3.method361(173, 16083, 0);
                if(selectedTabId == 12)
                    topHighlight3.method361(201, 16083, 0);
                if(selectedTabId == 13)
                    bottomHighlight1.method361(229, 16083, 0);
            }
            if(tabInterfaceIds[8] != -1 && (flashingTabId != 8 || gameTick % 20 < 10))
                tabIconSprites[7].method361(74, 16083, 2);
            if(tabInterfaceIds[9] != -1 && (flashingTabId != 9 || gameTick % 20 < 10))
                tabIconSprites[8].method361(102, 16083, 3);
            if(tabInterfaceIds[10] != -1 && (flashingTabId != 10 || gameTick % 20 < 10))
                tabIconSprites[9].method361(137, 16083, 4);
            if(tabInterfaceIds[11] != -1 && (flashingTabId != 11 || gameTick % 20 < 10))
                tabIconSprites[10].method361(174, 16083, 2);
            if(tabInterfaceIds[12] != -1 && (flashingTabId != 12 || gameTick % 20 < 10))
                tabIconSprites[11].method361(201, 16083, 2);
            if(tabInterfaceIds[13] != -1 && (flashingTabId != 13 || gameTick % 20 < 10))
                tabIconSprites[12].method361(226, 16083, 2);
        }
        bottomTabBuffer.method238(466, 23680, graphics, 496);
    }

    static boolean shouldRedrawChatbox(
        int selectedDragState,
        int dragState,
        String activeInputText,
        boolean isMenuOpen,
        int menuScreenArea
    ) {
        if(selectedDragState == 3)
            return true;
        if(dragState == 3)
            return true;
        if(activeInputText != null)
            return true;
        return isMenuOpen && menuScreenArea == 2;
    }
}
