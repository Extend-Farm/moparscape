package io.github.ffakira.rsps.protocol;

import io.github.ffakira.rsps.protocol.io.PacketFrameDecoder;
import io.github.ffakira.rsps.protocol.io.PacketFrameEncoder;
import io.github.ffakira.rsps.protocol.io.PacketReader;
import io.github.ffakira.rsps.protocol.io.PacketWriter;

public final class ProtocolBinaryCodec {

  private static final PacketCodecRegistry REGISTRY = ProtocolPacketCodecs.createRegistry();

  private ProtocolBinaryCodec() {
  }

  public static byte[] encodeClientMessage(ClientMessage message) {
    return encodePacket(message);
  }

  public static ClientMessage decodeClientMessage(byte[] encodedFrame) {
    return decodePacket(PacketDirection.CLIENT_TO_SERVER, encodedFrame, ClientMessage.class);
  }

  public static byte[] encodeServerMessage(ServerMessage message) {
    return encodePacket(message);
  }

  public static ServerMessage decodeServerMessage(byte[] encodedFrame) {
    return decodePacket(PacketDirection.SERVER_TO_CLIENT, encodedFrame, ServerMessage.class);
  }

  private static <T extends Packet> byte[] encodePacket(T packet) {
    PacketCodec<T> codec = requireCodec(packet);
    PacketWriter payloadWriter = new PacketWriter();
    codec.encode(payloadWriter, packet);
    return PacketFrameEncoder.encode(new PacketFrame(codec.opcode(), payloadWriter.toByteArray()));
  }

  private static <T extends Packet> T decodePacket(
      PacketDirection direction,
      byte[] encodedFrame,
      Class<T> expectedType
  ) {
    PacketFrame frame = PacketFrameDecoder.decode(encodedFrame);
    PacketCodec<? extends Packet> codec = REGISTRY.require(direction, frame.opcode());
    PacketReader payloadReader = new PacketReader(frame.payload());
    Packet packet = codec.decode(payloadReader);
    payloadReader.assertFullyRead();
    return expectedType.cast(packet);
  }

  @SuppressWarnings("unchecked")
  private static <T extends Packet> PacketCodec<T> requireCodec(T packet) {
    return REGISTRY.require((Class<T>) packet.getClass());
  }
}
