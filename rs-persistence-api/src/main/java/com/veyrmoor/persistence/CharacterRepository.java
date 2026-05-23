package com.veyrmoor.persistence;

import com.veyrmoor.model.AccountId;
import java.util.Optional;

public interface CharacterRepository {

  Optional<CharacterSnapshot> loadByAccountId(AccountId accountId);

  CharacterSnapshot save(CharacterSnapshot characterSnapshot);
}
