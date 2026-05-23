package com.veyrmoor.persistence.sql;

import java.util.Locale;

final class SqlNamePolicy {

  static final int ACCOUNT_LOGIN_NAME_MAX_LENGTH = 20;
  static final int CHARACTER_DISPLAY_NAME_MAX_LENGTH = 32;

  private SqlNamePolicy() {
  }

  static String accountLoginName(String value) {
    return requireStoredName(value, "account login name", ACCOUNT_LOGIN_NAME_MAX_LENGTH);
  }

  static String accountLoginKey(String value) {
    return lookupKey(value, "account login name", ACCOUNT_LOGIN_NAME_MAX_LENGTH);
  }

  static String characterDisplayName(String value) {
    return normalizeDisplayNameCase(
        requireStoredName(value, "character display name", CHARACTER_DISPLAY_NAME_MAX_LENGTH)
    );
  }

  static String characterDisplayNameKey(String value) {
    return lookupKey(value, "character display name", CHARACTER_DISPLAY_NAME_MAX_LENGTH);
  }

  private static String requireStoredName(String value, String label, int maxLength) {
    String trimmed = requireTrimmed(value, label);
    if (trimmed.length() > maxLength) {
      throw new IllegalArgumentException(label + " cannot be longer than " + maxLength + " characters");
    }
    return trimmed;
  }

  private static String lookupKey(String value, String label, int maxLength) {
    return requireStoredName(value, label, maxLength).toLowerCase(Locale.ROOT);
  }

  private static String requireTrimmed(String value, String label) {
    if (value == null) {
      throw new IllegalArgumentException(label + " cannot be null");
    }
    String trimmed = value.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException(label + " cannot be blank");
    }
    return trimmed;
  }

  private static String normalizeDisplayNameCase(String value) {
    StringBuilder normalized = new StringBuilder(value.length());
    boolean capitalizeNextLetter = true;
    for (int index = 0; index < value.length(); ) {
      int codePoint = value.codePointAt(index);
      if (Character.isLetter(codePoint)) {
        normalized.appendCodePoint(capitalizeNextLetter ? Character.toTitleCase(codePoint) : Character.toLowerCase(codePoint));
        capitalizeNextLetter = false;
      } else {
        normalized.appendCodePoint(codePoint);
        capitalizeNextLetter = !Character.isLetterOrDigit(codePoint);
      }
      index += Character.charCount(codePoint);
    }
    return normalized.toString();
  }
}
