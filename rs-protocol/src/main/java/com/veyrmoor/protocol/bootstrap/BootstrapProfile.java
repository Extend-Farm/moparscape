package com.veyrmoor.protocol.bootstrap;

import com.veyrmoor.model.StaffRole;

public record BootstrapProfile(StaffRole staffRole, boolean member, int runEnergy) {

  public BootstrapProfile {
    staffRole = staffRole == null ? StaffRole.NONE : staffRole;
  }
}
