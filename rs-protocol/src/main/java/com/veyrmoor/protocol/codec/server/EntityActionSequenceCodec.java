package com.veyrmoor.protocol.codec.server;

import com.veyrmoor.protocol.world.EntityActionSequenceMessage;
import com.veyrmoor.protocol.PacketCodec;
import com.veyrmoor.protocol.PacketDirection;
import com.veyrmoor.protocol.io.PacketReader;
import com.veyrmoor.protocol.io.PacketWriter;
import com.veyrmoor.protocol.opcode.ServerOpcodes;

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
