package io.github.ffakira.rsps.client.desktop.world.visibility;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObject;
import java.util.List;
import org.junit.jupiter.api.Test;

class WorldSceneOccluderBuilderTest {

  @Test
  void buildsWallAndHorizontalOccludersFromSceneObjects() {
    WorldSceneObject wall = new WorldSceneObject(1, "Wall", 10, 8, 0, 0, 0, 1, 1, false, -1, -1, List.of(), true, null);
    WorldSceneObject roof = new WorldSceneObject(2, "Roof", 12, 12, 0, 10, 0, 3, 2, false, -1, -1, List.of(), true, null);

    List<WorldSceneOccluder> occluders = new WorldSceneOccluderBuilder().build(
        0,
        32,
        32,
        new int[32 * 32],
        List.of(wall, roof)
    );

    assertThat(occluders).extracting(WorldSceneOccluder::type)
        .contains(WorldSceneOccluderType.X_WALL, WorldSceneOccluderType.HORIZONTAL_PLANE);
  }
}
