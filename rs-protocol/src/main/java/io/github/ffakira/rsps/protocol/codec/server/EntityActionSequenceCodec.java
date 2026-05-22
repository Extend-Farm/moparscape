package io.github.ffakira.rsps.protocol.codec.server;

import io.github.ffakira.rsps.protocol.world.EntityActionSequenceMessage;
import io.github.ffakira.rsps.protocol.PacketCodec;
import io.github.ffakira.rsps.protocol.PacketDirection;
import io.github.ffakira.rsps.protocol.io.PacketReader;
import io.github.ffakira.rsps.protocol.io.PacketWriter;
import io.github.ffakira.rsps.protocol.opcode.ServerOpcodes;

public final class EntityActionSequenceCodec implements PacketCodec<EntityActionSequenceMessage> {

  @Override
  public int opcode() {
    return ServerOpcodes.ENTITY_ACTION_SEQUENCE;
  }

  @Override
  public PacketDirection direction() {
    return PacketDirection.SERVER_TO_CLIENT;
  }

  @Override
  public Class<EntityActionSequenceMessage> packetType() {
    return EntityActionSequenceMessage.class;
  }

  @Override
  public void encode(PacketWriter out, EntityActionSequenceMessage packet) {
    out.writeInt(packet.entityId());
    out.writeInt(packet.actionSequenceId());
  }

  @Override
  public EntityActionSequenceMessage decode(PacketReader in) {
    return new EntityActionSequenceMessage(in.readInt(), in.readInt());
  }
}
