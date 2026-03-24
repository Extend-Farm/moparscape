// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import sign.SignLink;

public final class CacheableNodeDeque
{

    public CacheableNodeDeque(int i)
    {
        aBoolean41 = false;
        anInt42 = -589;
        aClass30_Sub2_43 = new CacheableNode();
        if(i != 0)
            anInt42 = -25;
        aClass30_Sub2_43.aClass30_Sub2_1303 = aClass30_Sub2_43;
        aClass30_Sub2_43.aClass30_Sub2_1304 = aClass30_Sub2_43;
    }

    public void method150(CacheableNode class30_sub2)
    {
        if(class30_sub2.aClass30_Sub2_1304 != null)
            class30_sub2.method330();
        class30_sub2.aClass30_Sub2_1304 = aClass30_Sub2_43.aClass30_Sub2_1304;
        class30_sub2.aClass30_Sub2_1303 = aClass30_Sub2_43;
        class30_sub2.aClass30_Sub2_1304.aClass30_Sub2_1303 = class30_sub2;
        class30_sub2.aClass30_Sub2_1303.aClass30_Sub2_1304 = class30_sub2;
    }

    public CacheableNode method151()
    {
        CacheableNode class30_sub2 = aClass30_Sub2_43.aClass30_Sub2_1303;
        if(class30_sub2 == aClass30_Sub2_43)
        {
            return null;
        } else
        {
            class30_sub2.method330();
            return class30_sub2;
        }
    }

    public CacheableNode method152()
    {
        CacheableNode class30_sub2 = aClass30_Sub2_43.aClass30_Sub2_1303;
        if(class30_sub2 == aClass30_Sub2_43)
        {
            aClass30_Sub2_44 = null;
            return null;
        } else
        {
            aClass30_Sub2_44 = class30_sub2.aClass30_Sub2_1303;
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
            aClass30_Sub2_44 = class30_sub2.aClass30_Sub2_1303;
            return class30_sub2;
        }
    }

    public int method154()
    {
        int i = 0;
        for(CacheableNode class30_sub2 = aClass30_Sub2_43.aClass30_Sub2_1303; class30_sub2 != aClass30_Sub2_43; class30_sub2 = class30_sub2.aClass30_Sub2_1303)
            i++;

        return i;
    }

    private boolean aBoolean41;
    private int anInt42;
    public CacheableNode aClass30_Sub2_43;
    private CacheableNode aClass30_Sub2_44;
}
