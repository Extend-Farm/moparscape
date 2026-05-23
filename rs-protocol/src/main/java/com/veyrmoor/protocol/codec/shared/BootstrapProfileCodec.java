package com.veyrmoor.protocol.codec.shared;

import com.veyrmoor.protocol.bootstrap.BootstrapProfile;
import com.veyrmoor.protocol.io.PacketReader;
import com.veyrmoor.protocol.io.PacketWriter;

public final class BootstrapProfileCodec {

  public static final BootstrapProfileCodec INSTANCE = new BootstrapProfileCodec();

  private BootstrapProfileCodec() {
  }

  public void encode(PacketWriter out, BootstrapProfile profile) {
    out.writeShort(profile.rights());
    out.writeBoolean(profile.member());
    out.writeByte(profile.runEnergy());
  }

  public BootstrapProfile decode(PacketReader in) {
    return new BootstrapProfile((short) in.readUnsignedShort(), in.readBoolean(), in.readUnsignedByte());
  }
}
