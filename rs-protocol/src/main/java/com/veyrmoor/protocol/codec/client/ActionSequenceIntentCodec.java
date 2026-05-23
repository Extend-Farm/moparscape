package com.veyrmoor.protocol.codec.client;

import com.veyrmoor.protocol.input.ActionSequenceIntentMessage;
import com.veyrmoor.protocol.PacketCodec;
import com.veyrmoor.protocol.PacketDirection;
import com.veyrmoor.protocol.io.PacketReader;
import com.veyrmoor.protocol.io.PacketWriter;
import com.veyrmoor.protocol.opcode.ClientOpcodes;

public final class ActionSequenceIntentCodec implements PacketCodec<ActionSequenceIntentMessage> {

  @Override
  public int opcode() {
    return ClientOpcodes.ACTION_SEQUENCE_INTENT;
  }

  @Override
  public PacketDirection direction() {
    return PacketDirection.CLIENT_TO_SERVER;
  }

  @Override
  public Class<ActionSequenceIntentMessage> packetType() {
    return ActionSequenceIntentMessage.class;
  }

  @Override
  public void encode(PacketWriter out, ActionSequenceIntentMessage packet) {
    out.writeInt(packet.actionSequenceId());
  }

  @Override
  public ActionSequenceIntentMessage decode(PacketReader in) {
    return new ActionSequenceIntentMessage(in.readInt());
  }
}
