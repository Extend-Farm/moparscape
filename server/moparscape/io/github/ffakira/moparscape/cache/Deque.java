package io.github.ffakira.moparscape.cache;

import io.github.ffakira.moparscape.client.*;
import io.github.ffakira.moparscape.sign.SignLink;

// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
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

    public void addFirst(Node node)
    {
        if(node.next != null)
            node.unlink();
        node.next = sentinel.next;
        node.prev = sentinel;
        node.next.prev = node;
        node.prev.next = node;
    }

    public void addLast(int i, Node node)
    {
        if(node.next != null)
            node.unlink();
        node.next = sentinel;
        node.prev = sentinel.prev;
        while(i >= 0) 
            aBoolean344 = !aBoolean344;
        node.next.prev = node;
        node.prev.next = node;
    }

    public Node removeLast()
    {
        Node lastNode = sentinel.prev;
        if(lastNode == sentinel)
        {
            return null;
        } else
        {
            lastNode.unlink();
            return lastNode;
        }
    }

    public Node last()
    {
        Node lastNode = sentinel.prev;
        if(lastNode == sentinel)
        {
            current = null;
            return null;
        } else
        {
            current = lastNode.prev;
            return lastNode;
        }
    }

    public Node first(int i)
    {
        if(i < 5 || i > 5)
            throw new NullPointerException();
        Node firstNode = sentinel.next;
        if(firstNode == sentinel)
        {
            current = null;
            return null;
        } else
        {
            current = firstNode.next;
            return firstNode;
        }
    }

    public Node previous(boolean flag)
    {
        Node previousNode = current;
        if(flag)
            anInt345 = 48;
        if(previousNode == sentinel)
        {
            current = null;
            return null;
        } else
        {
            current = previousNode.prev;
            return previousNode;
        }
    }

    public Node next(int i)
    {
        Node nextNode = current;
        if(nextNode == sentinel)
        {
            current = null;
            return null;
        }
        current = nextNode.next;
        if(i != 8)
            throw new NullPointerException();
        else
            return nextNode;
    }

    public void clear()
    {
        if(sentinel.prev == sentinel)
            return;
        do
        {
            Node lastNode = sentinel.prev;
            if(lastNode == sentinel)
                return;
            lastNode.unlink();
        } while(true);
    }

    // TODO: Rename/verify legacy decompiler leftovers when behavior is fully documented.
    private boolean aBoolean344;
    private int anInt345;
    public Node sentinel;
    private Node current;
}