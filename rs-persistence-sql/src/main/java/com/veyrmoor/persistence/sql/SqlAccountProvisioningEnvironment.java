package com.veyrmoor.persistence.sql;

final class SqlAccountProvisioningEnvironment {

  static final String ACCOUNT_USERNAME_ENV = "RSPS_BOOTSTRAP_ACCOUNT_USERNAME";
  static final String ACCOUNT_PASSWORD_ENV = "RSPS_BOOTSTRAP_ACCOUNT_PASSWORD";
  static final String ACCOUNT_PASSWORD_FILE_ENV = "RSPS_BOOTSTRAP_ACCOUNT_PASSWORD_FILE";
  static final String CHARACTER_DISPLAY_NAME_ENV = "RSPS_BOOTSTRAP_CHARACTER_NAME";

  private SqlAccountProvisioningEnvironment() {
  }

  static AccountProvisioningRequest require() {
    return new AccountProvisioningRequest(
        EnvironmentValueLoader.require(ACCOUNT_USERNAME_ENV),
        EnvironmentValueLoader.require(ACCOUNT_PASSWORD_ENV, ACCOUNT_PASSWORD_FILE_ENV),
        System.getenv(CHARACTER_DISPLAY_NAME_ENV)
    );
  }
}
