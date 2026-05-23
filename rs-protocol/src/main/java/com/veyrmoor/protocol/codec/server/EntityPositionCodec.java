package com.veyrmoor.protocol.codec.server;

import com.veyrmoor.protocol.world.EntityPositionMessage;
import com.veyrmoor.protocol.PacketCodec;
import com.veyrmoor.protocol.PacketDirection;
import com.veyrmoor.protocol.codec.shared.WorldPointCodec;
import com.veyrmoor.protocol.io.PacketReader;
import com.veyrmoor.protocol.io.PacketWriter;
import com.veyrmoor.protocol.opcode.ServerOpcodes;

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
