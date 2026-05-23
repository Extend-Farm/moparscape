package com.veyrmoor.model;

public record AccountId(long value) {

  public AccountId {
    if (value <= 0) {
      throw new IllegalArgumentException("Account id must be positive");
    }
  }
}
