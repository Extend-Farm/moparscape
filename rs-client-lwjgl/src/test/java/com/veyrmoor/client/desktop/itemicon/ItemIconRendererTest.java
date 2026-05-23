package com.veyrmoor.client.desktop.itemicon;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.cache.RawModelRepository;
import com.veyrmoor.client.desktop.assets.image.ArgbImage;
import com.veyrmoor.client.desktop.assets.texture.TextureArchiveAssetLoader;
import com.veyrmoor.client.desktop.world.raster.SceneTextureAssets;
import com.veyrmoor.content.ContentBootstrapService;
import com.veyrmoor.content.ItemDefinition;
import com.veyrmoor.content.ItemDefinitionCatalog;
import java.nio.file.Path;
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

    ArgbImage fireRuneIcon = itemIconRenderer.render(554, 1);
    ArgbImage waterRuneIcon = itemIconRenderer.render(555, 1);
    ArgbImage lawRuneIcon = itemIconRenderer.render(563, 1);
    ArgbImage whitePartyhatIcon = itemIconRenderer.render(1048, 1);

    assertThat(fireRuneIcon).isNotNull();
    assertThat(waterRuneIcon).isNotNull();
    assertThat(lawRuneIcon).isNotNull();
    assertThat(whitePartyhatIcon).isNotNull();
    assertThat(opaquePixelCount(fireRuneIcon)).isGreaterThan(20);
    assertThat(redDominantPixelCount(fireRuneIcon)).isGreaterThan(20);
    assertThat(opaquePixelCount(waterRuneIcon)).isGreaterThan(20);
    assertThat(blueDominantPixelCount(waterRuneIcon)).isGreaterThan(12);
    assertThat(blueDominantPixelCount(lawRuneIcon)).isGreaterThan(20);
    assertThat(opaquePixelCount(whitePartyhatIcon)).isGreaterThan(80);
    assertThat(opaqueBoundsWidth(whitePartyhatIcon)).isGreaterThan(12);
    assertThat(opaqueBoundsHeight(whitePartyhatIcon)).isGreaterThan(12);
  }

  @Test
  void notedItemsCompositeTheNoteTemplateWithTheirLinkedItemIcon() {
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
    assertThat(itemIconRenderer.iconKey(7, 1)).isNotEqualTo(itemIconRenderer.iconKey(6, 1));
    assertThat(noteItemIcon.pixels()).isNotEqualTo(baseItemIcon.pixels());
    assertThat(opaquePixelCount(noteItemIcon)).isGreaterThan(opaquePixelCount(baseItemIcon));
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
  void combatWidgetPreviewsApplyTheSendWeaponZoomContract() {
    var manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    SceneTextureAssets sceneTextureAssets = TextureArchiveAssetLoader.loadFromWorkingDirectory(Path.of("."));
    ItemIconRenderer itemIconRenderer = new ItemIconRenderer(
        ItemDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore()),
        sceneTextureAssets
    );

    ArgbImage inventoryIcon = itemIconRenderer.render(1265, 1);
    ArgbImage combatWidgetPreview = itemIconRenderer.renderWidgetPreview(1265, 200);
    OpaqueBounds inventoryBounds = opaqueBounds(inventoryIcon);
    OpaqueBounds previewBounds = opaqueBounds(combatWidgetPreview);

    assertThat(inventoryIcon).isNotNull();
    assertThat(combatWidgetPreview).isNotNull();
    assertThat(opaquePixelCount(combatWidgetPreview)).isGreaterThan(20);
    assertThat(previewBounds.width()).isLessThan(inventoryBounds.width());
    assertThat(previewBounds.height()).isLessThan(inventoryBounds.height());
  }

  @Test
  void rendersTexturedItemIconsEvenWithoutNativeSceneTextureSampling() {
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

      assertThat(texturedIcon).withFailMessage("Expected textured icon for item %s", itemId).isNotNull();
      assertThat(fallbackIcon).withFailMessage("Expected fallback icon for item %s", itemId).isNotNull();
      assertThat(opaquePixelCount(texturedIcon))
          .withFailMessage("Expected visible textured icon pixels for item %s", itemId)
          .isGreaterThan(12);
      assertThat(opaquePixelCount(fallbackIcon))
          .withFailMessage("Expected visible fallback icon pixels for item %s", itemId)
          .isGreaterThan(12);
      texturedSampleItemId = itemId;
      break;
    }
    assertThat(texturedSampleItemId).isGreaterThanOrEqualTo(0);
  }

  @Test
  void chaosRuneMatchesLegacySpriteFootprint() {
    var manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    SceneTextureAssets sceneTextureAssets = TextureArchiveAssetLoader.loadFromWorkingDirectory(Path.of("."));
    ItemIconRenderer itemIconRenderer = new ItemIconRenderer(
        ItemDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore()),
        sceneTextureAssets
    );

    ArgbImage chaosRuneIcon = itemIconRenderer.render(562, 1);
    OpaqueBounds bounds = opaqueBounds(chaosRuneIcon);

    assertThat(chaosRuneIcon).isNotNull();
    assertThat(bounds.minX()).withFailMessage("Expected minX 1 but got bounds %s", bounds).isEqualTo(1);
    assertThat(bounds.minY()).withFailMessage("Expected minY 2 but got bounds %s", bounds).isEqualTo(2);
    assertThat(bounds.maxX()).withFailMessage("Expected maxX 30 but got bounds %s", bounds).isEqualTo(30);
    assertThat(bounds.maxY()).withFailMessage("Expected maxY 30 but got bounds %s", bounds).isEqualTo(30);
    assertThat(bounds.width()).withFailMessage("Expected width 30 but got bounds %s", bounds).isEqualTo(30);
    assertThat(bounds.height()).withFailMessage("Expected height 29 but got bounds %s", bounds).isEqualTo(29);
  }

  @Test
  void clipsInventoryTrianglesAgainstTheNearPlaneInsteadOfDroppingThem() {
    List<ClippedVertex> clippedVertices = ItemIconRasterizer.clipTriangleToNearPlane(
        new ClippedVertex(-8.0f, -4.0f, 32.0f, 0x112233, 0.0f, 0.0f, false),
        new ClippedVertex(8.0f, -4.0f, 32.0f, 0x445566, 1.0f, 0.0f, false),
        new ClippedVertex(0.0f, 12.0f, 0.25f, 0x778899, 0.5f, 1.0f, false)
    );

    assertThat(clippedVertices).hasSize(4);
    assertThat(clippedVertices).allSatisfy(vertex -> assertThat(vertex.depth()).isGreaterThanOrEqualTo(1.0f));
    assertThat(clippedVertices.stream().filter(vertex -> Math.abs(vertex.depth() - 1.0f) < 0.0001f).count()).isEqualTo(2);
  }

  @Test
  void projectedFacesUseLegacyIntegerScreenProjection() {
    ProjectedFace projectedFace = ItemIconRasterizer.projectedFace(
        new ClippedVertex(-8.0f, -8.0f, 32.0f, 0xffffff, 0.0f, 0.0f, false),
        new ClippedVertex(0.0f, 8.0f, 32.0f, 0xffffff, 0.5f, 1.0f, false),
        new ClippedVertex(8.0f, -8.0f, 32.0f, 0xffffff, 1.0f, 0.0f, false),
        255,
        null,
        0,
        32.0f,
        0
    );

    assertThat(projectedFace).isNotNull();
    assertThat(projectedFace.ax()).isEqualTo(-112.0f);
    assertThat(projectedFace.ay()).isEqualTo(-112.0f);
    assertThat(projectedFace.bx()).isEqualTo(16.0f);
    assertThat(projectedFace.by()).isEqualTo(144.0f);
    assertThat(projectedFace.cx()).isEqualTo(144.0f);
    assertThat(projectedFace.cy()).isEqualTo(-112.0f);
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
    return opaqueBounds(image).width();
  }

  private int opaqueBoundsHeight(ArgbImage image) {
    return opaqueBounds(image).height();
  }

  private OpaqueBounds opaqueBounds(ArgbImage image) {
    int minY = image.height();
    int maxY = -1;
    int minX = image.width();
    int maxX = -1;
    for (int index = 0; index < image.pixels().length; index++) {
      if ((image.pixels()[index] >>> 24) == 0) {
        continue;
      }
      int x = index % image.width();
      int y = index / image.width();
      minX = Math.min(minX, x);
      maxX = Math.max(maxX, x);
      minY = Math.min(minY, y);
      maxY = Math.max(maxY, y);
    }
    if (maxX < minX || maxY < minY) {
      return new OpaqueBounds(0, 0, 0, 0);
    }
    return new OpaqueBounds(minX, minY, maxX, maxY);
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

  private int redDominantPixelCount(ArgbImage image) {
    int redDominantPixelCount = 0;
    for (int pixel : image.pixels()) {
      if ((pixel >>> 24) == 0) {
        continue;
      }
      int red = (pixel >>> 16) & 0xff;
      int green = (pixel >>> 8) & 0xff;
      int blue = pixel & 0xff;
      if (red > green + 20 && red > blue + 20) {
        redDominantPixelCount++;
      }
    }
    return redDominantPixelCount;
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

  private record OpaqueBounds(int minX, int minY, int maxX, int maxY) {

    private int width() {
      return maxX - minX + 1;
    }

    private int height() {
      return maxY - minY + 1;
    }
  }
}
