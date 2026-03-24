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
        sentinel = new Node();
        if(i <= 0)
            aBoolean344 = !aBoolean344;
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    public void addFirst(Node class30)
    {
        if(class30.next != null)
            class30.unlink();
        class30.next = sentinel.next;
        class30.prev = sentinel;
        class30.next.prev = class30;
        class30.prev.next = class30;
    }

    public void addLast(int i, Node class30)
    {
        if(class30.next != null)
            class30.unlink();
        class30.next = sentinel;
        class30.prev = sentinel.prev;
        while(i >= 0) 
            aBoolean344 = !aBoolean344;
        class30.next.prev = class30;
        class30.prev.next = class30;
    }

    public Node removeLast()
    {
        Node class30 = sentinel.prev;
        if(class30 == sentinel)
        {
            return null;
        } else
        {
            class30.unlink();
            return class30;
        }
    }

    public Node last()
    {
        Node class30 = sentinel.prev;
        if(class30 == sentinel)
        {
            current = null;
            return null;
        } else
        {
            current = class30.prev;
            return class30;
        }
    }

    public Node first(int i)
    {
        if(i < 5 || i > 5)
            throw new NullPointerException();
        Node class30 = sentinel.next;
        if(class30 == sentinel)
        {
            current = null;
            return null;
        } else
        {
            current = class30.next;
            return class30;
        }
    }

    public Node previous(boolean flag)
    {
        Node class30 = current;
        if(flag)
            anInt345 = 48;
        if(class30 == sentinel)
        {
            current = null;
            return null;
        } else
        {
            current = class30.prev;
            return class30;
        }
    }

    public Node next(int i)
    {
        Node class30 = current;
        if(class30 == sentinel)
        {
            current = null;
            return null;
        }
        current = class30.next;
        if(i != 8)
            throw new NullPointerException();
        else
            return class30;
    }

    public void clear()
    {
        if(sentinel.prev == sentinel)
            return;
        do
        {
            Node class30 = sentinel.prev;
            if(class30 == sentinel)
                return;
            class30.unlink();
        } while(true);
    }

    private boolean aBoolean344;
    private int anInt345;
    public Node sentinel;
    private Node current;
}
