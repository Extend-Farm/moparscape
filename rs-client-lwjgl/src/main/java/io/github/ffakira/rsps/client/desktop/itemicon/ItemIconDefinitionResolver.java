package io.github.ffakira.rsps.client.desktop.itemicon;

import io.github.ffakira.rsps.content.ItemDefinition;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;

final class ItemIconDefinitionResolver {

  private static final int MAX_RENDERABLE_VARIANT_STEPS = 4;

  private final ItemDefinitionCatalog itemDefinitions;

  ItemIconDefinitionResolver(ItemDefinitionCatalog itemDefinitions) {
    this.itemDefinitions = itemDefinitions;
  }

  ItemDefinition resolveRenderableDefinition(int itemId, int quantity) {
    ItemDefinition definition = itemDefinitions.find(itemId).orElse(null);
    if (definition == null) {
      return null;
    }
    for (int step = 0; step < MAX_RENDERABLE_VARIANT_STEPS; step++) {
      ItemDefinition nextDefinition = nextRenderableDefinition(definition, quantity);
      if (nextDefinition == null || nextDefinition.id() == definition.id()) {
        break;
      }
      definition = nextDefinition;
    }
    return hasInventoryModel(definition) ? definition : null;
  }

  boolean hasInventoryModel(ItemDefinition definition) {
    return definition.inventoryAppearance().modelId() >= 0;
  }

  private ItemDefinition nextRenderableDefinition(ItemDefinition definition, int quantity) {
    ItemDefinition stackVariant = stackVariant(definition, quantity);
    if (stackVariant != null && stackVariant.id() != definition.id()) {
      return stackVariant;
    }
    return null;
  }

  private ItemDefinition stackVariant(ItemDefinition definition, int quantity) {
    if (quantity <= 1) {
      return null;
    }
    ItemDefinition.StackVariant selectedVariant = null;
    for (ItemDefinition.StackVariant stackVariant : definition.stackVariants()) {
      if (quantity >= stackVariant.minimumQuantity()
          && (selectedVariant == null || stackVariant.minimumQuantity() >= selectedVariant.minimumQuantity())) {
        selectedVariant = stackVariant;
      }
    }
    if (selectedVariant == null) {
      return null;
    }
    return itemDefinitions.find(selectedVariant.itemId()).orElse(null);
  }
}
