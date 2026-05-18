package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.cache.*;
import io.github.ffakira.moparscape.net.*;
import io.github.ffakira.moparscape.sign.SignLink;

// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
final class GroundItem extends Renderable
{

    public final Model method444(int i)
    {
        ItemDefinition class8 = ItemDefinition.method198(anInt1558);
        if(i != 4016)
            throw new NullPointerException();
        else
            return class8.method201(anInt1559);
    }

    GroundItem()
    {
    }

    public int anInt1558;
    public int anInt1559;
}