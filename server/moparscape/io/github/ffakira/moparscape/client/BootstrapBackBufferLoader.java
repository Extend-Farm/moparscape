package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.cache.Archive;
import java.awt.Component;

final class BootstrapBackBufferLoader {

    private BootstrapBackBufferLoader() {
    }

    static ProducingGraphicsBuffer[] createBackBuffers(Archive mediaArchive, Component component)
    {
        ProducingGraphicsBuffer[] buffers = new ProducingGraphicsBuffer[9];
        Sprite sprite = new Sprite(mediaArchive, "backleft1", 0);
        buffers[0] = new ProducingGraphicsBuffer(sprite.anInt1440, sprite.anInt1441, component, 0);
        sprite.method346(0, 0, -32357);
        sprite = new Sprite(mediaArchive, "backleft2", 0);
        buffers[1] = new ProducingGraphicsBuffer(sprite.anInt1440, sprite.anInt1441, component, 0);
        sprite.method346(0, 0, -32357);
        sprite = new Sprite(mediaArchive, "backright1", 0);
        buffers[2] = new ProducingGraphicsBuffer(sprite.anInt1440, sprite.anInt1441, component, 0);
        sprite.method346(0, 0, -32357);
        sprite = new Sprite(mediaArchive, "backright2", 0);
        buffers[3] = new ProducingGraphicsBuffer(sprite.anInt1440, sprite.anInt1441, component, 0);
        sprite.method346(0, 0, -32357);
        sprite = new Sprite(mediaArchive, "backtop1", 0);
        buffers[4] = new ProducingGraphicsBuffer(sprite.anInt1440, sprite.anInt1441, component, 0);
        sprite.method346(0, 0, -32357);
        sprite = new Sprite(mediaArchive, "backvmid1", 0);
        buffers[5] = new ProducingGraphicsBuffer(sprite.anInt1440, sprite.anInt1441, component, 0);
        sprite.method346(0, 0, -32357);
        sprite = new Sprite(mediaArchive, "backvmid2", 0);
        buffers[6] = new ProducingGraphicsBuffer(sprite.anInt1440, sprite.anInt1441, component, 0);
        sprite.method346(0, 0, -32357);
        sprite = new Sprite(mediaArchive, "backvmid3", 0);
        buffers[7] = new ProducingGraphicsBuffer(sprite.anInt1440, sprite.anInt1441, component, 0);
        sprite.method346(0, 0, -32357);
        sprite = new Sprite(mediaArchive, "backhmid2", 0);
        buffers[8] = new ProducingGraphicsBuffer(sprite.anInt1440, sprite.anInt1441, component, 0);
        sprite.method346(0, 0, -32357);
        return buffers;
    }
}
