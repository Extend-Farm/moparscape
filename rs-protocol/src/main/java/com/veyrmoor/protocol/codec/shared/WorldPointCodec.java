package com.veyrmoor.protocol.codec.shared;

import com.veyrmoor.model.WorldPoint;
import com.veyrmoor.protocol.io.PacketReader;
import com.veyrmoor.protocol.io.PacketWriter;

public final class WorldPointCodec {

  public static final WorldPointCodec INSTANCE = new WorldPointCodec();

  private WorldPointCodec() {
  }

  public void encode(PacketWriter out, WorldPoint worldPoint) {
    out.writeShort(worldPoint.x());
    out.writeShort(worldPoint.y());
    out.writeByte(worldPoint.plane());
  }

  public WorldPoint decode(PacketReader in) {
    return new WorldPoint(in.readUnsignedShort(), in.readUnsignedShort(), in.readUnsignedByte());
  }
}
