package com.veyrmoor.protocol.codec.client;

import com.veyrmoor.protocol.PacketCodec;
import com.veyrmoor.protocol.PacketDirection;
import com.veyrmoor.protocol.ProtocolLimits;
import com.veyrmoor.protocol.input.PublicChatIntentMessage;
import com.veyrmoor.protocol.io.PacketReader;
import com.veyrmoor.protocol.io.PacketWriter;
import com.veyrmoor.protocol.opcode.ClientOpcodes;

public final class PublicChatIntentCodec implements PacketCodec<PublicChatIntentMessage> {

  @Override
  public int opcode() {
    return ClientOpcodes.PUBLIC_CHAT_INTENT;
  }

  @Override
  public PacketDirection direction() {
    return PacketDirection.CLIENT_TO_SERVER;
  }

  @Override
  public Class<PublicChatIntentMessage> packetType() {
    return PublicChatIntentMessage.class;
  }

  @Override
  public void encode(PacketWriter out, PublicChatIntentMessage packet) {
    out.writeString(packet.text(), ProtocolLimits.PUBLIC_CHAT_TEXT_BYTES);
  }

  @Override
  public PublicChatIntentMessage decode(PacketReader in) {
    return new PublicChatIntentMessage(in.readString(ProtocolLimits.PUBLIC_CHAT_TEXT_BYTES));
  }
}
