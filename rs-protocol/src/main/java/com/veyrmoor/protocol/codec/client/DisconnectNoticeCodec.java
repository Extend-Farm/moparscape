package com.veyrmoor.protocol.codec.client;

import com.veyrmoor.protocol.session.DisconnectNotice;
import com.veyrmoor.protocol.PacketCodec;
import com.veyrmoor.protocol.PacketDirection;
import com.veyrmoor.protocol.ProtocolLimits;
import com.veyrmoor.protocol.io.PacketReader;
import com.veyrmoor.protocol.io.PacketWriter;
import com.veyrmoor.protocol.opcode.ClientOpcodes;

public final class DisconnectNoticeCodec implements PacketCodec<DisconnectNotice> {

  @Override
  public int opcode() {
    return ClientOpcodes.DISCONNECT_NOTICE;
  }

  @Override
  public PacketDirection direction() {
    return PacketDirection.CLIENT_TO_SERVER;
  }

  @Override
  public Class<DisconnectNotice> packetType() {
    return DisconnectNotice.class;
  }

  @Override
  public void encode(PacketWriter out, DisconnectNotice packet) {
    out.writeString(packet.reason(), ProtocolLimits.DISCONNECT_REASON_BYTES);
  }

  @Override
  public DisconnectNotice decode(PacketReader in) {
    return new DisconnectNotice(in.readString(ProtocolLimits.DISCONNECT_REASON_BYTES));
  }
}
