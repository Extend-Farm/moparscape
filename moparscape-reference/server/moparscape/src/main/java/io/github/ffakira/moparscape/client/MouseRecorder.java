package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.cache.*;
import io.github.ffakira.moparscape.net.*;
import io.github.ffakira.moparscape.sign.SignLink;

// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
public class MouseRecorder
    implements Runnable
{

    public void run()
    {
        while(aBoolean808) 
        {
            synchronized(anObject806)
            {
                if(anInt810 < 500)
                {
                    anIntArray809[anInt810] = ((GameShell) (aClient805)).anInt20;
                    anIntArray807[anInt810] = ((GameShell) (aClient805)).anInt21;
                    anInt810++;
                }
            }
            try
            {
                Thread.sleep(50L);
            }
            catch(Exception _ex) { }
        }
    }

    public MouseRecorder(GameClient client1, int i)
    {
        anObject806 = new Object();
        anIntArray807 = new int[500];
        aBoolean808 = true;
        for(anIntArray809 = new int[500]; i >= 0;)
            throw new NullPointerException();

        aClient805 = client1;
    }

    public GameClient aClient805;
    public Object anObject806;
    public int anIntArray807[];
    public boolean aBoolean808;
    public int anIntArray809[];
    public int anInt810;
}