package com.veyrmoor.protocol.codec.server;

import com.veyrmoor.protocol.PacketCodec;
import com.veyrmoor.protocol.PacketDirection;
import com.veyrmoor.protocol.ProtocolLimits;
import com.veyrmoor.protocol.world.WorldSnapshotMessage;
import com.veyrmoor.protocol.codec.shared.WorldPointCodec;
import com.veyrmoor.protocol.io.PacketReader;
import com.veyrmoor.protocol.io.PacketWriter;
import com.veyrmoor.protocol.opcode.ServerOpcodes;

public final class WorldSnapshotCodec implements PacketCodec<WorldSnapshotMessage> {

  @Override
  public int opcode() {
    return ServerOpcodes.WORLD_SNAPSHOT;
  }

  @Override
  public PacketDirection direction() {
    return PacketDirection.SERVER_TO_CLIENT;
  }

  @Override
  public Class<WorldSnapshotMessage> packetType() {
    return WorldSnapshotMessage.class;
  }

  @Override
  public void encode(PacketWriter out, WorldSnapshotMessage packet) {
    out.writeString(packet.regionKey(), ProtocolLimits.REGION_KEY_BYTES);
    WorldPointCodec.INSTANCE.encode(out, packet.localPlayerPosition());
  }

  @Override
  public WorldSnapshotMessage decode(PacketReader in) {
    return new WorldSnapshotMessage(
        in.readString(ProtocolLimits.REGION_KEY_BYTES),
        WorldPointCodec.INSTANCE.decode(in)
    );
  }
}
