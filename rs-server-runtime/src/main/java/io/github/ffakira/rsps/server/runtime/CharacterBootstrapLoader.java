package io.github.ffakira.rsps.server.runtime;

import io.github.ffakira.rsps.persistence.AccountRecord;
import io.github.ffakira.rsps.persistence.CharacterRepository;
import java.util.Optional;

public final class CharacterBootstrapLoader {

  private final CharacterRepository characterRepository;

  public CharacterBootstrapLoader(CharacterRepository characterRepository) {
    this.characterRepository = characterRepository;
  }

  public Optional<CharacterBootstrap> load(AccountRecord accountRecord) {
    if (characterRepository instanceof CharacterBootstrapSource characterBootstrapSource) {
      return characterBootstrapSource.loadBootstrap(accountRecord);
    }
    return characterRepository.loadByAccountId(accountRecord.id())
        .map(characterSnapshot -> CharacterBootstrap.minimal(accountRecord, characterSnapshot));
  }
}
