package io.github.ffakira.rsps.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PasswordHashingTest {

  @Test
  void hashesAndVerifiesPassword() {
    String hash = PasswordHashing.hashPassword("password");

    assertThat(hash).startsWith("pbkdf2-sha256$");
    assertThat(PasswordHashing.matches(hash, "password")).isTrue();
    assertThat(PasswordHashing.matches(hash, "wrong-password")).isFalse();
  }

  @Test
  void stillAcceptsPlaintextDuringCompatibilityWindow() {
    assertThat(PasswordHashing.matches("password", "password")).isTrue();
    assertThat(PasswordHashing.matches("password", "wrong-password")).isFalse();
  }
}
