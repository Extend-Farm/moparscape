package com.veyrmoor.persistence.sql;

import com.veyrmoor.model.AccountId;
import com.veyrmoor.model.CharacterId;

record AccountProvisioningResult(
    AccountId accountId,
    CharacterId characterId,
    String loginName,
    String characterDisplayName,
    boolean accountCreated,
    boolean characterCreated,
    boolean passwordUpdated
) {
}
