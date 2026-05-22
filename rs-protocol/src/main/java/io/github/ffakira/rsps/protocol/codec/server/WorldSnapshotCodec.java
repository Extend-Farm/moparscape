package io.github.ffakira.rsps.protocol.codec.server;

import io.github.ffakira.rsps.protocol.PacketCodec;
import io.github.ffakira.rsps.protocol.PacketDirection;
import io.github.ffakira.rsps.protocol.ProtocolLimits;
import io.github.ffakira.rsps.protocol.world.WorldSnapshotMessage;
import io.github.ffakira.rsps.protocol.codec.shared.WorldPointCodec;
import io.github.ffakira.rsps.protocol.io.PacketReader;
import io.github.ffakira.rsps.protocol.io.PacketWriter;
import io.github.ffakira.rsps.protocol.opcode.ServerOpcodes;

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
