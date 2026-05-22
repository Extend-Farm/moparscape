package io.github.ffakira.rsps.protocol.codec.shared;

import io.github.ffakira.rsps.protocol.bootstrap.BootstrapItemSlot;
import io.github.ffakira.rsps.protocol.io.PacketReader;
import io.github.ffakira.rsps.protocol.io.PacketWriter;

public final class ItemSlotCodec {

  public static final ItemSlotCodec INSTANCE = new ItemSlotCodec();

  private ItemSlotCodec() {
  }

  public void encode(PacketWriter out, BootstrapItemSlot itemSlot) {
    out.writeByte(itemSlot.slotIndex());
    out.writeInt(itemSlot.itemId());
    out.writeInt(itemSlot.quantity());
  }

  public BootstrapItemSlot decode(PacketReader in) {
    return new BootstrapItemSlot(in.readUnsignedByte(), in.readInt(), in.readInt());
  }
}
