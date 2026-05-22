package io.github.ffakira.rsps.protocol.codec.server;

import io.github.ffakira.rsps.protocol.world.EntityPositionMessage;
import io.github.ffakira.rsps.protocol.PacketCodec;
import io.github.ffakira.rsps.protocol.PacketDirection;
import io.github.ffakira.rsps.protocol.codec.shared.WorldPointCodec;
import io.github.ffakira.rsps.protocol.io.PacketReader;
import io.github.ffakira.rsps.protocol.io.PacketWriter;
import io.github.ffakira.rsps.protocol.opcode.ServerOpcodes;

public final class EntityPositionCodec implements PacketCodec<EntityPositionMessage> {

  @Override
  public int opcode() {
    return ServerOpcodes.ENTITY_POSITION;
  }

  @Override
  public PacketDirection direction() {
    return PacketDirection.SERVER_TO_CLIENT;
  }

  @Override
  public Class<EntityPositionMessage> packetType() {
    return EntityPositionMessage.class;
  }

  @Override
  public void encode(PacketWriter out, EntityPositionMessage packet) {
    out.writeInt(packet.entityId());
    WorldPointCodec.INSTANCE.encode(out, packet.worldPoint());
  }

  @Override
  public EntityPositionMessage decode(PacketReader in) {
    return new EntityPositionMessage(in.readInt(), WorldPointCodec.INSTANCE.decode(in));
  }
}
