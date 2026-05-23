package com.veyrmoor.protocol.codec.shared;

import com.veyrmoor.protocol.bootstrap.BootstrapSkill;
import com.veyrmoor.protocol.io.PacketReader;
import com.veyrmoor.protocol.io.PacketWriter;

public final class SkillCodec {

  public static final SkillCodec INSTANCE = new SkillCodec();

  private SkillCodec() {
  }

  public void encode(PacketWriter out, BootstrapSkill skill) {
    out.writeByte(skill.skillId());
    out.writeByte(skill.currentLevel());
    out.writeInt(skill.experience());
  }

  public BootstrapSkill decode(PacketReader in) {
    return new BootstrapSkill(in.readUnsignedByte(), in.readUnsignedByte(), in.readInt());
  }
}
