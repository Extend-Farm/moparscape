package com.veyrmoor.server.runtime;

@FunctionalInterface
public interface LoginAccountProvisioner {

  boolean provision(String username, String password);

  static LoginAccountProvisioner disabled() {
    return (username, password) -> false;
  }
}
