package io.github.ffakira.rsps.client.lwjgl;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.model.WorldPoint;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class CacheBackedWorldSceneLoaderTest {

  @Test
  void loadsProjectedWorldSceneContainingAkiraSpawn() {
    CacheBackedWorldSceneLoader loader = new CacheBackedWorldSceneLoader(Path.of("."));
    WorldPoint akiraSpawn = new WorldPoint(3250, 3227, 0);

    WorldScene worldScene = loader.load(akiraSpawn);

    assertThat(worldScene.sceneKey()).isEqualTo(CacheBackedWorldSceneLoader.sceneKeyFor(akiraSpawn));
    assertThat(worldScene.contains(akiraSpawn)).isTrue();
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
}
