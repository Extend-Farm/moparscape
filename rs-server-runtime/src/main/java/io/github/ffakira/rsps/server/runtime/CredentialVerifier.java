package io.github.ffakira.rsps.server.runtime;

import io.github.ffakira.rsps.persistence.AccountRecord;
import io.github.ffakira.rsps.persistence.PasswordHashing;

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
