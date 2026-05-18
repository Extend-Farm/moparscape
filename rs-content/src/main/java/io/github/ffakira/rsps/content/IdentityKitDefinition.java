package io.github.ffakira.rsps.content;

import java.util.List;

public record IdentityKitDefinition(
    int id,
    int bodyPartId,
    List<Integer> bodyModelIds,
    List<Integer> recolorSources,
    List<Integer> recolorTargets,
    boolean nonSelectable
) {

  public IdentityKitDefinition {
    bodyModelIds = List.copyOf(bodyModelIds);
    recolorSources = List.copyOf(recolorSources);
    recolorTargets = List.copyOf(recolorTargets);
  }
}
