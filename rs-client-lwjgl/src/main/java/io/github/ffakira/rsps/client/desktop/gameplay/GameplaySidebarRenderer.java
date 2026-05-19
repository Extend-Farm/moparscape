package io.github.ffakira.rsps.client.desktop.gameplay;

import io.github.ffakira.rsps.client.core.BootstrapEquipmentItemPresentation;
import io.github.ffakira.rsps.client.core.BootstrapInventoryItemPresentation;
import io.github.ffakira.rsps.client.core.BootstrapSkillPresentation;
import io.github.ffakira.rsps.client.core.ClientViewModel;
import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.core.ImmediateModeRenderer2d;
import io.github.ffakira.rsps.client.desktop.core.ItemIconRenderer;
import io.github.ffakira.rsps.client.desktop.core.OpenGlTexture;
import io.github.ffakira.rsps.client.desktop.core.ScreenRect;
import io.github.ffakira.rsps.client.desktop.login.TitleScreenBitmapFont;
import io.github.ffakira.rsps.content.ItemDefinition;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import io.github.ffakira.rsps.protocol.BootstrapItemSlot;
import io.github.ffakira.rsps.protocol.BootstrapSkill;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

final class GameplaySidebarRenderer implements AutoCloseable {

  private static final int INVENTORY_COLUMNS = 4;
  private static final int INVENTORY_ROWS = 7;
  private static final float INVENTORY_SLOT_STEP_X = 42.0f;
  private static final float INVENTORY_SLOT_STEP_Y = 36.0f;
  private static final float INVENTORY_SLOT_LEFT = 12.0f;
  private static final float INVENTORY_SLOT_TOP = 11.0f;
  private static final float INVENTORY_ICON_SIZE = 32.0f;
  private static final int INVENTORY_STACK_TEXT_RGB = 0xffff00;

  private static final String[] SKILL_NAMES = {
      "Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer", "Magic",
      "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting",
      "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer", "Farming",
      "Runecraft", "Unused", "Unused", "Unused", "Unused"
  };
  private static final String[] EQUIPMENT_SLOT_NAMES = {
      "Head", "Cape", "Neck", "Weapon", "Body", "Shield", "Arms",
      "Legs", "Hair", "Hands", "Feet", "Ring", "Ammo", "Aura"
  };

  private final ItemDefinitionCatalog itemDefinitionCatalog;
  private final ItemIconRenderer itemIconRenderer;
  private final TitleScreenBitmapFont inventoryAmountFont;
  private final ImmediateModeRenderer2d primitives;
  private final Map<Integer, OpenGlTexture> itemIconTextures = new HashMap<>();
  private final Map<String, OpenGlTexture> inventoryAmountTextures = new HashMap<>();

  GameplaySidebarRenderer(
      ItemDefinitionCatalog itemDefinitionCatalog,
      ItemIconRenderer itemIconRenderer,
      TitleScreenBitmapFont inventoryAmountFont,
      ImmediateModeRenderer2d primitives
  ) {
    this.itemDefinitionCatalog = itemDefinitionCatalog;
    this.itemIconRenderer = itemIconRenderer;
    this.inventoryAmountFont = inventoryAmountFont;
    this.primitives = primitives;
  }

  void drawSidebar(ClientViewModel viewModel, GameplayTab activeGameplayTab) {
    // Sidebar content is intentionally honest about parity:
    // resolved bootstrap/item data is shown where native state exists, and explicit placeholders
    // remain where widget-driven interfaces have not been ported yet.
    ScreenRect rect = GameplayLayout.sidebarPanelRect();
    float left = rect.left() + 12.0f;
    float top = rect.top() + 22.0f;
    if (activeGameplayTab != GameplayTab.INVENTORY) {
      primitives.drawText(left, top, activeGameplayTab.label(), 0.92f, 0.86f, 0.46f);
    }
    switch (activeGameplayTab) {
      case INVENTORY -> drawInventorySidebar(viewModel, rect);
      case EQUIPMENT -> drawEquipmentSidebar(viewModel, left, top + 16.0f);
      case STATS -> drawStatsSidebar(viewModel, left, top + 16.0f);
      case COMBAT -> drawCombatSidebar(viewModel, left, top + 16.0f);
      case QUESTS -> drawSidebarPlaceholder(left, top + 16.0f, "Quest journal not synced yet.", "Native widget rendering is next.");
      case PRAYER -> drawSidebarPlaceholder(left, top + 16.0f, "Prayer book not synced yet.", "Skill state is available through Stats.");
      case MAGIC -> drawSidebarPlaceholder(left, top + 16.0f, "Spellbook not decoded yet.", "Runes are available in inventory.");
      case FRIENDS -> drawSidebarPlaceholder(left, top + 16.0f, "Friends list not synced yet.", "Social state will move into protocol snapshots.");
      case IGNORES -> drawSidebarPlaceholder(left, top + 16.0f, "Ignore list not synced yet.", "Legacy file import already stores social links.");
      case LOGOUT -> drawSidebarPlaceholder(left, top + 16.0f, "Logout flow is not wired to a tab yet.", "Use Esc to close the client.");
      case SETTINGS -> drawSidebarPlaceholder(left, top + 16.0f, "Settings panel not implemented yet.", "Run energy and rights are in chat status.");
      case EMOTES -> drawSidebarPlaceholder(left, top + 16.0f, "Emote book not decoded yet.", "Walking poses are live; emote sequences are still pending.");
      case MUSIC -> drawSidebarPlaceholder(left, top + 16.0f, "Music player not decoded yet.", "Audio is outside the current world slice.");
    }
  }

  void drawChatbox(ClientViewModel viewModel, GameplayTab activeGameplayTab) {
    ScreenRect rect = GameplayLayout.chatboxPanelRect();
    float left = rect.left() + 10.0f;
    float top = rect.top() + 22.0f;
    primitives.drawText(left, top, "Welcome to MoparScape.", 0.92f, 0.86f, 0.46f);
    primitives.drawText(left, top + 16.0f, viewModel.statusText(), 0.30f, 0.85f, 0.35f);
    if (viewModel.bootstrap() != null && viewModel.localPlayerPosition() != null) {
      primitives.drawText(
          left,
          top + 32.0f,
          viewModel.bootstrap().displayName() + ": region "
              + Objects.toString(viewModel.regionKey(), "unknown")
              + "  x:" + viewModel.localPlayerPosition().x()
              + " y:" + viewModel.localPlayerPosition().y()
              + " z:" + viewModel.localPlayerPosition().plane(),
          0.95f,
          0.96f,
          0.98f
      );
      primitives.drawText(
          left,
          top + 48.0f,
          "Rights " + viewModel.bootstrap().profile().rights()
              + "  Energy " + viewModel.bootstrap().profile().runEnergy()
              + "  Active tab " + activeGameplayTab.label(),
          0.84f,
          0.88f,
          0.94f
      );
    }
    primitives.drawText(left, top + 64.0f, "Click to walk. Arrow keys rotate and tilt the camera.", 0.84f, 0.88f, 0.94f);
    drawChatOptionsBar();
  }

  @Override
  public void close() {
    for (OpenGlTexture itemIconTexture : itemIconTextures.values()) {
      closeTexture(itemIconTexture);
    }
    for (OpenGlTexture inventoryAmountTexture : inventoryAmountTextures.values()) {
      closeTexture(inventoryAmountTexture);
    }
  }

  private void drawInventorySidebar(ClientViewModel viewModel, ScreenRect sidebarRect) {
    if (itemIconRenderer == null) {
      drawInventoryTextFallback(viewModel, sidebarRect.left() + 12.0f, sidebarRect.top() + 38.0f);
      return;
    }
    List<BootstrapInventoryItemPresentation> inventory = viewModel.inventoryPresentation();
    if (inventory.isEmpty()) {
      drawInventoryTextFallback(viewModel, sidebarRect.left() + 12.0f, sidebarRect.top() + 38.0f);
      return;
    }
    for (BootstrapInventoryItemPresentation item : inventory) {
      if (item.slotIndex() < 0 || item.slotIndex() >= INVENTORY_COLUMNS * INVENTORY_ROWS) {
        continue;
      }
      ScreenRect slotRect = inventorySlotRect(sidebarRect, item.slotIndex());
      OpenGlTexture itemTexture = itemIconTexture(item.itemId(), item.quantity());
      if (itemTexture != null) {
        primitives.drawTexturedQuad(itemTexture, slotRect);
      }
      if (showsStackAmount(item)) {
        drawInventoryStackAmount(slotRect, formatQuantity(item.quantity()));
      }
    }
  }

  private void drawInventoryTextFallback(ClientViewModel viewModel, float left, float top) {
    primitives.drawText(left, top - 16.0f, GameplayTab.INVENTORY.label(), 0.92f, 0.86f, 0.46f);
    List<BootstrapInventoryItemPresentation> inventory = viewModel.inventoryPresentation();
    if (!inventory.isEmpty()) {
      int lineCount = Math.min(10, inventory.size());
      for (int index = 0; index < lineCount; index++) {
        BootstrapInventoryItemPresentation item = inventory.get(index);
        primitives.drawText(
            left,
            top + index * 12.0f,
            truncate(item.name(), 18) + " x" + formatQuantity(item.quantity()),
            0.95f,
            0.96f,
            0.98f
        );
      }
      if (inventory.size() > lineCount) {
        primitives.drawText(left, top + lineCount * 12.0f, "+" + (inventory.size() - lineCount) + " more items", 0.70f, 0.74f, 0.82f);
      }
      return;
    }
    drawResolvedItemList(left, top, viewModel.inventory(), 10);
  }

  private void drawEquipmentSidebar(ClientViewModel viewModel, float left, float top) {
    List<BootstrapEquipmentItemPresentation> equipment = viewModel.equipmentPresentation();
    if (!equipment.isEmpty()) {
      int row = 0;
      for (BootstrapEquipmentItemPresentation item : equipment) {
        int column = row % 2;
        int currentRow = row / 2;
        float columnLeft = left + column * 82.0f;
        float rowTop = top + currentRow * 26.0f;
        primitives.drawText(columnLeft, rowTop, truncate(item.slotName(), 8), 0.84f, 0.88f, 0.94f);
        primitives.drawText(columnLeft, rowTop + 12.0f, truncate(item.name(), 11), 0.95f, 0.96f, 0.98f);
        row++;
        if (row >= 10) {
          break;
        }
      }
      return;
    }
    drawEquipmentList(left, top, viewModel.equipment(), 2);
  }

  private void drawStatsSidebar(ClientViewModel viewModel, float left, float top) {
    List<BootstrapSkillPresentation> skills = viewModel.skillPresentation();
    if (!skills.isEmpty()) {
      int lineCount = Math.min(15, skills.size());
      for (int index = 0; index < lineCount; index++) {
        BootstrapSkillPresentation skill = skills.get(index);
        primitives.drawText(left, top + index * 12.0f, truncate(skill.name(), 13) + " " + skill.currentLevel(), 0.95f, 0.96f, 0.98f);
      }
      return;
    }
    drawSkillGrid(left, top, viewModel);
  }

  private void drawCombatSidebar(ClientViewModel viewModel, float left, float top) {
    drawSidebarPlaceholder(
        left,
        top,
        "Combat style UI is not decoded yet.",
        "Top skill: " + highestSkillLabel(viewModel),
        "Equipment stacks: " + viewModel.equipment().size(),
        "Inventory stacks: " + viewModel.inventory().size()
    );
  }

  private void drawSidebarPlaceholder(float left, float top, String... lines) {
    for (int index = 0; index < lines.length; index++) {
      primitives.drawText(left, top + index * 14.0f, lines[index], 0.84f, 0.88f, 0.94f);
    }
  }

  private void drawChatOptionsBar() {
    primitives.drawText(10.0f, 479.0f, "Public chat", 0.95f, 0.96f, 0.98f);
    primitives.drawText(78.0f, 490.0f, "On", 0.30f, 0.85f, 0.35f);
    primitives.drawText(137.0f, 479.0f, "Private chat", 0.95f, 0.96f, 0.98f);
    primitives.drawText(209.0f, 490.0f, "On", 0.30f, 0.85f, 0.35f);
    primitives.drawText(266.0f, 479.0f, "Trade/compete", 0.95f, 0.96f, 0.98f);
    primitives.drawText(343.0f, 490.0f, "On", 0.30f, 0.85f, 0.35f);
    primitives.drawText(402.0f, 479.0f, "Report abuse", 0.95f, 0.96f, 0.98f);
  }

  private void drawResolvedItemList(float left, float top, List<BootstrapItemSlot> itemSlots, int maxLines) {
    int lineCount = Math.min(maxLines, itemSlots.size());
    for (int index = 0; index < lineCount; index++) {
      BootstrapItemSlot slot = itemSlots.get(index);
      primitives.drawText(
          left,
          top + index * 12.0f,
          truncate(resolveItemName(slot.itemId()), 18) + " x" + formatQuantity(slot.quantity()),
          0.95f,
          0.96f,
          0.98f
      );
    }
    if (itemSlots.size() > lineCount) {
      primitives.drawText(left, top + lineCount * 12.0f, "+" + (itemSlots.size() - lineCount) + " more items", 0.70f, 0.74f, 0.82f);
    }
  }

  private void drawEquipmentList(float left, float top, List<BootstrapItemSlot> equipment, int columns) {
    int rowCount = 0;
    for (BootstrapItemSlot slot : equipment) {
      if (rowCount >= EQUIPMENT_SLOT_NAMES.length) {
        break;
      }
      int column = rowCount % columns;
      int row = rowCount / columns;
      float columnLeft = left + column * 82.0f;
      float rowTop = top + row * 24.0f;
      int slotIndex = Math.min(slot.slotIndex(), EQUIPMENT_SLOT_NAMES.length - 1);
      primitives.drawText(columnLeft, rowTop, EQUIPMENT_SLOT_NAMES[slotIndex], 0.84f, 0.88f, 0.94f);
      primitives.drawText(columnLeft, rowTop + 12.0f, truncate(resolveItemName(slot.itemId()), 11), 0.95f, 0.96f, 0.98f);
      rowCount++;
    }
  }

  private void drawSkillGrid(float left, float top, ClientViewModel viewModel) {
    List<BootstrapSkill> skills = viewModel.skills().stream()
        .sorted(Comparator.comparingInt(BootstrapSkill::skillId))
        .toList();
    int columnCount = 3;
    int maxRows = 9;
    for (int index = 0; index < Math.min(skills.size(), columnCount * maxRows); index++) {
      BootstrapSkill skill = skills.get(index);
      int column = index / maxRows;
      int row = index % maxRows;
      float columnLeft = left + column * 160.0f;
      float rowTop = top + row * 12.0f;
      primitives.drawText(
          columnLeft,
          rowTop,
          truncate(skillName(skill.skillId()), 11) + " " + skill.currentLevel(),
          0.95f,
          0.96f,
          0.98f
      );
    }
  }

  private String highestSkillLabel(ClientViewModel viewModel) {
    return viewModel.skills().stream()
        .max(Comparator.comparingInt(BootstrapSkill::currentLevel))
        .map(skill -> skillName(skill.skillId()) + " " + skill.currentLevel())
        .orElse("none");
  }

  private String skillName(int skillId) {
    if (skillId < 0 || skillId >= SKILL_NAMES.length) {
      return "Skill " + skillId;
    }
    return SKILL_NAMES[skillId];
  }

  private String resolveItemName(int itemId) {
    if (itemDefinitionCatalog == null) {
      return "item-" + itemId;
    }
    return itemDefinitionCatalog.find(itemId)
        .map(ItemDefinition::name)
        .orElse("item-" + itemId);
  }

  private String formatQuantity(int quantity) {
    if (quantity >= 10_000_000) {
      return (quantity / 1_000_000) + "M";
    }
    if (quantity >= 100_000) {
      return (quantity / 1_000) + "K";
    }
    return Integer.toString(quantity);
  }

  private boolean showsStackAmount(BootstrapInventoryItemPresentation item) {
    return item.stackable() || item.noted() || item.quantity() != 1;
  }

  private String truncate(String value, int maxChars) {
    if (value.length() <= maxChars) {
      return value;
    }
    return value.substring(0, Math.max(0, maxChars - 1)) + ".";
  }

  private ScreenRect inventorySlotRect(ScreenRect sidebarRect, int slotIndex) {
    int column = slotIndex % INVENTORY_COLUMNS;
    int row = slotIndex / INVENTORY_COLUMNS;
    return new ScreenRect(
        sidebarRect.left() + INVENTORY_SLOT_LEFT + column * INVENTORY_SLOT_STEP_X,
        sidebarRect.top() + INVENTORY_SLOT_TOP + row * INVENTORY_SLOT_STEP_Y,
        INVENTORY_ICON_SIZE,
        INVENTORY_ICON_SIZE
    );
  }

  private OpenGlTexture itemIconTexture(int itemId, int quantity) {
    if (itemIconRenderer == null) {
      return null;
    }
    int iconKey = itemIconRenderer.iconKey(itemId, quantity);
    if (iconKey < 0) {
      return null;
    }
    OpenGlTexture cachedTexture = itemIconTextures.get(iconKey);
    if (cachedTexture != null) {
      return cachedTexture;
    }
    ArgbImage iconImage = itemIconRenderer.render(itemId, quantity);
    if (iconImage == null) {
      return null;
    }
    OpenGlTexture createdTexture = OpenGlTexture.fromArgbImage(iconImage);
    itemIconTextures.put(iconKey, createdTexture);
    return createdTexture;
  }

  private void drawShadowedText(float left, float top, String text, float red, float green, float blue) {
    drawShadowedText(left, top, text, red, green, blue, 1.0f);
  }

  private void drawShadowedText(float left, float top, String text, float red, float green, float blue, float scale) {
    float shadowOffset = scale < 0.8f ? 0.75f : 1.0f;
    primitives.drawText(left + shadowOffset, top + shadowOffset, text, 0.0f, 0.0f, 0.0f, scale);
    primitives.drawText(left, top, text, red, green, blue, scale);
  }

  private void drawInventoryStackAmount(ScreenRect slotRect, String text) {
    if (inventoryAmountFont == null) {
      primitives.drawText(slotRect.left() + 1.0f, slotRect.top() + 10.0f, text, 0.0f, 0.0f, 0.0f, 0.55f);
      primitives.drawText(slotRect.left(), slotRect.top() + 9.0f, text, 1.0f, 1.0f, 0.0f, 0.55f);
      return;
    }
    OpenGlTexture amountTexture = inventoryAmountTexture(text);
    if (amountTexture == null) {
      return;
    }
    primitives.drawTexturedQuad(
        amountTexture,
        new ScreenRect(
            slotRect.left(),
            slotRect.top() + 9.0f - inventoryAmountFont.maxGlyphHeight(),
            amountTexture.width(),
            amountTexture.height()
        )
    );
  }

  private OpenGlTexture inventoryAmountTexture(String text) {
    if (inventoryAmountFont == null || text == null || text.isEmpty()) {
      return null;
    }
    return inventoryAmountTextures.computeIfAbsent(text, this::createInventoryAmountTexture);
  }

  private OpenGlTexture createInventoryAmountTexture(String text) {
    int width = Math.max(1, inventoryAmountFont.measureText(text) + 1);
    int height = Math.max(1, inventoryAmountFont.maxGlyphHeight() + 1);
    int[] pixels = new int[width * height];
    inventoryAmountFont.drawText(
        pixels,
        width,
        height,
        text,
        0,
        inventoryAmountFont.maxGlyphHeight(),
        INVENTORY_STACK_TEXT_RGB,
        true
    );
    return OpenGlTexture.fromArgbImage(new ArgbImage(width, height, pixels));
  }

  private static void closeTexture(OpenGlTexture texture) {
    if (texture != null) {
      texture.close();
    }
  }
}
