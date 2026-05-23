package com.veyrmoor.server.runtime;

import com.veyrmoor.persistence.AccountRecord;
import java.util.Optional;

public interface CharacterBootstrapSource {

  Optional<CharacterBootstrap> loadBootstrap(AccountRecord accountRecord);
}
