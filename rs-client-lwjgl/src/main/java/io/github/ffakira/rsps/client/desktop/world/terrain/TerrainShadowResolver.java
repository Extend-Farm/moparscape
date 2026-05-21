package io.github.ffakira.rsps.client.desktop.world.terrain;

import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObject;
import java.util.List;

public final class TerrainShadowResolver {

  private static final int WALL_SHADOW = 50;
  private static final int MAX_OBJECT_SHADOW = 50;
  private static final float LEGACY_MODEL_UNIT_SCALE = 32.0f;

  public byte[] resolve(int sampleWidth, int sampleHeight, List<WorldSceneObject> sceneObjects) {
    byte[] shadowSamples = new byte[sampleWidth * sampleHeight];
    for (WorldSceneObject sceneObject : sceneObjects) {
      if (!sceneObject.castsShadow()) {
        continue;
      }
      // Match the legacy MapRegion shading grid: straight walls, corner walls, and large
      // interactives stamp byte shadow samples that the terrain light later subtracts.
      switch (sceneObject.type()) {
        case 0 -> applyStraightWallShadow(shadowSamples, sampleWidth, sampleHeight, sceneObject);
        case 1, 3 -> applyCornerWallShadow(shadowSamples, sampleWidth, sampleHeight, sceneObject);
        // Type 9 is a diagonal wall; types 10-11 are full interactives (trees, statues, machines);
        // types 12-17 are wall-attached interactives that nonetheless rest on the ground and cast
        // visible ground shade (lecterns, signposts, banner stands, market crates, etc).
        case 9, 10, 11, 12, 13, 14, 15, 16, 17 -> applyLargeObjectShadow(shadowSamples, sampleWidth, sampleHeight, sceneObject);
        default -> {
        }
      }
    }
    return shadowSamples;
  }

  private void applyStraightWallShadow(
      byte[] shadowSamples,
      int sampleWidth,
      int sampleHeight,
      WorldSceneObject sceneObject
  ) {
    int sampleX = sceneObject.localX();
    int sampleY = sceneObject.localY();
    switch (sceneObject.orientation() & 3) {
      case 0 -> {
        setShadow(shadowSamples, sampleWidth, sampleHeight, sampleX, sampleY, WALL_SHADOW);
        setShadow(shadowSamples, sampleWidth, sampleHeight, sampleX, sampleY + 1, WALL_SHADOW);
      }
      case 1 -> {
        setShadow(shadowSamples, sampleWidth, sampleHeight, sampleX, sampleY + 1, WALL_SHADOW);
        setShadow(shadowSamples, sampleWidth, sampleHeight, sampleX + 1, sampleY + 1, WALL_SHADOW);
      }
      case 2 -> {
        setShadow(shadowSamples, sampleWidth, sampleHeight, sampleX + 1, sampleY, WALL_SHADOW);
        setShadow(shadowSamples, sampleWidth, sampleHeight, sampleX + 1, sampleY + 1, WALL_SHADOW);
      }
      case 3 -> {
        setShadow(shadowSamples, sampleWidth, sampleHeight, sampleX, sampleY, WALL_SHADOW);
        setShadow(shadowSamples, sampleWidth, sampleHeight, sampleX + 1, sampleY, WALL_SHADOW);
      }
      default -> {
      }
    }
  }

  private void applyCornerWallShadow(
      byte[] shadowSamples,
      int sampleWidth,
      int sampleHeight,
      WorldSceneObject sceneObject
  ) {
    int sampleX = sceneObject.localX();
    int sampleY = sceneObject.localY();
    switch (sceneObject.orientation() & 3) {
      case 0 -> setShadow(shadowSamples, sampleWidth, sampleHeight, sampleX, sampleY + 1, WALL_SHADOW);
      case 1 -> setShadow(shadowSamples, sampleWidth, sampleHeight, sampleX + 1, sampleY + 1, WALL_SHADOW);
      case 2 -> setShadow(shadowSamples, sampleWidth, sampleHeight, sampleX + 1, sampleY, WALL_SHADOW);
      case 3 -> setShadow(shadowSamples, sampleWidth, sampleHeight, sampleX, sampleY, WALL_SHADOW);
      default -> {
      }
    }
  }

  private void applyLargeObjectShadow(
      byte[] shadowSamples,
      int sampleWidth,
      int sampleHeight,
      WorldSceneObject sceneObject
  ) {
    int shadowStrength = objectShadowStrength(sceneObject);
    if (shadowStrength <= 0) {
      return;
    }
    int maxSampleX = sceneObject.localX() + Math.max(1, sceneObject.sizeX());
    int maxSampleY = sceneObject.localY() + Math.max(1, sceneObject.sizeY());
    for (int sampleY = sceneObject.localY(); sampleY <= maxSampleY; sampleY++) {
      for (int sampleX = sceneObject.localX(); sampleX <= maxSampleX; sampleX++) {
        setShadow(shadowSamples, sampleWidth, sampleHeight, sampleX, sampleY, shadowStrength);
      }
    }
    // Legacy MapRegion lights from the north-west, so each tall object casts a soft shadow off to
    // the south-east. Stamp a one-tile penumbra at reduced strength in that direction so the
    // shadow reads as a real cast shadow rather than a uniform dark blob under the footprint.
    int penumbraStrength = (shadowStrength * 5) / 8;
    if (penumbraStrength <= 0) {
      return;
    }
    int penumbraMinX = sceneObject.localX() + 1;
    int penumbraMaxX = maxSampleX + 1;
    int penumbraMinY = sceneObject.localY() - 1;
    int penumbraMaxY = maxSampleY - 1;
    for (int sampleY = penumbraMinY; sampleY <= penumbraMaxY; sampleY++) {
      for (int sampleX = penumbraMinX; sampleX <= penumbraMaxX; sampleX++) {
        setShadow(shadowSamples, sampleWidth, sampleHeight, sampleX, sampleY, penumbraStrength);
      }
    }
  }

  private int objectShadowStrength(WorldSceneObject sceneObject) {
    if (sceneObject.geometry() != null) {
      float maxRadius = 0.0f;
      float[] vertexX = sceneObject.geometry().vertexX();
      float[] vertexZ = sceneObject.geometry().vertexZ();
      for (int index = 0; index < vertexX.length; index++) {
        maxRadius = Math.max(maxRadius, (float) Math.hypot(vertexX[index], vertexZ[index]));
      }
      return clamp(Math.round(maxRadius * LEGACY_MODEL_UNIT_SCALE), 0, MAX_OBJECT_SHADOW);
    }
    float fallbackRadius = (float) Math.hypot(
        Math.max(1, sceneObject.sizeX()) * 0.5f,
        Math.max(1, sceneObject.sizeY()) * 0.5f
    );
    return clamp(Math.round(fallbackRadius * LEGACY_MODEL_UNIT_SCALE), 0, MAX_OBJECT_SHADOW);
  }

  private void setShadow(byte[] shadowSamples, int sampleWidth, int sampleHeight, int sampleX, int sampleY, int shadowStrength) {
    if (sampleX < 0 || sampleY < 0 || sampleX >= sampleWidth || sampleY >= sampleHeight) {
      return;
    }
    int index = sampleY * sampleWidth + sampleX;
    int currentShadow = shadowSamples[index] & 0xff;
    if (shadowStrength > currentShadow) {
      shadowSamples[index] = (byte) shadowStrength;
    }
  }

  private int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }
}
