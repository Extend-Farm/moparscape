package io.github.ffakira.rsps.protocol.codec.shared;

import io.github.ffakira.rsps.protocol.bootstrap.BootstrapAnimationProfile;
import io.github.ffakira.rsps.protocol.io.PacketReader;
import io.github.ffakira.rsps.protocol.io.PacketWriter;

public final class AnimationProfileCodec {

  public static final AnimationProfileCodec INSTANCE = new AnimationProfileCodec();

  private AnimationProfileCodec() {
  }

  public void encode(PacketWriter out, BootstrapAnimationProfile profile) {
    out.writeInt(profile.standSequenceId());
    out.writeInt(profile.standTurnSequenceId());
    out.writeInt(profile.walkSequenceId());
    out.writeInt(profile.turnAroundSequenceId());
    out.writeInt(profile.turnRightSequenceId());
    out.writeInt(profile.turnLeftSequenceId());
    out.writeInt(profile.runSequenceId());
  }

  public BootstrapAnimationProfile decode(PacketReader in) {
    return new BootstrapAnimationProfile(
        in.readInt(),
        in.readInt(),
        in.readInt(),
        in.readInt(),
        in.readInt(),
        in.readInt(),
        in.readInt()
    );
  }
}
