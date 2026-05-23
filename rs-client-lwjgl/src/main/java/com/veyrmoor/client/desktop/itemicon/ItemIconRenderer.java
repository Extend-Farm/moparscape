package com.veyrmoor.client.desktop.itemicon;

import com.veyrmoor.cache.RawModelRepository;
import com.veyrmoor.client.desktop.assets.image.ArgbImage;
import com.veyrmoor.client.desktop.world.raster.SceneTextureAssets;
import com.veyrmoor.content.ItemDefinition;
import com.veyrmoor.content.ItemDefinitionCatalog;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ItemIconRenderer {

  private static final int NOTE_OVERLAY_STACK_SIZE = 10;

  private final ItemDefinitionCatalog itemDefinitions;
  private final ItemIconDefinitionResolver definitionResolver;
  private final ItemIconModelProjector modelProjector;
  private final Map<Integer, ArgbImage> iconsByRenderableItemId = new HashMap<>();
  private final Map<WidgetPreviewKey, ArgbImage> widgetPreviewsByRenderableItemId = new HashMap<>();

  public ItemIconRenderer(ItemDefinitionCatalog itemDefinitions, RawModelRepository rawModelRepository) {
    this(itemDefinitions, rawModelRepository, null);
  }

  public ItemIconRenderer(
      ItemDefinitionCatalog itemDefinitions,
      RawModelRepository rawModelRepository,
      SceneTextureAssets sceneTextureAssets
  ) {
    this.itemDefinitions = itemDefinitions;
    definitionResolver = new ItemIconDefinitionResolver(itemDefinitions);
    modelProjector = new ItemIconModelProjector(itemDefinitions, rawModelRepository, sceneTextureAssets);
  }

  public int iconKey(int itemId, int quantity) {
    ItemDefinition renderableDefinition = definitionResolver.resolveRenderableDefinition(itemId, quantity);
    return renderableDefinition == null ? -1 : renderableDefinition.id();
  }

  public ArgbImage render(int itemId, int quantity) {
    ItemDefinition renderableDefinition = definitionResolver.resolveRenderableDefinition(itemId, quantity);
    if (renderableDefinition == null) {
      return null;
    }
    return iconsByRenderableItemId.computeIfAbsent(renderableDefinition.id(), this::renderResolvedItemIcon);
  }

  public ArgbImage renderWidgetPreview(int itemId, int zoomScale) {
    ItemDefinition renderableDefinition = definitionResolver.resolveRenderableDefinition(itemId, 1);
    if (renderableDefinition == null || zoomScale <= 0) {
      return null;
    }
    return widgetPreviewsByRenderableItemId.computeIfAbsent(
        new WidgetPreviewKey(renderableDefinition.id(), zoomScale),
        key -> renderResolvedWidgetPreview(key.itemId(), key.zoomScale())
    );
  }

  private ArgbImage renderResolvedItemIcon(int itemId) {
    List<ProjectedFace> projectedFaces = modelProjector.project(itemId);
    if (projectedFaces.isEmpty()) {
      return null;
    }
    ArgbImage icon = ItemIconRasterizer.rasterize(projectedFaces);
    return compositeNoteOverlay(itemId, icon);
  }

  private ArgbImage renderResolvedWidgetPreview(int itemId, int zoomScale) {
    List<ProjectedFace> projectedFaces = modelProjector.projectWidgetPreview(itemId, zoomScale);
    if (projectedFaces.isEmpty()) {
      return null;
    }
    return scaleIntoCanvas(ItemIconRasterizer.rasterize(projectedFaces), 100.0f / zoomScale);
  }

  private ArgbImage compositeNoteOverlay(int itemId, ArgbImage baseIcon) {
    ItemDefinition definition = itemDefinitions.find(itemId).orElse(null);
    if (definition == null || !definition.noted() || definition.noteLinkItemId() < 0) {
      return baseIcon;
    }
    if (definition.noteLinkItemId() == itemId) {
      return baseIcon;
    }
    ArgbImage overlayIcon = render(definition.noteLinkItemId(), NOTE_OVERLAY_STACK_SIZE);
    if (overlayIcon == null) {
      return baseIcon;
    }
    return overlay(baseIcon, overlayIcon);
  }

  private ArgbImage overlay(ArgbImage baseIcon, ArgbImage overlayIcon) {
    if (baseIcon.width() != overlayIcon.width() || baseIcon.height() != overlayIcon.height()) {
      return baseIcon;
    }
    int[] compositedPixels = baseIcon.pixels().clone();
    int[] overlayPixels = overlayIcon.pixels();
    for (int index = 0; index < overlayPixels.length; index++) {
      int overlayArgb = overlayPixels[index];
      int overlayAlpha = (overlayArgb >>> 24) & 0xff;
      if (overlayAlpha <= 0) {
        continue;
      }
      compositedPixels[index] = overlayAlpha >= 255
          ? overlayArgb
          : PixelBlender.blend(compositedPixels[index], overlayArgb, overlayAlpha);
    }
    return new ArgbImage(baseIcon.width(), baseIcon.height(), compositedPixels);
  }

  private ArgbImage scaleIntoCanvas(ArgbImage sourceImage, float scale) {
    if (sourceImage == null || scale >= 0.999f || scale <= 0.0f) {
      return sourceImage;
    }
    int[] scaledPixels = new int[sourceImage.width() * sourceImage.height()];
    float centerX = sourceImage.width() * 0.5f;
    float centerY = sourceImage.height() * 0.5f;
    for (int targetY = 0; targetY < sourceImage.height(); targetY++) {
      float sourceY = centerY + (targetY - centerY) / scale;
      int sourcePixelY = Math.round(sourceY);
      if (sourcePixelY < 0 || sourcePixelY >= sourceImage.height()) {
        continue;
      }
      int targetRowOffset = targetY * sourceImage.width();
      int sourceRowOffset = sourcePixelY * sourceImage.width();
      for (int targetX = 0; targetX < sourceImage.width(); targetX++) {
        float sourceX = centerX + (targetX - centerX) / scale;
        int sourcePixelX = Math.round(sourceX);
        if (sourcePixelX < 0 || sourcePixelX >= sourceImage.width()) {
          continue;
        }
        scaledPixels[targetRowOffset + targetX] = sourceImage.pixels()[sourceRowOffset + sourcePixelX];
      }
    }
    return new ArgbImage(sourceImage.width(), sourceImage.height(), scaledPixels);
  }

  private record WidgetPreviewKey(int itemId, int zoomScale) {
  }
}
