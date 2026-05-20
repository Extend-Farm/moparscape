package io.github.ffakira.rsps.client.desktop.itemicon;

import io.github.ffakira.rsps.cache.RawModelRepository;
import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneTextureAssets;
import io.github.ffakira.rsps.content.ItemDefinition;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ItemIconRenderer {

  private static final int NOTE_OVERLAY_STACK_SIZE = 10;

  private final ItemDefinitionCatalog itemDefinitions;
  private final ItemIconDefinitionResolver definitionResolver;
  private final ItemIconModelProjector modelProjector;
  private final Map<Integer, ArgbImage> iconsByRenderableItemId = new HashMap<>();

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

  private ArgbImage renderResolvedItemIcon(int itemId) {
    List<ItemIconRasterizer.ProjectedFace> projectedFaces = modelProjector.project(itemId);
    if (projectedFaces.isEmpty()) {
      return null;
    }
    ArgbImage icon = ItemIconRasterizer.rasterize(projectedFaces);
    return compositeNoteOverlay(itemId, icon);
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
          : blend(compositedPixels[index], overlayArgb, overlayAlpha);
    }
    return new ArgbImage(baseIcon.width(), baseIcon.height(), compositedPixels);
  }

  private int blend(int destinationArgb, int sourceArgb, int sourceAlpha) {
    int inverseAlpha = 255 - sourceAlpha;
    int destinationRed = (destinationArgb >>> 16) & 0xff;
    int destinationGreen = (destinationArgb >>> 8) & 0xff;
    int destinationBlue = destinationArgb & 0xff;
    int sourceRed = (sourceArgb >>> 16) & 0xff;
    int sourceGreen = (sourceArgb >>> 8) & 0xff;
    int sourceBlue = sourceArgb & 0xff;
    int red = (sourceRed * sourceAlpha + destinationRed * inverseAlpha) / 255;
    int green = (sourceGreen * sourceAlpha + destinationGreen * inverseAlpha) / 255;
    int blue = (sourceBlue * sourceAlpha + destinationBlue * inverseAlpha) / 255;
    return 0xff000000 | (red << 16) | (green << 8) | blue;
  }
}
