package io.github.ffakira.rsps.client.desktop.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.client.desktop.world.raster.SceneTextureAssets;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class TextureArchiveAssetLoaderTest {

  @Test
  void loadsSceneTexturesFromTexturesArchive() {
    SceneTextureAssets assets = TextureArchiveAssetLoader.loadFromWorkingDirectory(Path.of("."));

    assertThat(assets.textures()).hasSize(50);
    assertThat(assets.texture(0)).isNotNull();
    assertThat(assets.texture(0).width()).isGreaterThan(0);
    assertThat(assets.texture(0).height()).isGreaterThan(0);
    assertThat(assets.textures()).anySatisfy(texture -> assertThat(texture).isNotNull());
  }

}
