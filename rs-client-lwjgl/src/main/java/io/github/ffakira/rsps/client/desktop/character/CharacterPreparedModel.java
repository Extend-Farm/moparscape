package io.github.ffakira.rsps.client.desktop.character;

import java.util.List;
import java.util.Objects;

record CharacterPreparedModel(
    List<CharacterModelSourceBuilder.PreparedContribution> preparedContributions,
    List<PreparedContributionLighting> preparedLighting,
    CharacterActorTransform actorTransform,
    CharacterActorBounds actorBounds
) {

  CharacterPreparedModel {
    preparedContributions = List.copyOf(Objects.requireNonNull(preparedContributions, "preparedContributions"));
    preparedLighting = List.copyOf(Objects.requireNonNull(preparedLighting, "preparedLighting"));
    actorTransform = Objects.requireNonNull(actorTransform, "actorTransform");
    actorBounds = Objects.requireNonNull(actorBounds, "actorBounds");
  }
}
