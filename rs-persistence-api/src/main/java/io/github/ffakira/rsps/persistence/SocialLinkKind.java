package io.github.ffakira.rsps.persistence;

public enum SocialLinkKind {
  FRIEND(0),
  IGNORE(1);

  private final short databaseValue;

  SocialLinkKind(int databaseValue) {
    this.databaseValue = (short) databaseValue;
  }

  public short databaseValue() {
    return databaseValue;
  }

  public static SocialLinkKind fromDatabaseValue(short databaseValue) {
    for (SocialLinkKind linkKind : values()) {
      if (linkKind.databaseValue == databaseValue) {
        return linkKind;
      }
    }
    throw new IllegalArgumentException("Unsupported social link kind: " + databaseValue);
  }
}
