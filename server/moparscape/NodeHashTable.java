// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import sign.SignLink;

public final class NodeHashTable
{

    public NodeHashTable(int i, int j)
    {
        aBoolean37 = false;
        anInt38 = -373;
        if(i >= 0)
            throw new NullPointerException();
        bucketCount = j;
        buckets = new Node[j];
        for(int k = 0; k < j; k++)
        {
            Node class30 = buckets[k] = new Node();
            class30.prev = class30;
            class30.next = class30;
        }

    }

    public Node get(long l)
    {
        Node class30 = buckets[(int)(l & (long)(bucketCount - 1))];
        for(Node class30_1 = class30.prev; class30_1 != class30; class30_1 = class30_1.prev)
            if(class30_1.key == l)
                return class30_1;

        return null;
    }

    public void put(Node class30, long l, byte byte0)
    {
        try
        {
            if(class30.next != null)
                class30.unlink();
            Node class30_1 = buckets[(int)(l & (long)(bucketCount - 1))];
            if(byte0 != 7)
            {
                return;
            } else
            {
                class30.next = class30_1.next;
                class30.prev = class30_1;
                class30.next.prev = class30;
                class30.prev.next = class30;
                class30.key = l;
                return;
            }
        }
        catch(RuntimeException runtimeexception)
        {
            SignLink.reporterror("91499, " + class30 + ", " + l + ", " + byte0 + ", " + runtimeexception.toString());
        }
        throw new RuntimeException();
    }

    private boolean aBoolean37;
    private int anInt38;
    private int bucketCount;
    private Node buckets[];
}
