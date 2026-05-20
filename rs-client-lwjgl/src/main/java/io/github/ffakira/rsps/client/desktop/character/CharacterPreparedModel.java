package io.github.ffakira.rsps.client.desktop.character;

import java.util.List;

record CharacterPreparedModel(
    List<CharacterModelSourceBuilder.PreparedContribution> preparedContributions,
    List<PreparedContributionLighting> preparedLighting,
    CharacterActorTransform actorTransform,
    CharacterActorBounds actorBounds
) {

  CharacterPreparedModel {
    preparedContributions = List.copyOf(preparedContributions);
    preparedLighting = List.copyOf(preparedLighting);
  }
}
