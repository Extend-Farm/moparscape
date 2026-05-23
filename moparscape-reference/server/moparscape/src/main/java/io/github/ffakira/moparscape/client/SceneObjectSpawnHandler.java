package io.github.ffakira.moparscape.client;

final class SceneObjectSpawnHandler {

    private SceneObjectSpawnHandler() {
    }

    static int[] readSpawnAttributes(SceneGraph sceneGraph, SceneObjectSpawnRequest spawnRequest)
    {
        int sceneObject = 0;
        int objectId = -1;
        int objectType = 0;
        int objectOrientation = 0;
        if(spawnRequest.anInt1296 == 0)
            sceneObject = sceneGraph.method300(spawnRequest.anInt1295, spawnRequest.anInt1297, spawnRequest.anInt1298);
        if(spawnRequest.anInt1296 == 1)
            sceneObject = sceneGraph.method301(spawnRequest.anInt1295, spawnRequest.anInt1297, 0, spawnRequest.anInt1298);
        if(spawnRequest.anInt1296 == 2)
            sceneObject = sceneGraph.method302(spawnRequest.anInt1295, spawnRequest.anInt1297, spawnRequest.anInt1298);
        if(spawnRequest.anInt1296 == 3)
            sceneObject = sceneGraph.method303(spawnRequest.anInt1295, spawnRequest.anInt1297, spawnRequest.anInt1298);
        if(sceneObject != 0)
        {
            int sceneFlags = sceneGraph.method304(spawnRequest.anInt1295, spawnRequest.anInt1297, spawnRequest.anInt1298, sceneObject);
            objectId = sceneObject >> 14 & 0x7fff;
            objectType = sceneFlags & 0x1f;
            objectOrientation = sceneFlags >> 6;
        }
        return new int[] {
            objectId, objectType, objectOrientation
        };
    }
}
