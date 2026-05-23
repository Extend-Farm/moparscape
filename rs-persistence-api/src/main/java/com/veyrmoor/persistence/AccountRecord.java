package com.veyrmoor.persistence;

import com.veyrmoor.model.AccountId;

public record AccountRecord(AccountId id, String username, String passwordHash) {
}
