package com.veyrmoor.protocol.codec.server;

import com.veyrmoor.protocol.PacketCodec;
import com.veyrmoor.protocol.PacketDirection;
import com.veyrmoor.protocol.ProtocolLimits;
import com.veyrmoor.protocol.chat.PublicChatMessage;
import com.veyrmoor.protocol.io.PacketReader;
import com.veyrmoor.protocol.io.PacketWriter;
import com.veyrmoor.protocol.opcode.ServerOpcodes;

public final class PublicChatCodec implements PacketCodec<PublicChatMessage> {

  @Override
  public int opcode() {
    return ServerOpcodes.PUBLIC_CHAT;
  }

  @Override
  public PacketDirection direction() {
    return PacketDirection.SERVER_TO_CLIENT;
  }

  @Override
  public Class<PublicChatMessage> packetType() {
    return PublicChatMessage.class;
  }

  @Override
  public void encode(PacketWriter out, PublicChatMessage packet) {
    out.writeString(packet.speakerDisplayName(), ProtocolLimits.DISPLAY_NAME_BYTES);
    out.writeString(packet.text(), ProtocolLimits.PUBLIC_CHAT_TEXT_BYTES);
  }

  @Override
  public PublicChatMessage decode(PacketReader in) {
    return new PublicChatMessage(
        in.readString(ProtocolLimits.DISPLAY_NAME_BYTES),
        in.readString(ProtocolLimits.PUBLIC_CHAT_TEXT_BYTES)
    );
  }
}
