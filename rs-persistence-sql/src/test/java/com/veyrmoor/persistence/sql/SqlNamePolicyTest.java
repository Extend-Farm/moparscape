package com.veyrmoor.persistence.sql;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SqlNamePolicyTest {

  @Test
  void canonicalizesCharacterDisplayNameCase() {
    assertThat(SqlNamePolicy.characterDisplayName("akira")).isEqualTo("Akira");
    assertThat(SqlNamePolicy.characterDisplayName("john doe")).isEqualTo("John Doe");
    assertThat(SqlNamePolicy.characterDisplayName("o'neill")).isEqualTo("O'Neill");
  }
}
