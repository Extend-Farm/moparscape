package io.github.ffakira.rsps.protocol.codec.client;

import io.github.ffakira.rsps.protocol.session.DisconnectNotice;
import io.github.ffakira.rsps.protocol.PacketCodec;
import io.github.ffakira.rsps.protocol.PacketDirection;
import io.github.ffakira.rsps.protocol.ProtocolLimits;
import io.github.ffakira.rsps.protocol.io.PacketReader;
import io.github.ffakira.rsps.protocol.io.PacketWriter;
import io.github.ffakira.rsps.protocol.opcode.ClientOpcodes;

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
