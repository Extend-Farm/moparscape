package io.github.ffakira.rsps.protocol;

import java.util.HashMap;
import java.util.Map;

public final class PacketCodecRegistry {

  private final Map<Integer, PacketCodec<? extends Packet>> clientToServer;
  private final Map<Integer, PacketCodec<? extends Packet>> serverToClient;
  private final Map<Class<?>, PacketCodec<? extends Packet>> codecsByType;

  private PacketCodecRegistry(
      Map<Integer, PacketCodec<? extends Packet>> clientToServer,
      Map<Integer, PacketCodec<? extends Packet>> serverToClient,
      Map<Class<?>, PacketCodec<? extends Packet>> codecsByType
  ) {
    this.clientToServer = Map.copyOf(clientToServer);
    this.serverToClient = Map.copyOf(serverToClient);
    this.codecsByType = Map.copyOf(codecsByType);
  }

  @SafeVarargs
  public static PacketCodecRegistry create(PacketCodec<? extends Packet>... codecs) {
    Map<Integer, PacketCodec<? extends Packet>> clientToServer = new HashMap<>(codecs.length);
    Map<Integer, PacketCodec<? extends Packet>> serverToClient = new HashMap<>(codecs.length);
    Map<Class<?>, PacketCodec<? extends Packet>> codecsByType = new HashMap<>(codecs.length);
    for (PacketCodec<? extends Packet> codec : codecs) {
      registerCodec(clientToServer, serverToClient, codecsByType, codec);
    }
    return new PacketCodecRegistry(clientToServer, serverToClient, codecsByType);
  }

  private static void registerCodec(
      Map<Integer, PacketCodec<? extends Packet>> clientToServer,
      Map<Integer, PacketCodec<? extends Packet>> serverToClient,
      Map<Class<?>, PacketCodec<? extends Packet>> codecsByType,
      PacketCodec<? extends Packet> codec
  ) {
    Map<Integer, PacketCodec<? extends Packet>> target = switch (codec.direction()) {
      case CLIENT_TO_SERVER -> clientToServer;
      case SERVER_TO_CLIENT -> serverToClient;
    };
    PacketCodec<? extends Packet> previous = target.putIfAbsent(codec.opcode(), codec);
    if (previous != null) {
      throw new IllegalStateException("Duplicate opcode %d for %s and %s".formatted(
          codec.opcode(),
          previous.packetType().getName(),
          codec.packetType().getName()
      ));
    }
    PacketCodec<? extends Packet> previousType = codecsByType.putIfAbsent(codec.packetType(), codec);
    if (previousType != null) {
      throw new IllegalStateException("Duplicate packet codec for " + codec.packetType().getName());
    }
  }

  @SuppressWarnings("unchecked")
  public <T extends Packet> PacketCodec<T> require(PacketDirection direction, int opcode) {
    PacketCodec<? extends Packet> codec = switch (direction) {
      case CLIENT_TO_SERVER -> clientToServer.get(opcode);
      case SERVER_TO_CLIENT -> serverToClient.get(opcode);
    };
    if (codec == null) {
      throw new ProtocolCodecException("Unknown opcode %d for %s".formatted(opcode, direction));
    }
    return (PacketCodec<T>) codec;
  }

  @SuppressWarnings("unchecked")
  public <T extends Packet> PacketCodec<T> require(Class<T> packetType) {
    PacketCodec<? extends Packet> codec = codecsByType.get(packetType);
    if (codec == null) {
      throw new ProtocolCodecException("Unknown packet type " + packetType.getName());
    }
    return (PacketCodec<T>) codec;
  }
}
