package com.veyrmoor.client.desktop.gameplay.sidebar;

import static org.lwjgl.opengl.GL11.glColor3f;

import com.veyrmoor.client.core.ClientChatMessage;
import com.veyrmoor.client.core.ClientChatMessageKind;
import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.desktop.assets.image.ArgbImage;
import com.veyrmoor.client.desktop.assets.sprite.ArchiveSpriteResolver;
import com.veyrmoor.client.desktop.gameplay.GameplayChatController;
import com.veyrmoor.client.desktop.gameplay.GameplayChatState;
import com.veyrmoor.client.desktop.gameplay.GameplayClickResult;
import com.veyrmoor.client.desktop.gameplay.GameplayLayout;
import com.veyrmoor.client.desktop.gameplay.ReportAbuseController;
import com.veyrmoor.client.desktop.gameplay.sidebar.widget.SidebarWidgetRenderer;
import com.veyrmoor.client.desktop.login.TitleScreenBitmapFont;
import com.veyrmoor.client.desktop.render.common.ImmediateModeRenderer2d;
import com.veyrmoor.client.desktop.render.common.OpenGlTexture;
import com.veyrmoor.client.desktop.render.common.ScreenRect;
import com.veyrmoor.model.StaffRole;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

final class SidebarChatboxRenderer implements AutoCloseable {

  private static final float CHAT_LINE_HEIGHT = 14.0f;
  private static final float CHAT_HISTORY_BOTTOM_BASELINE_OFFSET = 70.0f;
  private static final float CHAT_PROMPT_LEFT_OFFSET = 4.0f;
  private static final float CHAT_RIGHT_PADDING = 4.0f;
  private static final int CHAT_HISTORY_LINES = 5;
  private static final int CHAT_LABEL_RGB = 0x000000;
  private static final int CHAT_PUBLIC_TEXT_RGB = 0x0000ff;
  private static final int CHAT_PROMPT_TEXT_RGB = 0x0000ff;
  private static final int CHAT_SYSTEM_RGB = 0x000000;
  private static final int CHAT_SEPARATOR_RGB = 0x000000;
  private static final float CHAT_SEPARATOR_TOP_OFFSET = 77.0f;
  private static final int CHAT_SCROLLBAR_TRACK_RGB = 0x23201b;
  private static final int CHAT_SCROLLBAR_THUMB_FILL_RGB = 0x4d4233;
  private static final int CHAT_SCROLLBAR_HIGHLIGHT_RGB = 0x766654;
  private static final int CHAT_SCROLLBAR_DARK_RGB = 0x332d25;
  private static final int CHAT_SCROLLBAR_WIDTH = 16;
  private static final int CHAT_SCROLLBAR_ARROW_HEIGHT = 16;
  private static final float CHAT_CROWN_BASELINE_OFFSET_Y = 12.0f;
  private static final float CHAT_CROWN_TEXT_GAP = 2.0f;
  private static final String CHAT_ELLIPSIS = "...";
  private static final String REPORT_ABUSE_LABEL = "Report abuse";
  private static final float REPORT_ABUSE_CENTER_X = 458.0f;
  private static final float REPORT_ABUSE_BASELINE_Y = 486.0f;
  private static final ClientChatMessage DEFAULT_WELCOME_MESSAGE =
      ClientChatMessage.system("Welcome to Veyrmoor! Have fun.");

  private final GameplayChatController chatController;
  private final TitleScreenBitmapFont chatFont;
  private final ImmediateModeRenderer2d primitives;
  private final OpenGlTexture chatScrollbarTopArrowTexture;
  private final OpenGlTexture chatScrollbarBottomArrowTexture;
  private final OpenGlTexture[] modIconTextures;
  private final Map<BitmapTextKey, OpenGlTexture> bitmapTextTextures = new HashMap<>();
  private final Map<BitmapGlyphKey, BitmapGlyphTexture> bitmapGlyphTextures = new HashMap<>();
  private boolean promptGlyphsPrewarmed;

  SidebarChatboxRenderer(
      GameplayChatController chatController,
      TitleScreenBitmapFont chatFont,
      ArchiveSpriteResolver mediaSpriteResolver,
      ImmediateModeRenderer2d primitives
  ) {
    this.chatController = chatController;
    this.chatFont = chatFont;
    this.primitives = primitives;
    this.chatScrollbarTopArrowTexture = spriteTexture(mediaSpriteResolver, "scrollbar", 0);
    this.chatScrollbarBottomArrowTexture = spriteTexture(mediaSpriteResolver, "scrollbar", 1);
    this.modIconTextures = new OpenGlTexture[]{
        spriteTexture(mediaSpriteResolver, "mod_icons", 0),
        spriteTexture(mediaSpriteResolver, "mod_icons", 1)
    };
    prewarmChatPromptGlyphTextures();
  }

  GameplayClickResult handleChatboxClick(double x, double y, ReportAbuseController reportAbuseController) {
    if (reportAbuseLabelRect().contains(x, y)) {
      if (reportAbuseController != null) {
        reportAbuseController.open();
      }
      return GameplayClickResult.handledClick();
    }
    if (!GameplayLayout.chatboxPanelRect().contains(x, y)) {
      return GameplayClickResult.ignored();
    }
    chatController.activateTyping();
    return GameplayClickResult.handledClick();
  }

  void drawChatbox(ClientViewModel viewModel) {
    ScreenRect chatboxRect = GameplayLayout.chatboxPanelRect();
    ScreenRect historyRect = GameplayLayout.chatHistoryRect();
    ScreenRect inputRect = GameplayLayout.chatInputRect();
    drawChatHistory(viewModel, historyRect);
    drawChatPromptSeparator(chatboxRect);
    drawChatPrompt(viewModel, inputRect);
    drawChatOptionsBar();
  }

  @Override
  public void close() {
    for (OpenGlTexture bitmapTextTexture : bitmapTextTextures.values()) {
      closeTexture(bitmapTextTexture);
    }
    for (BitmapGlyphTexture bitmapGlyphTexture : bitmapGlyphTextures.values()) {
      closeTexture(bitmapGlyphTexture.texture());
    }
    closeTexture(chatScrollbarTopArrowTexture);
    closeTexture(chatScrollbarBottomArrowTexture);
    for (OpenGlTexture modIconTexture : modIconTextures) {
      closeTexture(modIconTexture);
    }
  }

  static String fitChatHistoryText(TitleScreenBitmapFont font, String text, float maxWidth) {
    return fitChatTextFromStart(font, text, maxWidth);
  }

  static String fitChatPromptText(TitleScreenBitmapFont font, String text, float maxWidth) {
    return fitChatTextFromEnd(font, text, maxWidth);
  }

  private void drawChatHistory(ClientViewModel viewModel, ScreenRect historyRect) {
    List<ClientChatMessage> renderedMessages = renderedChatMessages(viewModel);
    int visibleCount = Math.min(renderedMessages.size(), CHAT_HISTORY_LINES);
    int startIndex = renderedMessages.size() - visibleCount;
    float historyRight = historyRect.left() + historyRect.width() - CHAT_RIGHT_PADDING;
    for (int index = 0; index < visibleCount; index++) {
      ClientChatMessage message = renderedMessages.get(startIndex + index);
      float baselineY = historyRect.top()
          + CHAT_HISTORY_BOTTOM_BASELINE_OFFSET
          - (visibleCount - 1 - index) * CHAT_LINE_HEIGHT;
      drawChatMessageLine(viewModel, historyRect.left(), historyRight, baselineY, message);
    }
    drawChatScrollbar(historyRect, Math.max(1, renderedMessages.size()));
  }

  private void drawChatPrompt(ClientViewModel viewModel, ScreenRect inputRect) {
    GameplayChatState chatState = chatController.state();
    String displayName = viewModel.bootstrap() == null ? "You" : viewModel.bootstrap().displayName();
    float baselineY = GameplayLayout.chatPromptBaselineY();
    drawBitmapTextAtBaseline(chatFont, inputRect.left() + CHAT_PROMPT_LEFT_OFFSET, baselineY, displayName + ":", CHAT_LABEL_RGB, false);
    float promptLeft = inputRect.left()
        + CHAT_PROMPT_LEFT_OFFSET
        + measureBitmapText(chatFont, displayName + ": ")
        + 2.0f;
    String promptText = fitChatPromptText(
        chatFont,
        chatState.draftText() + "*",
        inputRect.left() + inputRect.width() - CHAT_RIGHT_PADDING - promptLeft
    );
    drawBitmapTextGlyphsAtBaseline(chatFont, promptLeft, baselineY, promptText, CHAT_PROMPT_TEXT_RGB, false);
  }

  private void drawChatPromptSeparator(ScreenRect chatboxRect) {
    fillRect(
        chatboxRect.left(),
        chatboxRect.top() + CHAT_SEPARATOR_TOP_OFFSET,
        chatboxRect.width(),
        1.0f,
        CHAT_SEPARATOR_RGB
    );
  }

  private void drawChatOptionsBar() {
    drawBitmapTextCentered(chatFont, 55.0f, 481.0f, "Public chat", 0xffffff, true);
    drawBitmapTextCentered(chatFont, 55.0f, 494.0f, "On", 0x00ff00, true);
    drawBitmapTextCentered(chatFont, 184.0f, 481.0f, "Private chat", 0xffffff, true);
    drawBitmapTextCentered(chatFont, 184.0f, 494.0f, "On", 0x00ff00, true);
    drawBitmapTextCentered(chatFont, 324.0f, 481.0f, "Trade/compete", 0xffffff, true);
    drawBitmapTextCentered(chatFont, 324.0f, 494.0f, "On", 0x00ff00, true);
    drawBitmapTextCentered(chatFont, REPORT_ABUSE_CENTER_X, REPORT_ABUSE_BASELINE_Y, REPORT_ABUSE_LABEL, 0xffffff, true);
  }

  private void drawChatMessageLine(
      ClientViewModel viewModel,
      float left,
      float right,
      float baselineY,
      ClientChatMessage message
  ) {
    if (message.kind() == ClientChatMessageKind.SYSTEM || message.speakerDisplayName() == null) {
      drawBitmapTextAtBaseline(
          chatFont,
          left,
          baselineY,
          fitChatHistoryText(chatFont, message.text(), right - left),
          CHAT_SYSTEM_RGB,
          false
      );
      return;
    }
    float currentLeft = left;
    OpenGlTexture crownTexture = speakerCrownTexture(viewModel, message);
    if (crownTexture != null) {
      drawTextureAt(crownTexture, currentLeft, baselineY - CHAT_CROWN_BASELINE_OFFSET_Y);
      currentLeft += crownTexture.width() + CHAT_CROWN_TEXT_GAP;
    }
    String speaker = message.speakerDisplayName() + ":";
    drawBitmapTextAtBaseline(chatFont, currentLeft, baselineY, speaker, CHAT_LABEL_RGB, false);
    float messageLeft = currentLeft + measureBitmapText(chatFont, speaker + " ");
    drawBitmapTextAtBaseline(
        chatFont,
        messageLeft,
        baselineY,
        fitChatHistoryText(chatFont, message.text(), right - messageLeft),
        CHAT_PUBLIC_TEXT_RGB,
        false
    );
  }

  private void drawChatScrollbar(ScreenRect historyRect, int totalMessages) {
    ScreenRect scrollbarRect = new ScreenRect(
        historyRect.left() + historyRect.width(),
        historyRect.top(),
        CHAT_SCROLLBAR_WIDTH,
        historyRect.height()
    );
    drawTextureAt(chatScrollbarTopArrowTexture, scrollbarRect.left(), scrollbarRect.top());
    drawTextureAt(
        chatScrollbarBottomArrowTexture,
        scrollbarRect.left(),
        scrollbarRect.top() + scrollbarRect.height() - CHAT_SCROLLBAR_ARROW_HEIGHT
    );
    float trackTop = scrollbarRect.top() + CHAT_SCROLLBAR_ARROW_HEIGHT;
    float trackHeight = scrollbarRect.height() - CHAT_SCROLLBAR_ARROW_HEIGHT * 2.0f;
    fillRect(scrollbarRect.left(), trackTop, scrollbarRect.width(), trackHeight, CHAT_SCROLLBAR_TRACK_RGB);
    outlineRect(scrollbarRect.left(), trackTop, scrollbarRect.width(), trackHeight, CHAT_SCROLLBAR_DARK_RGB);
    int viewportHeight = Math.round(historyRect.height());
    int scrollContentHeight = Math.max(viewportHeight, totalMessages * Math.round(CHAT_LINE_HEIGHT));
    int thumbHeight = com.veyrmoor.client.desktop.gameplay.sidebar.widget.SidebarWidgetScrollState.scrollbarThumbHeight(
        viewportHeight,
        scrollContentHeight
    );
    int thumbOffset = com.veyrmoor.client.desktop.gameplay.sidebar.widget.SidebarWidgetScrollState.scrollbarThumbOffset(
        Math.max(0, scrollContentHeight - viewportHeight),
        viewportHeight,
        scrollContentHeight,
        thumbHeight
    );
    float thumbTop = scrollbarRect.top() + CHAT_SCROLLBAR_ARROW_HEIGHT + thumbOffset;
    fillRect(scrollbarRect.left() + 1.0f, thumbTop, scrollbarRect.width() - 2.0f, thumbHeight, CHAT_SCROLLBAR_THUMB_FILL_RGB);
    drawGlyphLine(scrollbarRect.left(), thumbTop, scrollbarRect.left() + scrollbarRect.width() - 1.0f, thumbTop, CHAT_SCROLLBAR_HIGHLIGHT_RGB);
    drawGlyphLine(scrollbarRect.left(), thumbTop, scrollbarRect.left(), thumbTop + thumbHeight - 1.0f, CHAT_SCROLLBAR_HIGHLIGHT_RGB);
    drawGlyphLine(
        scrollbarRect.left() + scrollbarRect.width() - 1.0f,
        thumbTop,
        scrollbarRect.left() + scrollbarRect.width() - 1.0f,
        thumbTop + thumbHeight - 1.0f,
        CHAT_SCROLLBAR_DARK_RGB
    );
    drawGlyphLine(
        scrollbarRect.left(),
        thumbTop + thumbHeight - 1.0f,
        scrollbarRect.left() + scrollbarRect.width() - 1.0f,
        thumbTop + thumbHeight - 1.0f,
        CHAT_SCROLLBAR_DARK_RGB
    );
  }

  private void drawBitmapTextAtBaseline(
      TitleScreenBitmapFont font,
      float left,
      float baselineY,
      String text,
      int rgb,
      boolean shadow
  ) {
    if (font == null || text == null || text.isEmpty()) {
      return;
    }
    SidebarWidgetRenderer.TextTextureLayout layout = SidebarWidgetRenderer.textTextureLayout(font, text, shadow);
    OpenGlTexture texture = bitmapTextTexture(font, text, rgb, shadow);
    if (texture == null) {
      return;
    }
    drawTextureAt(
        texture,
        left + layout.canvasLeft(),
        baselineY - font.maxGlyphHeight() + layout.canvasTop()
    );
  }

  private void drawBitmapTextGlyphsAtBaseline(
      TitleScreenBitmapFont font,
      float left,
      float baselineY,
      String text,
      int rgb,
      boolean shadow
  ) {
    if (font == null || text == null || text.isEmpty()) {
      return;
    }
    float currentLeft = left;
    for (int index = 0; index < text.length(); index++) {
      BitmapGlyphTexture glyphTexture = bitmapGlyphTexture(font, text.charAt(index), rgb, shadow);
      if (glyphTexture == null) {
        continue;
      }
      drawTextureAt(
          glyphTexture.texture(),
          currentLeft + glyphTexture.layout().canvasLeft(),
          baselineY - font.maxGlyphHeight() + glyphTexture.layout().canvasTop()
      );
      currentLeft += glyphTexture.advance();
    }
  }

  private void drawBitmapTextCentered(
      TitleScreenBitmapFont font,
      float centerX,
      float baselineY,
      String text,
      int rgb,
      boolean shadow
  ) {
    if (font == null || text == null || text.isEmpty()) {
      return;
    }
    float left = centerX - measureBitmapText(font, text) * 0.5f;
    drawBitmapTextAtBaseline(font, left, baselineY, text, rgb, shadow);
  }

  private OpenGlTexture bitmapTextTexture(
      TitleScreenBitmapFont font,
      String text,
      int rgb,
      boolean shadow
  ) {
    if (font == null || text == null || text.isEmpty()) {
      return null;
    }
    return bitmapTextTextures.computeIfAbsent(
        new BitmapTextKey(font, text, rgb, shadow),
        this::createBitmapTextTexture
    );
  }

  private OpenGlTexture createBitmapTextTexture(BitmapTextKey key) {
    SidebarWidgetRenderer.TextTextureLayout layout =
        SidebarWidgetRenderer.textTextureLayout(key.font(), key.text(), key.shadow());
    int[] pixels = new int[layout.width() * layout.height()];
    key.font().drawText(
        pixels,
        layout.width(),
        layout.height(),
        key.text(),
        -layout.canvasLeft(),
        layout.baselineY(),
        key.rgb(),
        key.shadow()
    );
    return OpenGlTexture.fromArgbImage(new ArgbImage(layout.width(), layout.height(), pixels));
  }

  private BitmapGlyphTexture bitmapGlyphTexture(
      TitleScreenBitmapFont font,
      char glyph,
      int rgb,
      boolean shadow
  ) {
    return bitmapGlyphTextures.computeIfAbsent(
        new BitmapGlyphKey(font, glyph, rgb, shadow),
        this::createBitmapGlyphTexture
    );
  }

  private BitmapGlyphTexture createBitmapGlyphTexture(BitmapGlyphKey key) {
    String glyphText = String.valueOf(key.glyph());
    SidebarWidgetRenderer.TextTextureLayout layout =
        SidebarWidgetRenderer.textTextureLayout(key.font(), glyphText, key.shadow());
    int[] pixels = new int[layout.width() * layout.height()];
    key.font().drawText(
        pixels,
        layout.width(),
        layout.height(),
        glyphText,
        -layout.canvasLeft(),
        layout.baselineY(),
        key.rgb(),
        key.shadow()
    );
    return new BitmapGlyphTexture(
        OpenGlTexture.fromArgbImage(new ArgbImage(layout.width(), layout.height(), pixels)),
        layout,
        key.font().measureGlyph(key.glyph())
    );
  }

  private void drawTextureAt(OpenGlTexture texture, float left, float top) {
    if (texture == null) {
      return;
    }
    primitives.drawTexturedQuad(texture, new ScreenRect(left, top, texture.width(), texture.height()));
  }

  private void fillRect(float left, float top, float width, float height, int rgb) {
    glColor3f(rgbUnit(rgb, 16), rgbUnit(rgb, 8), rgbUnit(rgb, 0));
    primitives.drawQuad(left, top, width, height);
  }

  private void outlineRect(float left, float top, float width, float height, int rgb) {
    glColor3f(rgbUnit(rgb, 16), rgbUnit(rgb, 8), rgbUnit(rgb, 0));
    primitives.drawOutline(left, top, width, height);
  }

  private void drawGlyphLine(float startX, float startY, float endX, float endY, int rgb) {
    glColor3f(rgbUnit(rgb, 16), rgbUnit(rgb, 8), rgbUnit(rgb, 0));
    primitives.drawLine(startX, startY, endX, endY);
  }

  private ScreenRect reportAbuseLabelRect() {
    float labelWidth = Math.max(80.0f, measureBitmapText(chatFont, REPORT_ABUSE_LABEL));
    float labelLeft = REPORT_ABUSE_CENTER_X - labelWidth * 0.5f;
    float labelTop = REPORT_ABUSE_BASELINE_Y - (chatFont == null ? 10.0f : chatFont.maxGlyphHeight());
    float labelHeight = chatFont == null ? 12.0f : chatFont.maxGlyphHeight() + 2.0f;
    return new ScreenRect(labelLeft, labelTop, labelWidth, labelHeight);
  }

  private List<ClientChatMessage> renderedChatMessages(ClientViewModel viewModel) {
    if (!viewModel.chatMessages().isEmpty()) {
      return viewModel.chatMessages();
    }
    return List.of(DEFAULT_WELCOME_MESSAGE);
  }

  private OpenGlTexture speakerCrownTexture(ClientViewModel viewModel, ClientChatMessage message) {
    if (message == null || message.speakerDisplayName() == null || modIconTextures.length < 2) {
      return null;
    }
    StaffRole staffRole = speakerStaffRole(viewModel, message.speakerDisplayName());
    if (!staffRole.hasCrown()) {
      return null;
    }
    int crownSpriteIndex = staffRole.crownSpriteIndex();
    return crownSpriteIndex >= 0 && crownSpriteIndex < modIconTextures.length
        ? modIconTextures[crownSpriteIndex]
        : null;
  }

  private StaffRole speakerStaffRole(ClientViewModel viewModel, String speakerDisplayName) {
    if (speakerDisplayName == null || viewModel == null || viewModel.bootstrap() == null) {
      return StaffRole.NONE;
    }
    if (!speakerDisplayName.equals(viewModel.bootstrap().displayName())) {
      return StaffRole.NONE;
    }
    return viewModel.bootstrap().profile() == null ? StaffRole.NONE : viewModel.bootstrap().profile().staffRole();
  }

  private static float measureBitmapText(TitleScreenBitmapFont font, String text) {
    if (font == null || text == null) {
      return 0.0f;
    }
    return font.measureText(text);
  }

  private static String fitChatTextFromStart(TitleScreenBitmapFont font, String text, float maxWidth) {
    String safeText = Objects.toString(text, "");
    if (font == null || safeText.isEmpty() || maxWidth <= 0.0f) {
      return "";
    }
    if (measureBitmapText(font, safeText) <= maxWidth) {
      return safeText;
    }
    float ellipsisWidth = measureBitmapText(font, CHAT_ELLIPSIS);
    if (ellipsisWidth > maxWidth) {
      return "";
    }
    String bestFit = CHAT_ELLIPSIS;
    for (int endIndex = 1; endIndex <= safeText.length(); endIndex++) {
      String candidate = safeText.substring(0, endIndex) + CHAT_ELLIPSIS;
      if (measureBitmapText(font, candidate) <= maxWidth) {
        bestFit = candidate;
        continue;
      }
      break;
    }
    return bestFit;
  }

  private static String fitChatTextFromEnd(TitleScreenBitmapFont font, String text, float maxWidth) {
    String safeText = Objects.toString(text, "");
    if (font == null || safeText.isEmpty() || maxWidth <= 0.0f) {
      return "";
    }
    if (measureBitmapText(font, safeText) <= maxWidth) {
      return safeText;
    }
    float ellipsisWidth = measureBitmapText(font, CHAT_ELLIPSIS);
    if (ellipsisWidth > maxWidth) {
      return "";
    }
    String bestFit = CHAT_ELLIPSIS;
    for (int startIndex = safeText.length() - 1; startIndex >= 0; startIndex--) {
      String candidate = CHAT_ELLIPSIS + safeText.substring(startIndex);
      if (measureBitmapText(font, candidate) <= maxWidth) {
        bestFit = candidate;
        continue;
      }
      break;
    }
    return bestFit;
  }

  private void prewarmChatPromptGlyphTextures() {
    if (chatFont == null || promptGlyphsPrewarmed) {
      return;
    }
    for (int glyph = 32; glyph <= 126; glyph++) {
      bitmapGlyphTexture(chatFont, (char) glyph, CHAT_PROMPT_TEXT_RGB, false);
    }
    promptGlyphsPrewarmed = true;
  }

  private static float rgbUnit(int rgb, int shift) {
    return ((rgb >>> shift) & 0xff) / 255.0f;
  }

  private static void closeTexture(OpenGlTexture texture) {
    if (texture != null) {
      texture.close();
    }
  }

  private static OpenGlTexture spriteTexture(
      ArchiveSpriteResolver spriteResolver,
      String entryName,
      int frameIndex
  ) {
    if (spriteResolver == null) {
      return null;
    }
    ArgbImage image = spriteResolver.resolve(entryName, frameIndex);
    return image == null ? null : OpenGlTexture.fromArgbImage(image);
  }

  private record BitmapTextKey(
      TitleScreenBitmapFont font,
      String text,
      int rgb,
      boolean shadow
  ) {
  }

  private record BitmapGlyphKey(
      TitleScreenBitmapFont font,
      char glyph,
      int rgb,
      boolean shadow
  ) {
  }

  private record BitmapGlyphTexture(
      OpenGlTexture texture,
      SidebarWidgetRenderer.TextTextureLayout layout,
      int advance
  ) {
  }
}
