package com.veyrmoor.protocol;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.veyrmoor.protocol.codec.client.HandshakeRequestCodec;
import com.veyrmoor.protocol.codec.server.HandshakeAcceptedCodec;
import com.veyrmoor.protocol.io.PacketReader;
import com.veyrmoor.protocol.io.PacketWriter;
import com.veyrmoor.protocol.opcode.ClientOpcodes;
import com.veyrmoor.protocol.session.HandshakeRequest;
import com.veyrmoor.protocol.session.LoginRequest;
import org.junit.jupiter.api.Test;

class PacketCodecRegistryTest {

  @Test
  void createsAnAtomicRegistryAndResolvesByOpcodeAndPacketType() {
    PacketCodecRegistry registry = PacketCodecRegistry.create(
        new HandshakeRequestCodec(),
        new HandshakeAcceptedCodec()
    );

    assertThat(registry.require(PacketDirection.CLIENT_TO_SERVER, ClientOpcodes.HANDSHAKE_REQUEST))
        .isInstanceOf(HandshakeRequestCodec.class);
    assertThat(registry.require(HandshakeRequest.class))
        .isInstanceOf(HandshakeRequestCodec.class);
  }

  @Test
  void rejectsDuplicateOpcodesDuringCreation() {
    assertThatThrownBy(() -> PacketCodecRegistry.create(
        new HandshakeRequestCodec(),
        new DuplicateHandshakeOpcodeLoginRequestCodec()
    ))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Duplicate opcode");
  }

  @Test
  void rejectsDuplicatePacketTypesDuringCreation() {
    assertThatThrownBy(() -> PacketCodecRegistry.create(
        new HandshakeRequestCodec(),
        new AlternateHandshakeRequestCodec()
    ))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Duplicate packet codec");
  }

  private static final class DuplicateHandshakeOpcodeLoginRequestCodec implements PacketCodec<LoginRequest> {

    @Override
    public int opcode() {
      return ClientOpcodes.HANDSHAKE_REQUEST;
    }

    @Override
    public PacketDirection direction() {
      return PacketDirection.CLIENT_TO_SERVER;
    }

    @Override
    public Class<LoginRequest> packetType() {
      return LoginRequest.class;
    }

    @Override
    public void encode(PacketWriter out, LoginRequest packet) {
    }

    @Override
    public LoginRequest decode(PacketReader in) {
      return new LoginRequest("", "");
    }
  }

  private static final class AlternateHandshakeRequestCodec implements PacketCodec<HandshakeRequest> {

    @Override
    public int opcode() {
      return ClientOpcodes.LOGIN_REQUEST;
    }

    @Override
    public PacketDirection direction() {
      return PacketDirection.CLIENT_TO_SERVER;
    }

    @Override
    public Class<HandshakeRequest> packetType() {
      return HandshakeRequest.class;
    }

    @Override
    public void encode(PacketWriter out, HandshakeRequest packet) {
    }

    @Override
    public HandshakeRequest decode(PacketReader in) {
      return new HandshakeRequest(ProtocolVersion.current(), "");
    }
  }
}
