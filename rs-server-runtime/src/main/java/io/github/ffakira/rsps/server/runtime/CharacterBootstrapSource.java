package io.github.ffakira.rsps.server.runtime;

import io.github.ffakira.rsps.persistence.AccountRecord;
import java.util.Optional;

public interface CharacterBootstrapSource {

  Optional<CharacterBootstrap> loadBootstrap(AccountRecord accountRecord);
}
