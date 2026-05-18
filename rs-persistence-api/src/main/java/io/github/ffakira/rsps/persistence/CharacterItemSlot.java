package io.github.ffakira.rsps.persistence;

public record CharacterItemSlot(ItemContainerKind containerKind, int slotIndex, int itemId, int quantity) {

  public CharacterItemSlot {
    if (slotIndex < 0) {
      throw new IllegalArgumentException("Slot index cannot be negative");
    }
    if (quantity < 0) {
      throw new IllegalArgumentException("Quantity cannot be negative");
    }
  }
}
