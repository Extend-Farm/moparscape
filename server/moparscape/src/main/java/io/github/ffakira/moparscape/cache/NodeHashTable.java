package io.github.ffakira.moparscape.cache;

import io.github.ffakira.moparscape.client.*;
import io.github.ffakira.moparscape.sign.SignLink;

// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
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
        for(int bucketIndex = 0; bucketIndex < j; bucketIndex++)
        {
            Node sentinelNode = buckets[bucketIndex] = new Node();
            sentinelNode.prev = sentinelNode;
            sentinelNode.next = sentinelNode;
        }

    }

    public Node get(long key)
    {
        Node bucketSentinel = buckets[(int)(key & (long)(bucketCount - 1))];
        for(Node current = bucketSentinel.prev; current != bucketSentinel; current = current.prev)
            if(current.key == key)
                return current;

        return null;
    }

    public void put(Node node, long key, byte controlByte)
    {
        try
        {
            if(node.next != null)
                node.unlink();
            Node bucketSentinel = buckets[(int)(key & (long)(bucketCount - 1))];
            if(controlByte != 7)
            {
                return;
            } else
            {
                node.next = bucketSentinel.next;
                node.prev = bucketSentinel;
                node.next.prev = node;
                node.prev.next = node;
                node.key = key;
                return;
            }
        }
        catch(RuntimeException runtimeexception)
        {
            SignLink.reporterror("91499, " + node + ", " + key + ", " + controlByte + ", " + runtimeexception.toString());
        }
        throw new RuntimeException();
    }

    // TODO: Rename/verify legacy decompiler leftovers when behavior is fully documented.
    private boolean aBoolean37;
    private int anInt38;
    private int bucketCount;
    private Node buckets[];
}