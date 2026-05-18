package io.github.ffakira.rsps.client.desktop.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.cache.RawModelRepository;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneTextureAssets;
import io.github.ffakira.rsps.content.ContentBootstrapService;
import io.github.ffakira.rsps.content.ItemDefinition;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class ItemIconRendererTest {

  @Test
  void rendersLiveCacheItemIconsWithOpaquePixels() {
    var manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    SceneTextureAssets sceneTextureAssets = TextureArchiveAssetLoader.loadFromWorkingDirectory(Path.of("."));
    ItemIconRenderer itemIconRenderer = new ItemIconRenderer(
        ItemDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore()),
        sceneTextureAssets
    );

    ArgbImage waterRuneIcon = itemIconRenderer.render(555, 1);
    ArgbImage whitePartyhatIcon = itemIconRenderer.render(1048, 1);

    assertThat(waterRuneIcon).isNotNull();
    assertThat(whitePartyhatIcon).isNotNull();
    assertThat(opaquePixelCount(waterRuneIcon)).isGreaterThan(20);
    assertThat(blueDominantPixelCount(waterRuneIcon)).isGreaterThan(12);
    assertThat(opaquePixelCount(whitePartyhatIcon)).isGreaterThan(80);
    assertThat(opaqueBoundsWidth(whitePartyhatIcon)).isGreaterThan(12);
    assertThat(opaqueBoundsHeight(whitePartyhatIcon)).isGreaterThan(12);
  }

  @Test
  void notedItemsReuseTheirLinkedItemIconUntilNativeNoteOverlayExists() {
    var manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    SceneTextureAssets sceneTextureAssets = TextureArchiveAssetLoader.loadFromWorkingDirectory(Path.of("."));
    ItemIconRenderer itemIconRenderer = new ItemIconRenderer(
        ItemDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore()),
        sceneTextureAssets
    );

    ArgbImage baseItemIcon = itemIconRenderer.render(6, 1);
    ArgbImage noteItemIcon = itemIconRenderer.render(7, 1);

    assertThat(baseItemIcon).isNotNull();
    assertThat(noteItemIcon).isNotNull();
    assertThat(noteItemIcon.pixels()).containsExactly(baseItemIcon.pixels());
  }

  @Test
  void largeStacksUseAlternateCacheItemIconsWhenDefinitionsProvideThem() {
    var manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    SceneTextureAssets sceneTextureAssets = TextureArchiveAssetLoader.loadFromWorkingDirectory(Path.of("."));
    ItemIconRenderer itemIconRenderer = new ItemIconRenderer(
        ItemDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore()),
        sceneTextureAssets
    );

    int singleCoinIconKey = itemIconRenderer.iconKey(995, 1);
    int largeCoinIconKey = itemIconRenderer.iconKey(995, 10_000_000);

    assertThat(singleCoinIconKey).isPositive();
    assertThat(largeCoinIconKey).isPositive();
    assertThat(largeCoinIconKey).isNotEqualTo(singleCoinIconKey);
    assertThat(itemIconRenderer.render(995, 10_000_000)).isNotNull();
  }

  @Test
  void sampledInventoryIconsRenderVisibleContent() {
    var manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    SceneTextureAssets sceneTextureAssets = TextureArchiveAssetLoader.loadFromWorkingDirectory(Path.of("."));
    ItemIconRenderer itemIconRenderer = new ItemIconRenderer(
        ItemDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore()),
        sceneTextureAssets
    );

    int[] sampledItemIds = {554, 555, 557, 561, 563, 1048};
    for (int itemId : sampledItemIds) {
      ArgbImage icon = itemIconRenderer.render(itemId, 1);

      assertThat(icon).withFailMessage("Expected icon for item %s", itemId).isNotNull();
      assertThat(opaquePixelCount(icon)).withFailMessage("Expected visible pixels for item %s", itemId).isGreaterThan(12);
      assertThat(opaqueBoundsWidth(icon)).withFailMessage("Expected width for item %s", itemId).isGreaterThan(8);
      assertThat(opaqueBoundsHeight(icon)).withFailMessage("Expected height for item %s", itemId).isGreaterThan(8);
    }
  }

  @Test
  void texturedItemIconsUseSceneTextureSamplingWhenTheModelReferencesCacheTextures() {
    var manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    ItemDefinitionCatalog itemDefinitionCatalog = ItemDefinitionCatalog.load(manifest);
    RawModelRepository rawModelRepository = new RawModelRepository(manifest.cacheStore());
    SceneTextureAssets sceneTextureAssets = TextureArchiveAssetLoader.loadFromWorkingDirectory(Path.of("."));
    ItemIconRenderer texturedRenderer = new ItemIconRenderer(itemDefinitionCatalog, rawModelRepository, sceneTextureAssets);
    ItemIconRenderer fallbackRenderer = new ItemIconRenderer(itemDefinitionCatalog, rawModelRepository);

    int texturedSampleItemId = -1;
    for (int itemId = 0; itemId < itemDefinitionCatalog.size(); itemId++) {
      ItemDefinition definition = itemDefinitionCatalog.find(itemId).orElse(null);
      if (definition == null || !hasResolvableTexturedFace(definition, rawModelRepository, sceneTextureAssets)) {
        continue;
      }
      ArgbImage texturedIcon = texturedRenderer.render(itemId, 1);
      ArgbImage fallbackIcon = fallbackRenderer.render(itemId, 1);
      if (texturedIcon == null || fallbackIcon == null) {
        continue;
      }
      if (Arrays.equals(texturedIcon.pixels(), fallbackIcon.pixels())) {
        continue;
      }

      assertThat(texturedIcon).withFailMessage("Expected textured icon for item %s", itemId).isNotNull();
      assertThat(fallbackIcon).withFailMessage("Expected fallback icon for item %s", itemId).isNotNull();
      assertThat(Arrays.equals(texturedIcon.pixels(), fallbackIcon.pixels()))
          .withFailMessage("Expected scene texture sampling to change textured item %s", itemId)
          .isFalse();
      texturedSampleItemId = itemId;
      break;
    }
    assertThat(texturedSampleItemId).isGreaterThanOrEqualTo(0);
  }

  @Test
  void clipsInventoryTrianglesAgainstTheNearPlaneInsteadOfDroppingThem() {
    List<ItemIconRenderer.ClippedVertex> clippedVertices = ItemIconRenderer.clipTriangleToNearPlane(
        new ItemIconRenderer.ClippedVertex(-8.0f, -4.0f, 32.0f, 0x112233, 0.0f, 0.0f),
        new ItemIconRenderer.ClippedVertex(8.0f, -4.0f, 32.0f, 0x445566, 1.0f, 0.0f),
        new ItemIconRenderer.ClippedVertex(0.0f, 12.0f, 0.25f, 0x778899, 0.5f, 1.0f)
    );

    assertThat(clippedVertices).hasSize(4);
    assertThat(clippedVertices).allSatisfy(vertex -> assertThat(vertex.depth()).isGreaterThanOrEqualTo(1.0f));
    assertThat(clippedVertices.stream().filter(vertex -> Math.abs(vertex.depth() - 1.0f) < 0.0001f).count()).isEqualTo(2);
  }

  private int opaquePixelCount(ArgbImage image) {
    int opaquePixelCount = 0;
    for (int pixel : image.pixels()) {
      if ((pixel >>> 24) != 0) {
        opaquePixelCount++;
      }
    }
    return opaquePixelCount;
  }

  private int opaqueBoundsWidth(ArgbImage image) {
    int minX = image.width();
    int maxX = -1;
    for (int index = 0; index < image.pixels().length; index++) {
      if ((image.pixels()[index] >>> 24) == 0) {
        continue;
      }
      int x = index % image.width();
      minX = Math.min(minX, x);
      maxX = Math.max(maxX, x);
    }
    return maxX < minX ? 0 : maxX - minX + 1;
  }

  private int opaqueBoundsHeight(ArgbImage image) {
    int minY = image.height();
    int maxY = -1;
    for (int index = 0; index < image.pixels().length; index++) {
      if ((image.pixels()[index] >>> 24) == 0) {
        continue;
      }
      int y = index / image.width();
      minY = Math.min(minY, y);
      maxY = Math.max(maxY, y);
    }
    return maxY < minY ? 0 : maxY - minY + 1;
  }

  private int blueDominantPixelCount(ArgbImage image) {
    int blueDominantPixelCount = 0;
    for (int pixel : image.pixels()) {
      if ((pixel >>> 24) == 0) {
        continue;
      }
      int red = (pixel >>> 16) & 0xff;
      int green = (pixel >>> 8) & 0xff;
      int blue = pixel & 0xff;
      if (blue > red + 24 && blue > green + 8) {
        blueDominantPixelCount++;
      }
    }
    return blueDominantPixelCount;
  }

  private boolean hasResolvableTexturedFace(
      ItemDefinition definition,
      RawModelRepository rawModelRepository,
      SceneTextureAssets sceneTextureAssets
  ) {
    if (definition.inventoryAppearance().modelId() < 0) {
      return false;
    }
    var rawModelData = rawModelRepository.loadModel(definition.inventoryAppearance().modelId());
    if (rawModelData == null) {
      return false;
    }
    for (int faceIndex = 0; faceIndex < rawModelData.faceCount(); faceIndex++) {
      int faceMode = rawModelData.faceRenderTypes()[faceIndex] & 3;
      if (faceMode < 2) {
        continue;
      }
      int textureId = rawModelData.faceColorHsl()[faceIndex];
      int textureIndex = rawModelData.faceRenderTypes()[faceIndex] >> 2;
      if (textureIndex >= 0
          && textureIndex < rawModelData.texturedFaceCount()
          && sceneTextureAssets.texture(textureId) != null) {
        return true;
      }
    }
    return false;
  }
}
