package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.cache.Deque;

final class SceneEffectHandler {

    private SceneEffectHandler() {
    }

    // Exact extraction of legacy method104 graphics-object tick/update loop.
    static boolean updateGraphicsObjects(
        Deque graphicsObjectDeque,
        int currentPlane,
        int gameTick,
        int animationStep,
        SceneGraph scene,
        boolean preserveConnectionFlag,
        boolean currentConnectionFlag
    ) {
        GraphicsObject graphicsObject = (GraphicsObject)graphicsObjectDeque.last();
        for(; graphicsObject != null; graphicsObject = (GraphicsObject)graphicsObjectDeque.previous(false))
            if(graphicsObject.anInt1560 != currentPlane || graphicsObject.aBoolean1567)
                graphicsObject.unlink();
            else
            if(gameTick >= graphicsObject.anInt1564)
            {
                graphicsObject.method454(animationStep, true);
                if(graphicsObject.aBoolean1567)
                    graphicsObject.unlink();
                else
                    scene.method285(graphicsObject.anInt1560, 0, (byte)6, graphicsObject.anInt1563, -1, graphicsObject.anInt1562, 60, graphicsObject.anInt1561, graphicsObject, false);
            }

        return currentConnectionFlag & preserveConnectionFlag;
    }
}
