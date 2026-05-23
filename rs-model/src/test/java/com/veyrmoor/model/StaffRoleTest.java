package com.veyrmoor.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class StaffRoleTest {

  @Test
  void mapsExactIdsToTheExpectedStaffRolesAndCrowns() {
    assertThat(StaffRole.fromId(0)).isEqualTo(StaffRole.NONE);
    assertThat(StaffRole.fromId(1)).isEqualTo(StaffRole.MODERATOR);
    assertThat(StaffRole.fromId(2)).isEqualTo(StaffRole.ADMIN);
    assertThat(StaffRole.fromId(3)).isEqualTo(StaffRole.NONE);

    assertThat(StaffRole.NONE.hasCrown()).isFalse();
    assertThat(StaffRole.MODERATOR.crownSpriteIndex()).isZero();
    assertThat(StaffRole.ADMIN.crownSpriteIndex()).isEqualTo(1);
  }
}
