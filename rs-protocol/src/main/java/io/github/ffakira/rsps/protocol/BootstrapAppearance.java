package io.github.ffakira.rsps.protocol;

import java.util.List;

public record BootstrapAppearance(List<Integer> lookValues) {

  public BootstrapAppearance {
    lookValues = List.copyOf(lookValues);
  }
}
