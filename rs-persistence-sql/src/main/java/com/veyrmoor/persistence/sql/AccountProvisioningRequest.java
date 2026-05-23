package com.veyrmoor.persistence.sql;

record AccountProvisioningRequest(
    String loginName,
    String password,
    String characterDisplayName
) {

  AccountProvisioningRequest {
    loginName = SqlNamePolicy.accountLoginName(loginName);
    if (password == null || password.isBlank()) {
      throw new IllegalArgumentException("account password cannot be blank");
    }
    characterDisplayName = characterDisplayName == null || characterDisplayName.isBlank()
        ? SqlNamePolicy.characterDisplayName(loginName)
        : SqlNamePolicy.characterDisplayName(characterDisplayName);
  }
}
