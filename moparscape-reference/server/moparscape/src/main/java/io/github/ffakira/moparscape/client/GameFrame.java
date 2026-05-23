package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.cache.*;
import io.github.ffakira.moparscape.net.*;
import io.github.ffakira.moparscape.sign.SignLink;
import java.awt.*;

// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class GameFrame extends Frame
{

    public GameFrame(GameShell applet_sub1, int i, byte byte0, int j)
    {
        aBoolean35 = true;
        anApplet_Sub1_36 = applet_sub1;
        setTitle("MoparScape");
        setResizable(false);
        setLayout(new BorderLayout());
        gameCanvas = new Canvas();
        gameCanvas.setBackground(Color.black);
        gameCanvas.setPreferredSize(new Dimension(i, j));
        gameCanvas.setFocusable(true);
        gameCanvas.setFocusTraversalKeysEnabled(false);
        setFocusTraversalKeysEnabled(false);
        add(gameCanvas, BorderLayout.CENTER);
        if(byte0 != 5)
            aBoolean35 = !aBoolean35;
        pack();
        setVisible(true);
        toFront();
        setLocationRelativeTo(null);
        requestGameFocus();
    }

    public Graphics getGraphics()
    {
        return gameCanvas.getGraphics();
    }

    public Component getGameComponent()
    {
        return gameCanvas;
    }

    public void requestGameFocus()
    {
        gameCanvas.requestFocus();
        EventQueue.invokeLater(new Runnable() {

            public void run()
            {
                gameCanvas.requestFocusInWindow();
            }

        });
    }

    public final void update(Graphics g)
    {
        anApplet_Sub1_36.update(g);
    }

    public final void paint(Graphics g)
    {
        anApplet_Sub1_36.paint(g);
    }

    private boolean aBoolean35;
    private final Canvas gameCanvas;
    GameShell anApplet_Sub1_36;
}
