package com.veyrmoor.protocol.codec.shared;

import com.veyrmoor.protocol.bootstrap.BootstrapAppearance;
import com.veyrmoor.protocol.ProtocolLimits;
import com.veyrmoor.protocol.io.PacketReader;
import com.veyrmoor.protocol.io.PacketWriter;
import java.util.ArrayList;

public final class AppearanceCodec {

  public static final AppearanceCodec INSTANCE = new AppearanceCodec();

  private AppearanceCodec() {
  }

  public void encode(PacketWriter out, BootstrapAppearance appearance) {
    out.writeShort(appearance.lookValues().size());
    for (Integer lookValue : appearance.lookValues()) {
      out.writeInt(lookValue);
    }
    AnimationProfileCodec.INSTANCE.encode(out, appearance.animationProfile());
  }

  public BootstrapAppearance decode(PacketReader in) {
    int size = in.readListSize(ProtocolLimits.APPEARANCE_LOOK_VALUES);
    ArrayList<Integer> lookValues = new ArrayList<>(size);
    for (int index = 0; index < size; index++) {
      lookValues.add(in.readInt());
    }
    return new BootstrapAppearance(lookValues, AnimationProfileCodec.INSTANCE.decode(in));
  }
}
