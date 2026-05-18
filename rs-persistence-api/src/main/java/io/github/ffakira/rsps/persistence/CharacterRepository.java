package io.github.ffakira.rsps.persistence;

import io.github.ffakira.rsps.model.AccountId;
import java.util.Optional;

public interface CharacterRepository {

  Optional<CharacterSnapshot> loadByAccountId(AccountId accountId);

  CharacterSnapshot save(CharacterSnapshot characterSnapshot);
}
