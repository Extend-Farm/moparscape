package com.veyrmoor.persistence;

public enum ItemContainerKind {
  INVENTORY(0),
  EQUIPMENT(1),
  BANK(2);

  private final short databaseValue;

  ItemContainerKind(int databaseValue) {
    this.databaseValue = (short) databaseValue;
  }

  public short databaseValue() {
    return databaseValue;
  }

  public static ItemContainerKind fromDatabaseValue(short databaseValue) {
    for (ItemContainerKind containerKind : values()) {
      if (containerKind.databaseValue == databaseValue) {
        return containerKind;
      }
    }
    throw new IllegalArgumentException("Unsupported item container kind: " + databaseValue);
  }
}
