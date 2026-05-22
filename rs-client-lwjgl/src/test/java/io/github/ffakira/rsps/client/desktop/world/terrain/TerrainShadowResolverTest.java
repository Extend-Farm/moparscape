package io.github.ffakira.rsps.client.desktop.world.terrain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObject;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObjectGeometry;
import java.util.List;
import org.junit.jupiter.api.Test;

class TerrainShadowResolverTest {

  private final TerrainShadowResolver resolver = new TerrainShadowResolver();

  @Test
  void appliesLegacyWallShadowSamplesForStraightWalls() {
    byte[] shadowSamples = resolver.resolve(
        4,
        4,
        List.of(new WorldSceneObject(1, "Wall", 1, 1, 0, 0, 0, 1, 1, false, -1, -1, List.of(), true, null))
    );

    assertThat(shadowSamples[index(4, 1, 1)] & 0xff).isEqualTo(50);
    assertThat(shadowSamples[index(4, 1, 2)] & 0xff).isEqualTo(50);
    assertThat(shadowSamples[index(4, 2, 1)] & 0xff).isZero();
  }

  @Test
  void respectsCastsShadowFlagAndClampsLargeObjectStrength() {
    WorldSceneObjectGeometry tallGeometry = new WorldSceneObjectGeometry(
        new float[]{0.0f, 0.0f},
        new float[]{0.0f, 4.0f},
        new float[]{0.0f, 0.0f},
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{255},
        new io.github.ffakira.rsps.client.desktop.world.raster.SceneRasterMode[]{
            io.github.ffakira.rsps.client.desktop.world.raster.SceneRasterMode.GOURAUD
        },
        new int[]{-1},
        new int[]{-1},
        new int[]{-1},
        new int[]{-1}
    );
    byte[] shadowSamples = resolver.resolve(
        6,
        6,
        List.of(
            new WorldSceneObject(2, "Tree", 2, 2, 0, 10, 0, 1, 1, false, -1, -1, List.of(), true, tallGeometry),
            new WorldSceneObject(3, "No-shadow wall", 0, 0, 0, 0, 0, 1, 1, false, -1, -1, List.of(), true, false, null)
        )
    );

    assertThat(shadowSamples[index(6, 2, 2)] & 0xff).isEqualTo(30);
    assertThat(shadowSamples[index(6, 3, 3)] & 0xff).isEqualTo(30);
    assertThat(shadowSamples[index(6, 0, 0)] & 0xff).isZero();
  }

  @Test
  void doesNotTreatDiagonalWallsAsLargeObjectShadowCasters() {
    WorldSceneObjectGeometry diagonalGeometry = new WorldSceneObjectGeometry(
        new float[]{-0.5f, 0.5f},
        new float[]{0.0f, 1.5f},
        new float[]{-0.5f, 0.5f},
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{255},
        new io.github.ffakira.rsps.client.desktop.world.raster.SceneRasterMode[]{
            io.github.ffakira.rsps.client.desktop.world.raster.SceneRasterMode.GOURAUD
        },
        new int[]{-1},
        new int[]{-1},
        new int[]{-1},
        new int[]{-1}
    );

    byte[] shadowSamples = resolver.resolve(
        6,
        6,
        List.of(new WorldSceneObject(9, "Diagonal wall", 2, 2, 0, 9, 0, 1, 1, false, -1, -1, List.of(), true, diagonalGeometry))
    );

    for (byte shadowSample : shadowSamples) {
      assertThat(shadowSample & 0xff).isZero();
    }
  }

  private static int index(int width, int x, int y) {
    return y * width + x;
  }
}
