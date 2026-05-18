package io.github.ffakira.rsps.client.core;

public record BootstrapInventoryItemPresentation(
    int slotIndex,
    int itemId,
    String name,
    int quantity,
    boolean stackable,
    boolean membersOnly,
    boolean noted
) {
}
