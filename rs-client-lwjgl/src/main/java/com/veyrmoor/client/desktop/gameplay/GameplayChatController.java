package com.veyrmoor.client.desktop.gameplay;

import com.veyrmoor.protocol.ProtocolLimits;
import com.veyrmoor.protocol.chat.PublicChatTextNormalizer;

public final class GameplayChatController {

  private final StringBuilder draftText = new StringBuilder(ProtocolLimits.PUBLIC_CHAT_TEXT_BYTES);
  private int draftUtf8Bytes;
  private boolean typing;
  private volatile GameplayChatState state = GameplayChatState.idle();

  public GameplayChatState state() {
    return state;
  }

  public boolean isTyping() {
    return typing;
  }

  public void activateTyping() {
    if (typing) {
      return;
    }
    typing = true;
    publishState();
  }

  public void appendCodePoint(int codePoint) {
    if (!typing || !Character.isValidCodePoint(codePoint) || Character.isISOControl(codePoint)) {
      return;
    }
    int normalizedCodePoint = normalizeCodePoint(codePoint);
    if (normalizedCodePoint == ' ' && (draftText.isEmpty() || endsWithSpace())) {
      return;
    }
    int codePointUtf8Bytes = utf8ByteLength(normalizedCodePoint);
    if (draftUtf8Bytes + codePointUtf8Bytes > ProtocolLimits.PUBLIC_CHAT_TEXT_BYTES) {
      return;
    }
    draftText.appendCodePoint(normalizedCodePoint);
    draftUtf8Bytes += codePointUtf8Bytes;
    publishState();
  }

  public void backspace() {
    if (!typing || draftText.length() == 0) {
      return;
    }
    int nextEndIndex = draftText.offsetByCodePoints(draftText.length(), -1);
    draftUtf8Bytes -= utf8ByteLength(draftText.codePointAt(nextEndIndex));
    draftText.delete(nextEndIndex, draftText.length());
    publishState();
  }

  public String submitDraft() {
    if (!typing) {
      return null;
    }
    String submittedText = PublicChatTextNormalizer.normalize(draftText.toString());
    clearDraft();
    return submittedText.isEmpty() ? null : submittedText;
  }

  public void cancelTyping() {
    clearDraft();
  }

  public void reset() {
    clearDraft();
  }

  private void clearDraft() {
    typing = false;
    draftText.setLength(0);
    draftUtf8Bytes = 0;
    state = GameplayChatState.idle();
  }

  private void publishState() {
    state = typing ? GameplayChatState.typing(draftText.toString()) : GameplayChatState.idle();
  }

  private static int normalizeCodePoint(int codePoint) {
    if (Character.isWhitespace(codePoint) || Character.isSpaceChar(codePoint)) {
      return ' ';
    }
    return Character.toLowerCase(codePoint);
  }

  private boolean endsWithSpace() {
    return draftText.length() > 0 && draftText.charAt(draftText.length() - 1) == ' ';
  }

  private static int utf8ByteLength(int codePoint) {
    if (codePoint <= 0x7f) {
      return 1;
    }
    if (codePoint <= 0x7ff) {
      return 2;
    }
    if (codePoint <= 0xffff) {
      return 3;
    }
    return 4;
  }
}
