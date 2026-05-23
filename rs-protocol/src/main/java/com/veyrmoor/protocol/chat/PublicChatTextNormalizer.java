package com.veyrmoor.protocol.chat;

public final class PublicChatTextNormalizer {

  private PublicChatTextNormalizer() {
  }

  public static String normalize(String text) {
    if (text == null) {
      throw new IllegalArgumentException("text cannot be null");
    }
    StringBuilder normalized = new StringBuilder(text.length());
    boolean capitalizeNextLetter = true;
    boolean previousWasSpace = false;
    for (int offset = 0; offset < text.length(); ) {
      int codePoint = text.codePointAt(offset);
      offset += Character.charCount(codePoint);
      if (Character.isWhitespace(codePoint) || Character.isSpaceChar(codePoint)) {
        if (!normalized.isEmpty() && !previousWasSpace) {
          normalized.append(' ');
          previousWasSpace = true;
        }
        continue;
      }
      int normalizedCodePoint = Character.toLowerCase(codePoint);
      if (capitalizeNextLetter && Character.isLetter(normalizedCodePoint)) {
        normalizedCodePoint = Character.toUpperCase(normalizedCodePoint);
        capitalizeNextLetter = false;
      } else if (Character.isLetter(normalizedCodePoint)) {
        capitalizeNextLetter = false;
      }
      normalized.appendCodePoint(normalizedCodePoint);
      previousWasSpace = false;
      if (normalizedCodePoint == '.' || normalizedCodePoint == '!' || normalizedCodePoint == '?') {
        capitalizeNextLetter = true;
      }
    }
    int normalizedLength = normalized.length();
    if (normalizedLength > 0 && normalized.charAt(normalizedLength - 1) == ' ') {
      normalized.setLength(normalizedLength - 1);
    }
    return normalized.toString();
  }
}
