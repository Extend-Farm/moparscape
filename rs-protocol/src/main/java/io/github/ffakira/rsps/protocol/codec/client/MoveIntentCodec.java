package io.github.ffakira.rsps.protocol.codec.client;

import io.github.ffakira.rsps.protocol.input.MoveIntentMessage;
import io.github.ffakira.rsps.protocol.PacketCodec;
import io.github.ffakira.rsps.protocol.PacketDirection;
import io.github.ffakira.rsps.protocol.ProtocolCodecException;
import io.github.ffakira.rsps.protocol.io.PacketReader;
import io.github.ffakira.rsps.protocol.io.PacketWriter;
import io.github.ffakira.rsps.protocol.opcode.ClientOpcodes;

public final class MoveIntentCodec implements PacketCodec<MoveIntentMessage> {

  private static final int MAX_DELTA = 2;

  @Override
  public int opcode() {
    return ClientOpcodes.MOVE_INTENT;
  }

  @Override
  public PacketDirection direction() {
    return PacketDirection.CLIENT_TO_SERVER;
  }

  @Override
  public Class<MoveIntentMessage> packetType() {
    return MoveIntentMessage.class;
  }

  @Override
  public void encode(PacketWriter out, MoveIntentMessage packet) {
    validateDelta(packet.deltaX(), packet.deltaY());
    out.writeByte(packet.deltaX());
    out.writeByte(packet.deltaY());
    out.writeMovementMode(packet.movementMode());
  }

  @Override
  public MoveIntentMessage decode(PacketReader in) {
    int deltaX = in.readSignedByte();
    int deltaY = in.readSignedByte();
    validateDelta(deltaX, deltaY);
    return new MoveIntentMessage(deltaX, deltaY, in.readMovementMode());
  }

  private static void validateDelta(int deltaX, int deltaY) {
    if (deltaX < -MAX_DELTA || deltaX > MAX_DELTA || deltaY < -MAX_DELTA || deltaY > MAX_DELTA) {
      throw new ProtocolCodecException("Invalid movement delta (%d, %d)".formatted(deltaX, deltaY));
    }
  }
}
