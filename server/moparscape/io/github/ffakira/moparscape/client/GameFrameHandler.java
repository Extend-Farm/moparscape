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
}
