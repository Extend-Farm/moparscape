package com.veyrmoor.persistence;

import java.util.Optional;

public interface AccountRepository {

  Optional<AccountRecord> findByUsername(String username);

  AccountRecord save(AccountRecord accountRecord);
}
