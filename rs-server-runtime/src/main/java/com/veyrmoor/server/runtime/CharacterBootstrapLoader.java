package com.veyrmoor.server.runtime;

import com.veyrmoor.persistence.AccountRecord;
import com.veyrmoor.persistence.CharacterRepository;
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
