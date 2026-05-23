package com.veyrmoor.protocol.codec.shared;

import com.veyrmoor.protocol.bootstrap.BootstrapItemSlot;
import com.veyrmoor.protocol.io.PacketReader;
import com.veyrmoor.protocol.io.PacketWriter;

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
