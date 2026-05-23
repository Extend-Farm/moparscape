package io.github.ffakira.moparscape.cache;

import io.github.ffakira.moparscape.client.*;
import io.github.ffakira.moparscape.sign.SignLink;

// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 


public class CacheableNode extends Node
{

    public void unlinkDual()
    {
        if(nextDual == null)
        {
            return;
        } else
        {
            nextDual.previousDual = previousDual;
            previousDual.nextDual = nextDual;
            previousDual = null;
            nextDual = null;
            return;
        }
    }

    public CacheableNode()
    {
    }

    public CacheableNode previousDual;
    CacheableNode nextDual;
    public static int anInt1305;
}