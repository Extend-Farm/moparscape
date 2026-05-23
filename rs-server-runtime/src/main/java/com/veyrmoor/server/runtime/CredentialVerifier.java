package com.veyrmoor.server.runtime;

import com.veyrmoor.persistence.AccountRecord;
import com.veyrmoor.persistence.PasswordHashing;

@FunctionalInterface
public interface CredentialVerifier {

  boolean matches(AccountRecord accountRecord, String presentedPassword);

  static CredentialVerifier plainText() {
    return (accountRecord, presentedPassword) -> accountRecord.passwordHash().equals(presentedPassword);
  }

  static CredentialVerifier compatible() {
    return (accountRecord, presentedPassword) ->
        PasswordHashing.matches(accountRecord.passwordHash(), presentedPassword);
  }
}
