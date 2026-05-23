package com.veyrmoor.cache;

import java.util.Locale;

public final class ArchiveNameHasher {

  private ArchiveNameHasher() {
  }

  public static int hash(String entryName) {
    int hash = 0;
    String normalizedName = entryName.toUpperCase(Locale.ROOT);
    for (int index = 0; index < normalizedName.length(); index++) {
      hash = (hash * 61 + normalizedName.charAt(index)) - 32;
    }
    return hash;
  }
}
