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
        show();
        if(byte0 != 5)
            aBoolean35 = !aBoolean35;
        toFront();
        resize(i + 8, j + 28);
    }

    public Graphics getGraphics()
    {
        Graphics g = super.getGraphics();
        g.translate(4, 24);
        return g;
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
    GameShell anApplet_Sub1_36;
}