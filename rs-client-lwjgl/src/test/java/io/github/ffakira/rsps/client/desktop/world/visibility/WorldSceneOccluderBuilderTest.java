package io.github.ffakira.rsps.client.desktop.world.visibility;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.client.desktop.world.terrain.TerrainOcclusionFlagResolver;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObject;
import java.util.List;
import org.junit.jupiter.api.Test;

class WorldSceneOccluderBuilderTest {

  @Test
  void buildsWallOccludersFromObjectsAndHorizontalOccludersFromTerrainFlags() {
    WorldSceneObject wall = new WorldSceneObject(1, "Wall", 10, 8, 0, 0, 0, 1, 1, false, -1, -1, List.of(), true, null);
    byte[] surfacePlanes = new byte[32 * 32];
    int[] terrainOcclusionFlags = new int[32 * 32];
    for (int tileY = 12; tileY <= 13; tileY++) {
      for (int tileX = 12; tileX <= 13; tileX++) {
        int tileIndex = tileY * 32 + tileX;
        surfacePlanes[tileIndex] = 1;
        terrainOcclusionFlags[tileIndex] = TerrainOcclusionFlagResolver.LEGACY_FLAT_TILE_OCCLUSION_MASK;
      }
    }

    List<WorldSceneOccluder> occluders = new WorldSceneOccluderBuilder().build(
        0,
        32,
        32,
        new int[32 * 32],
        surfacePlanes,
        terrainOcclusionFlags,
        List.of(wall)
    );

    assertThat(occluders).extracting(WorldSceneOccluder::type)
        .contains(WorldSceneOccluderType.X_WALL, WorldSceneOccluderType.HORIZONTAL_PLANE);
  }
}
