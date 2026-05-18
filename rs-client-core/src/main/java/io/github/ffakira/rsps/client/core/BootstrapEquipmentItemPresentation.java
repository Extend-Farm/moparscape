package io.github.ffakira.rsps.client.core;

public record BootstrapEquipmentItemPresentation(
    int slotIndex,
    String slotName,
    int itemId,
    String name,
    int quantity,
    boolean stackable,
    boolean membersOnly,
    boolean noted
) {
}
