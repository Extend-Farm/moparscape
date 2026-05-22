package io.github.ffakira.rsps.protocol.codec.shared;

import io.github.ffakira.rsps.protocol.bootstrap.BootstrapAppearance;
import io.github.ffakira.rsps.protocol.ProtocolLimits;
import io.github.ffakira.rsps.protocol.io.PacketReader;
import io.github.ffakira.rsps.protocol.io.PacketWriter;
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
