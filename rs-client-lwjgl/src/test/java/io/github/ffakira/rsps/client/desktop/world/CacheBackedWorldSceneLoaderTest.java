package io.github.ffakira.rsps.client.desktop.world;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObject;
import io.github.ffakira.rsps.model.WorldPoint;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class CacheBackedWorldSceneLoaderTest {

  @Test
  void loadsProjectedWorldSceneContainingAkiraSpawn() {
    CacheBackedWorldSceneLoader loader = new CacheBackedWorldSceneLoader(Path.of("."));
    WorldPoint akiraSpawn = new WorldPoint(3250, 3227, 0);

    WorldScene worldScene = loader.load(akiraSpawn);
    int spawnLocalX = akiraSpawn.x() - worldScene.originWorldX();
    int spawnLocalY = akiraSpawn.y() - worldScene.originWorldY();

    assertThat(worldScene.sceneKey()).isEqualTo(CacheBackedWorldSceneLoader.sceneKeyFor(akiraSpawn));
    assertThat(worldScene.contains(akiraSpawn)).isTrue();
    assertThat(worldScene.underlayIdAt(spawnLocalX, spawnLocalY)).isPositive();
    assertThat(worldScene.image().width()).isGreaterThan(0);
    assertThat(worldScene.image().height()).isGreaterThan(0);
    assertThat(worldScene.minimapPixelWidthPerTile()).isGreaterThan(1);
    assertThat(worldScene.minimapPixelHeightPerTile()).isGreaterThan(1);
    assertThat(worldScene.minimapImage().width()).isEqualTo(worldScene.tileWidth() * worldScene.minimapPixelWidthPerTile());
    assertThat(worldScene.minimapImage().height()).isEqualTo(worldScene.tileHeight() * worldScene.minimapPixelHeightPerTile());
    assertThat(worldScene.projectPixelX(akiraSpawn)).isBetween(0, worldScene.image().width() - 1);
    assertThat(worldScene.projectPixelY(akiraSpawn)).isBetween(0, worldScene.image().height() - 1);
    assertThat(worldScene.projectMinimapX(akiraSpawn)).isBetween(0, worldScene.minimapImage().width() - 1);
    assertThat(worldScene.projectMinimapY(akiraSpawn)).isBetween(0, worldScene.minimapImage().height() - 1);
    assertThat(worldScene.objects()).isNotEmpty();
    assertThat(worldScene.objects()).allSatisfy(worldSceneObject -> {
      assertThat(worldSceneObject.localX()).isBetween(0, worldScene.tileWidth() - 1);
      assertThat(worldSceneObject.localY()).isBetween(0, worldScene.tileHeight() - 1);
      assertThat(worldSceneObject.name()).isNotBlank();
    });
    assertThat(worldScene.objects().stream().anyMatch(worldSceneObject -> worldSceneObject.geometry() != null)).isTrue();
    Map<String, Long> objectsByTile = worldScene.objects().stream()
        .collect(Collectors.groupingBy(
            object -> object.plane() + ":" + object.localX() + ":" + object.localY(),
            Collectors.counting()
        ));
    assertThat(objectsByTile.values().stream().anyMatch(count -> count > 1)).isTrue();
    assertThat(Arrays.stream(worldScene.image().pixels()).anyMatch(pixel -> pixel != 0)).isTrue();
    assertThat(Arrays.stream(worldScene.minimapImage().pixels()).anyMatch(pixel -> pixel != 0)).isTrue();
  }

  @Test
  void usesBridgeDeckSurfaceAndWaterTintAtLumbridgeBridge() {
    CacheBackedWorldSceneLoader loader = new CacheBackedWorldSceneLoader(Path.of("."));
    WorldPoint bridgeView = new WorldPoint(3244, 3226, 0);

    WorldScene worldScene = loader.load(bridgeView);

    int bridgeLocalX = 3244 - worldScene.originWorldX();
    int bridgeLocalY = 3226 - worldScene.originWorldY();
    int waterLocalY = bridgeLocalY + 1;

    assertThat(worldScene.elevationAt(bridgeLocalX, bridgeLocalY)).isGreaterThan(0);
    int lowestNearbyWaterElevation = Integer.MAX_VALUE;
    for (int localY = bridgeLocalY - 2; localY <= bridgeLocalY + 3; localY++) {
      for (int localX = bridgeLocalX - 3; localX <= bridgeLocalX + 3; localX++) {
        if (localX < 0 || localY < 0 || localX >= worldScene.tileWidth() || localY >= worldScene.tileHeight()) {
          continue;
        }
        if (worldScene.overlayTextureIdAt(localX, localY) == 1) {
          lowestNearbyWaterElevation = Math.min(lowestNearbyWaterElevation, worldScene.elevationAt(localX, localY));
        }
      }
    }
    assertThat(lowestNearbyWaterElevation).isLessThan(Integer.MAX_VALUE);
    assertThat(worldScene.elevationAt(bridgeLocalX, bridgeLocalY)).isGreaterThan(lowestNearbyWaterElevation);
    assertThat(worldScene.overlayTextureIdAt(bridgeLocalX, bridgeLocalY)).isEqualTo(-1);
    assertThat(worldScene.overlayColorAt(bridgeLocalX, bridgeLocalY)).isNotZero();
    assertThat(worldScene.surfacePlaneAt(bridgeLocalX, bridgeLocalY)).isEqualTo(1);
    assertThat(worldScene.bridgeTerrainLayer().activeAt(bridgeLocalX, bridgeLocalY)).isTrue();
    assertThat(worldScene.bridgeTerrainLayer().overlayIdAt(bridgeLocalX, bridgeLocalY)).isPositive();
    assertThat(worldScene.bridgeTerrainLayer().overlayTextureIdAt(bridgeLocalX, bridgeLocalY)).isEqualTo(1);
    int underBridgeRgb = worldScene.bridgeTerrainLayer().overlayColorAt(bridgeLocalX, bridgeLocalY);
    assertThat(underBridgeRgb & 0xff).isGreaterThan((underBridgeRgb >>> 16) & 0xff);

    assertThat(worldScene.overlayIdAt(bridgeLocalX, waterLocalY)).isPositive();
    assertThat(worldScene.surfacePlaneAt(bridgeLocalX, waterLocalY)).isEqualTo(0);
    assertThat(worldScene.overlayTextureIdAt(bridgeLocalX, waterLocalY)).isEqualTo(1);
    int waterRgb = worldScene.overlayColorAt(bridgeLocalX, waterLocalY);
    assertThat(waterRgb & 0xff).isGreaterThan((waterRgb >>> 16) & 0xff);
    assertThat(worldScene.objects())
        .filteredOn(object -> object.localX() >= bridgeLocalX - 4 && object.localX() <= bridgeLocalX + 4)
        .filteredOn(object -> object.localY() >= bridgeLocalY - 2 && object.localY() <= bridgeLocalY + 2)
        .allMatch(object -> object.plane() == 0);
    assertThat(worldScene.objects())
        .anyMatch(object -> object.objectId() == 3007
            && object.localX() >= bridgeLocalX - 4
            && object.localX() <= bridgeLocalX + 6
            && object.localY() >= bridgeLocalY - 2
            && object.localY() <= bridgeLocalY + 1
            && object.plane() == 0);
    assertThat(worldScene.objects().stream()
        .filter(object -> object.objectId() >= 2999 && object.objectId() <= 3013)
        .map(WorldSceneObject::geometry)
        .filter(Objects::nonNull)
        .anyMatch(geometry -> hasVertexBelowSurface(geometry.vertexY())))
        .isTrue();
  }

  private static boolean hasVertexBelowSurface(float[] vertexY) {
    for (float value : vertexY) {
      if (value < -0.01f) {
        return true;
      }
    }
    return false;
  }
}
