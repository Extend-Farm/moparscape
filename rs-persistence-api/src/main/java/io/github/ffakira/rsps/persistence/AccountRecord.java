package io.github.ffakira.rsps.persistence;

import io.github.ffakira.rsps.model.AccountId;

public record AccountRecord(AccountId id, String username, String passwordHash) {
}
