// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import sign.SignLink;

public final class Deque
{

    public Deque(int i)
    {
        aBoolean344 = false;
        anInt345 = -77;
        aClass30_346 = new Node();
        if(i <= 0)
            aBoolean344 = !aBoolean344;
        aClass30_346.aClass30_549 = aClass30_346;
        aClass30_346.aClass30_550 = aClass30_346;
    }

    public void method249(Node class30)
    {
        if(class30.aClass30_550 != null)
            class30.method329();
        class30.aClass30_550 = aClass30_346.aClass30_550;
        class30.aClass30_549 = aClass30_346;
        class30.aClass30_550.aClass30_549 = class30;
        class30.aClass30_549.aClass30_550 = class30;
    }

    public void method250(int i, Node class30)
    {
        if(class30.aClass30_550 != null)
            class30.method329();
        class30.aClass30_550 = aClass30_346;
        class30.aClass30_549 = aClass30_346.aClass30_549;
        while(i >= 0) 
            aBoolean344 = !aBoolean344;
        class30.aClass30_550.aClass30_549 = class30;
        class30.aClass30_549.aClass30_550 = class30;
    }

    public Node method251()
    {
        Node class30 = aClass30_346.aClass30_549;
        if(class30 == aClass30_346)
        {
            return null;
        } else
        {
            class30.method329();
            return class30;
        }
    }

    public Node method252()
    {
        Node class30 = aClass30_346.aClass30_549;
        if(class30 == aClass30_346)
        {
            aClass30_347 = null;
            return null;
        } else
        {
            aClass30_347 = class30.aClass30_549;
            return class30;
        }
    }

    public Node method253(int i)
    {
        if(i < 5 || i > 5)
            throw new NullPointerException();
        Node class30 = aClass30_346.aClass30_550;
        if(class30 == aClass30_346)
        {
            aClass30_347 = null;
            return null;
        } else
        {
            aClass30_347 = class30.aClass30_550;
            return class30;
        }
    }

    public Node method254(boolean flag)
    {
        Node class30 = aClass30_347;
        if(flag)
            anInt345 = 48;
        if(class30 == aClass30_346)
        {
            aClass30_347 = null;
            return null;
        } else
        {
            aClass30_347 = class30.aClass30_549;
            return class30;
        }
    }

    public Node method255(int i)
    {
        Node class30 = aClass30_347;
        if(class30 == aClass30_346)
        {
            aClass30_347 = null;
            return null;
        }
        aClass30_347 = class30.aClass30_550;
        if(i != 8)
            throw new NullPointerException();
        else
            return class30;
    }

    public void method256()
    {
        if(aClass30_346.aClass30_549 == aClass30_346)
            return;
        do
        {
            Node class30 = aClass30_346.aClass30_549;
            if(class30 == aClass30_346)
                return;
            class30.method329();
        } while(true);
    }

    private boolean aBoolean344;
    private int anInt345;
    public Node aClass30_346;
    private Node aClass30_347;
}
