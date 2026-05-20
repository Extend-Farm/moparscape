package io.github.ffakira.rsps.client.desktop.character;

record CharacterActorBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {

  float centerX() {
    return (minX + maxX) * 0.5f;
  }

  float centerZ() {
    return (minZ + maxZ) * 0.5f;
  }
}
