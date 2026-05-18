package io.github.ffakira.moparscape.cache;

import io.github.ffakira.moparscape.client.*;
import io.github.ffakira.moparscape.sign.SignLink;

// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 


public class Node
{

    public void unlink()
    {
        if(next == null)
        {
            return;
        } else
        {
            next.prev = prev;
            prev.next = next;
            prev = null;
            next = null;
            return;
        }
    }

    public Node()
    {
        aBoolean547 = true;
    }

    private boolean aBoolean547;
    public long key;
    public Node prev;
    Node next;
    public static boolean aBoolean551;
}