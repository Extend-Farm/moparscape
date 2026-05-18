package io.github.ffakira.rsps.client.desktop.core;

import io.github.ffakira.rsps.client.core.ClientViewModel;
import io.github.ffakira.rsps.content.ItemDefinition;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import io.github.ffakira.rsps.protocol.BootstrapItemSlot;
import io.github.ffakira.rsps.protocol.BootstrapSkill;
import io.github.ffakira.rsps.client.desktop.world.minimap.MapBackClipMasks;
import io.github.ffakira.rsps.client.desktop.world.minimap.MinimapViewport;
import io.github.ffakira.rsps.client.desktop.world.minimap.MinimapViewportPlanner;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.glColor3f;

final class GameplayChromeRenderer implements AutoCloseable {

  private static final int MINIMAP_VISIBLE_TILES = 37;
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

  private final ImmediateModeRenderer2d primitives;
  private final GameplayFrameAssets gameplayFrameAssets;
  private final OpenGlTexture invBackTexture;
  private final OpenGlTexture chatBackTexture;
  private final OpenGlTexture mapBackTexture;
  private final OpenGlTexture backBase1Texture;
  private final OpenGlTexture backBase2Texture;
  private final OpenGlTexture backHMid1Texture;
  private final OpenGlTexture compassTexture;
  private final OpenGlTexture redstone1Texture;
  private final OpenGlTexture redstone2Texture;
  private final OpenGlTexture redstone3Texture;
  private final OpenGlTexture redstone1FlippedTexture;
  private final OpenGlTexture redstone2FlippedTexture;
  private final OpenGlTexture redstone1MirroredTexture;
  private final OpenGlTexture redstone2MirroredTexture;
  private final OpenGlTexture redstone3MirroredTexture;
  private final OpenGlTexture redstone1BothTransformsTexture;
  private final OpenGlTexture redstone2BothTransformsTexture;
  private final OpenGlTexture[] sideIconTextures;
  private final ItemDefinitionCatalog itemDefinitionCatalog;
  private final ItemIconRenderer itemIconRenderer;
  private final Map<Integer, OpenGlTexture> itemIconTextures = new HashMap<>();
  private final Map<String, OpenGlTexture> inventoryAmountTextures = new HashMap<>();
  private final TitleScreenBitmapFont inventoryAmountFont;
  private final MinimapViewportPlanner minimapViewportPlanner = new MinimapViewportPlanner();
  private final MapBackClipMasks mapBackClipMasks;
  private GameplayTab activeGameplayTab = GameplayTab.INVENTORY;

  GameplayChromeRenderer(
      GameplayFrameAssets gameplayFrameAssets,
      ItemDefinitionCatalog itemDefinitionCatalog,
      ItemIconRenderer itemIconRenderer,
      TitleScreenFonts titleScreenFonts,
      ImmediateModeRenderer2d primitives
  ) {
    this.gameplayFrameAssets = gameplayFrameAssets;
    this.itemDefinitionCatalog = itemDefinitionCatalog;
    this.itemIconRenderer = itemIconRenderer;
    this.primitives = primitives;
    this.invBackTexture = gameplayFrameAssets == null ? null : OpenGlTexture.fromArgbImage(gameplayFrameAssets.invBack());
    this.chatBackTexture = gameplayFrameAssets == null ? null : OpenGlTexture.fromArgbImage(gameplayFrameAssets.chatBack());
    this.mapBackTexture = gameplayFrameAssets == null ? null : OpenGlTexture.fromArgbImage(gameplayFrameAssets.mapBack());
    this.backBase1Texture = gameplayFrameAssets == null ? null : OpenGlTexture.fromArgbImage(gameplayFrameAssets.backBase1());
    this.backBase2Texture = gameplayFrameAssets == null ? null : OpenGlTexture.fromArgbImage(gameplayFrameAssets.backBase2());
    this.backHMid1Texture = gameplayFrameAssets == null ? null : OpenGlTexture.fromArgbImage(gameplayFrameAssets.backHMid1());
    this.compassTexture = gameplayFrameAssets == null ? null : OpenGlTexture.fromArgbImage(gameplayFrameAssets.compass());
    this.redstone1Texture = gameplayFrameAssets == null ? null : OpenGlTexture.fromArgbImage(gameplayFrameAssets.redstone1());
    this.redstone2Texture = gameplayFrameAssets == null ? null : OpenGlTexture.fromArgbImage(gameplayFrameAssets.redstone2());
    this.redstone3Texture = gameplayFrameAssets == null ? null : OpenGlTexture.fromArgbImage(gameplayFrameAssets.redstone3());
    this.redstone1FlippedTexture = gameplayFrameAssets == null ? null : OpenGlTexture.fromArgbImage(gameplayFrameAssets.redstone1Flipped());
    this.redstone2FlippedTexture = gameplayFrameAssets == null ? null : OpenGlTexture.fromArgbImage(gameplayFrameAssets.redstone2Flipped());
    this.redstone1MirroredTexture = gameplayFrameAssets == null ? null : OpenGlTexture.fromArgbImage(gameplayFrameAssets.redstone1Mirrored());
    this.redstone2MirroredTexture = gameplayFrameAssets == null ? null : OpenGlTexture.fromArgbImage(gameplayFrameAssets.redstone2Mirrored());
    this.redstone3MirroredTexture = gameplayFrameAssets == null ? null : OpenGlTexture.fromArgbImage(gameplayFrameAssets.redstone3Mirrored());
    this.redstone1BothTransformsTexture = gameplayFrameAssets == null ? null : OpenGlTexture.fromArgbImage(gameplayFrameAssets.redstone1BothTransforms());
    this.redstone2BothTransformsTexture = gameplayFrameAssets == null ? null : OpenGlTexture.fromArgbImage(gameplayFrameAssets.redstone2BothTransforms());
    this.sideIconTextures = createSideIconTextures(gameplayFrameAssets);
    this.mapBackClipMasks = gameplayFrameAssets == null ? null : MapBackClipMasks.fromMapBack(gameplayFrameAssets.mapBack());
    this.inventoryAmountFont = titleScreenFonts == null ? null : titleScreenFonts.plainSmall();
  }

  boolean handleClick(double x, double y) {
    for (GameplayTab gameplayTab : GameplayTab.values()) {
      if (GameplayLayout.gameplayTabRect(gameplayTab).contains(x, y)) {
        activeGameplayTab = gameplayTab;
        return true;
      }
    }
    return false;
  }

  void resetToInventoryTab() {
    activeGameplayTab = GameplayTab.INVENTORY;
  }

  void render(
      ClientViewModel viewModel,
      WorldScene worldScene,
      OpenGlTexture worldMinimapTexture,
      WorldCameraState cameraState
  ) {
    // Chrome is a deliberate mix today:
    // - authentic cache-backed frame art when media assets are available
    // - a native scene-state minimap inside that frame
    // - native/synthetic sidebar and chat contents until widget decoding is ported
    if (gameplayFrameAssets != null) {
      drawGameplayFrameAssets();
    } else {
      drawPanel(GameplayLayout.worldViewportRect(), 0.07f, 0.08f, 0.10f);
      drawPanel(GameplayLayout.minimapPanelRect(), 0.09f, 0.11f, 0.15f);
      drawPanel(GameplayLayout.sidebarPanelRect(), 0.09f, 0.11f, 0.15f);
      drawPanel(GameplayLayout.chatboxPanelRect(), 0.09f, 0.11f, 0.15f);
    }
    drawActiveGameplayTabHighlight();
    drawSideIcons();
    drawMinimap(viewModel, worldScene, worldMinimapTexture, cameraState);
    drawSidebar(viewModel);
    drawChatbox(viewModel);
  }

  private void drawGameplayFrameAssets() {
    // These are real cache `media` archive assets. They establish the authentic frame shell even
    // though the panel contents rendered inside them are still largely native placeholders.
    primitives.drawTexturedQuad(mapBackTexture, GameplayLayout.minimapPanelRect());
    primitives.drawTexturedQuad(backHMid1Texture, GameplayLayout.topTabRect());
    primitives.drawTexturedQuad(backBase2Texture, GameplayLayout.bottomTabRect());
    primitives.drawTexturedQuad(invBackTexture, GameplayLayout.sidebarPanelRect());
    primitives.drawTexturedQuad(chatBackTexture, GameplayLayout.chatboxPanelRect());
    primitives.drawTexturedQuad(backBase1Texture, new ScreenRect(0.0f, 453.0f, 496.0f, 50.0f));
  }

  private void drawSideIcons() {
    if (sideIconTextures.length < 13) {
      return;
    }
    float[][] positions = {
        {516.0f + 29.0f, 160.0f + 13.0f},
        {516.0f + 53.0f, 160.0f + 11.0f},
        {516.0f + 82.0f, 160.0f + 11.0f},
        {516.0f + 115.0f, 160.0f + 12.0f},
        {516.0f + 153.0f, 160.0f + 13.0f},
        {516.0f + 180.0f, 160.0f + 11.0f},
        {516.0f + 208.0f, 160.0f + 13.0f},
        {496.0f + 74.0f, 466.0f + 2.0f},
        {496.0f + 102.0f, 466.0f + 3.0f},
        {496.0f + 137.0f, 466.0f + 4.0f},
        {496.0f + 174.0f, 466.0f + 2.0f},
        {496.0f + 201.0f, 466.0f + 2.0f},
        {496.0f + 226.0f, 466.0f + 2.0f}
    };
    for (int index = 0; index < Math.min(sideIconTextures.length, positions.length); index++) {
      OpenGlTexture sideIconTexture = sideIconTextures[index];
      if (sideIconTexture == null) {
        continue;
      }
      primitives.drawTexturedQuad(
          sideIconTexture,
          new ScreenRect(positions[index][0], positions[index][1], sideIconTexture.width(), sideIconTexture.height())
      );
    }
  }

  private void drawPanel(ScreenRect rect, float red, float green, float blue) {
    glColor3f(red, green, blue);
    primitives.drawQuad(rect.left(), rect.top(), rect.width(), rect.height());
    glColor3f(0.33f, 0.40f, 0.52f);
    primitives.drawOutline(rect.left(), rect.top(), rect.width(), rect.height());
  }

  private void drawMinimap(
      ClientViewModel viewModel,
      WorldScene worldScene,
      OpenGlTexture worldMinimapTexture,
      WorldCameraState cameraState
  ) {
    ScreenRect minimapRect = GameplayLayout.minimapContentRect();
    MinimapViewport minimapViewport = minimapViewportPlanner.plan(
        worldScene,
        viewModel.localPlayerPosition(),
        MINIMAP_VISIBLE_TILES
    );
    if (minimapViewport != null && worldMinimapTexture != null) {
      if (mapBackClipMasks != null) {
        drawRotatedMinimap(worldMinimapTexture, minimapRect, minimapViewport, cameraState);
        drawCenteredMinimapPlayer(minimapRect);
      } else {
        primitives.drawCroppedTexturedOval(
            worldMinimapTexture,
            minimapViewport.sourceX(),
            minimapViewport.sourceY(),
            minimapViewport.sourceWidth(),
            minimapViewport.sourceHeight(),
            minimapRect
        );
        drawMinimapPlayer(minimapRect, minimapViewport);
      }
      drawCompass(cameraState);
      return;
    }
    glColor3f(0.03f, 0.05f, 0.07f);
    primitives.drawQuad(minimapRect.left(), minimapRect.top(), minimapRect.width(), minimapRect.height());
    glColor3f(0.20f, 0.24f, 0.30f);
    primitives.drawOutline(minimapRect.left(), minimapRect.top(), minimapRect.width(), minimapRect.height());
    primitives.drawCenteredText(
        minimapRect.left() + minimapRect.width() / 2.0f,
        minimapRect.top() + minimapRect.height() / 2.0f,
        "Loading map",
        0.92f,
        0.86f,
        0.46f
    );
    drawCompass(cameraState);
  }

  private void drawSidebar(ClientViewModel viewModel) {
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
      case EXTRA -> drawSidebarPlaceholder(left, top + 16.0f, "Extra tab reserved.", "Used as a placeholder for the final tab slot.");
    }
  }

  private void drawChatbox(ClientViewModel viewModel) {
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

  private void drawInventorySidebar(ClientViewModel viewModel, ScreenRect sidebarRect) {
    if (itemIconRenderer == null) {
      drawInventoryTextFallback(viewModel, sidebarRect.left() + 12.0f, sidebarRect.top() + 38.0f);
      return;
    }
    List<io.github.ffakira.rsps.client.core.BootstrapInventoryItemPresentation> inventory = viewModel.inventoryPresentation();
    if (inventory.isEmpty()) {
      drawInventoryTextFallback(viewModel, sidebarRect.left() + 12.0f, sidebarRect.top() + 38.0f);
      return;
    }
    for (var item : inventory) {
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
    primitives.drawText(left, top - 16.0f, activeGameplayTab.label(), 0.92f, 0.86f, 0.46f);
    List<io.github.ffakira.rsps.client.core.BootstrapInventoryItemPresentation> inventory = viewModel.inventoryPresentation();
    if (!inventory.isEmpty()) {
      int lineCount = Math.min(10, inventory.size());
      for (int index = 0; index < lineCount; index++) {
        var item = inventory.get(index);
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
    List<io.github.ffakira.rsps.client.core.BootstrapEquipmentItemPresentation> equipment = viewModel.equipmentPresentation();
    if (!equipment.isEmpty()) {
      int row = 0;
      for (var item : equipment) {
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
    List<io.github.ffakira.rsps.client.core.BootstrapSkillPresentation> skills = viewModel.skillPresentation();
    if (!skills.isEmpty()) {
      int lineCount = Math.min(15, skills.size());
      for (int index = 0; index < lineCount; index++) {
        var skill = skills.get(index);
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

  private void drawMinimapPlayer(ScreenRect minimapRect, MinimapViewport minimapViewport) {
    float scaleX = minimapRect.width() / minimapViewport.sourceWidth();
    float scaleY = minimapRect.height() / minimapViewport.sourceHeight();
    float markerLeft = minimapRect.left() + minimapViewport.markerSourceX() * scaleX - 2.0f;
    float markerTop = minimapRect.top() + minimapViewport.markerSourceY() * scaleY - 2.0f;
    glColor3f(0.96f, 0.96f, 0.96f);
    primitives.drawQuad(markerLeft, markerTop, 4.0f, 4.0f);
    glColor3f(0.98f, 0.82f, 0.16f);
    primitives.drawOutline(markerLeft, markerTop, 4.0f, 4.0f);
  }

  private void drawRotatedMinimap(
      OpenGlTexture worldMinimapTexture,
      ScreenRect minimapRect,
      MinimapViewport minimapViewport,
      WorldCameraState cameraState
  ) {
    primitives.drawMaskedRotatedTexturedRows(
        worldMinimapTexture,
        minimapRect,
        MapBackClipMasks.MINIMAP_WIDTH,
        mapBackClipMasks.minimapRowStarts(),
        mapBackClipMasks.minimapRowWidths(),
        cameraState == null ? 0.0f : cameraState.yawDegrees(),
        minimapViewport.sourceX() + minimapViewport.markerSourceX(),
        minimapViewport.sourceY() + minimapViewport.markerSourceY()
    );
  }

  private void drawCenteredMinimapPlayer(ScreenRect minimapRect) {
    float markerLeft = minimapRect.left() + minimapRect.width() * 0.5f - 2.0f;
    float markerTop = minimapRect.top() + minimapRect.height() * 0.5f - 2.0f;
    glColor3f(0.96f, 0.96f, 0.96f);
    primitives.drawQuad(markerLeft, markerTop, 4.0f, 4.0f);
    glColor3f(0.98f, 0.82f, 0.16f);
    primitives.drawOutline(markerLeft, markerTop, 4.0f, 4.0f);
  }

  private void drawActiveGameplayTabHighlight() {
    OpenGlTexture highlightTexture = gameplayTabHighlightTexture(activeGameplayTab);
    if (highlightTexture != null) {
      primitives.drawTexturedQuad(
          highlightTexture,
          GameplayLayout.gameplayTabHighlightRect(activeGameplayTab, highlightTexture.width(), highlightTexture.height())
      );
      return;
    }
  }

  private void drawCompass(WorldCameraState cameraState) {
    if (compassTexture == null || mapBackClipMasks == null) {
      return;
    }
    primitives.drawMaskedRotatedTexturedRows(
        compassTexture,
        GameplayLayout.compassRect(),
        MapBackClipMasks.COMPASS_WIDTH,
        mapBackClipMasks.compassRowStarts(),
        mapBackClipMasks.compassRowWidths(),
        cameraState == null ? 0.0f : cameraState.yawDegrees(),
        compassTexture.width() / 2.0f,
        compassTexture.height() / 2.0f
    );
  }

  private OpenGlTexture gameplayTabHighlightTexture(GameplayTab gameplayTab) {
    return switch (gameplayTab.index()) {
      case 0 -> redstone1Texture;
      case 1, 2 -> redstone2Texture;
      case 3 -> redstone3Texture;
      case 4, 5 -> redstone2FlippedTexture;
      case 6 -> redstone1FlippedTexture;
      case 7 -> redstone1MirroredTexture;
      case 8, 9 -> redstone2MirroredTexture;
      case 10 -> redstone3MirroredTexture;
      case 11, 12 -> redstone2BothTransformsTexture;
      case 13 -> redstone1BothTransformsTexture;
      default -> redstone3Texture;
    };
  }

  private void drawResolvedItemList(float left, float top, List<BootstrapItemSlot> itemSlots, int maxLines) {
    int lineCount = Math.min(maxLines, itemSlots.size());
    for (int index = 0; index < lineCount; index++) {
      var slot = itemSlots.get(index);
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
      var skill = skills.get(index);
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

  private boolean showsStackAmount(io.github.ffakira.rsps.client.core.BootstrapInventoryItemPresentation item) {
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

  private float inventorySlotCellRight(ScreenRect sidebarRect, int slotIndex) {
    int column = slotIndex % INVENTORY_COLUMNS;
    return sidebarRect.left() + INVENTORY_SLOT_LEFT + (column + 1) * INVENTORY_SLOT_STEP_X;
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

  private void drawRightAlignedShadowedText(float right, float top, String text, float red, float green, float blue) {
    drawRightAlignedShadowedText(right, top, text, red, green, blue, 1.0f);
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

  private void drawRightAlignedShadowedText(
      float right,
      float top,
      String text,
      float red,
      float green,
      float blue,
      float scale
  ) {
    float left = right - primitives.measureTextWidth(text, scale);
    drawShadowedText(left, top, text, red, green, blue, scale);
  }

  @Override
  public void close() {
    closeTexture(invBackTexture);
    closeTexture(chatBackTexture);
    closeTexture(mapBackTexture);
    closeTexture(backBase1Texture);
    closeTexture(backBase2Texture);
    closeTexture(backHMid1Texture);
    closeTexture(compassTexture);
    closeTexture(redstone1Texture);
    closeTexture(redstone2Texture);
    closeTexture(redstone3Texture);
    closeTexture(redstone1FlippedTexture);
    closeTexture(redstone2FlippedTexture);
    closeTexture(redstone1MirroredTexture);
    closeTexture(redstone2MirroredTexture);
    closeTexture(redstone3MirroredTexture);
    closeTexture(redstone1BothTransformsTexture);
    closeTexture(redstone2BothTransformsTexture);
    for (OpenGlTexture sideIconTexture : sideIconTextures) {
      closeTexture(sideIconTexture);
    }
    for (OpenGlTexture itemIconTexture : itemIconTextures.values()) {
      closeTexture(itemIconTexture);
    }
    for (OpenGlTexture inventoryAmountTexture : inventoryAmountTextures.values()) {
      closeTexture(inventoryAmountTexture);
    }
  }

  private static OpenGlTexture[] createSideIconTextures(GameplayFrameAssets gameplayFrameAssets) {
    if (gameplayFrameAssets == null || gameplayFrameAssets.sideIcons() == null) {
      return new OpenGlTexture[0];
    }
    OpenGlTexture[] textures = new OpenGlTexture[gameplayFrameAssets.sideIcons().length];
    for (int index = 0; index < gameplayFrameAssets.sideIcons().length; index++) {
      ArgbImage sideIcon = gameplayFrameAssets.sideIcons()[index];
      textures[index] = sideIcon == null ? null : OpenGlTexture.fromArgbImage(sideIcon);
    }
    return textures;
  }

  private static void closeTexture(OpenGlTexture texture) {
    if (texture != null) {
      texture.close();
    }
  }
}
