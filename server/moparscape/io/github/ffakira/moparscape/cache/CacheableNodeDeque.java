package io.github.ffakira.moparscape.cache;

import io.github.ffakira.moparscape.client.*;
import io.github.ffakira.moparscape.sign.SignLink;

// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
public final class CacheableNodeDeque
{

    public CacheableNodeDeque(int i)
    {
        aBoolean41 = false;
        anInt42 = -589;
        aClass30_Sub2_43 = new CacheableNode();
        if(i != 0)
            anInt42 = -25;
        aClass30_Sub2_43.previousDual = aClass30_Sub2_43;
        aClass30_Sub2_43.nextDual = aClass30_Sub2_43;
    }

    public void method150(CacheableNode class30_sub2)
    {
        if(class30_sub2.nextDual != null)
            class30_sub2.unlinkDual();
        class30_sub2.nextDual = aClass30_Sub2_43.nextDual;
        class30_sub2.previousDual = aClass30_Sub2_43;
        class30_sub2.nextDual.previousDual = class30_sub2;
        class30_sub2.previousDual.nextDual = class30_sub2;
    }

    public CacheableNode method151()
    {
        CacheableNode class30_sub2 = aClass30_Sub2_43.previousDual;
        if(class30_sub2 == aClass30_Sub2_43)
        {
            return null;
        } else
        {
            class30_sub2.unlinkDual();
            return class30_sub2;
        }
    }

    public CacheableNode method152()
    {
        CacheableNode class30_sub2 = aClass30_Sub2_43.previousDual;
        if(class30_sub2 == aClass30_Sub2_43)
        {
            aClass30_Sub2_44 = null;
            return null;
        } else
        {
            aClass30_Sub2_44 = class30_sub2.previousDual;
            return class30_sub2;
        }
    }

    public CacheableNode method153(boolean flag)
    {
        if(flag)
            throw new NullPointerException();
        CacheableNode class30_sub2 = aClass30_Sub2_44;
        if(class30_sub2 == aClass30_Sub2_43)
        {
            aClass30_Sub2_44 = null;
            return null;
        } else
        {
            aClass30_Sub2_44 = class30_sub2.previousDual;
            return class30_sub2;
        }
    }

    public int method154()
    {
        int i = 0;
        for(CacheableNode class30_sub2 = aClass30_Sub2_43.previousDual; class30_sub2 != aClass30_Sub2_43; class30_sub2 = class30_sub2.previousDual)
            i++;

        return i;
    }

    private boolean aBoolean41;
    private int anInt42;
    public CacheableNode aClass30_Sub2_43;
    private CacheableNode aClass30_Sub2_44;
}