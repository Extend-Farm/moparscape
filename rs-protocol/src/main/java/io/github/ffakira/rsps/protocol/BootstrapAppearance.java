package io.github.ffakira.rsps.protocol;

import java.util.List;

public record BootstrapAppearance(List<Integer> lookValues, BootstrapAnimationProfile animationProfile) {

  public BootstrapAppearance(List<Integer> lookValues) {
    this(lookValues, BootstrapAnimationProfile.referencePlayer());
  }

  public BootstrapAppearance {
    lookValues = List.copyOf(lookValues);
    animationProfile = animationProfile == null ? BootstrapAnimationProfile.referencePlayer() : animationProfile;
  }
}
