package com.veyrmoor.persistence.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public record SqlPersistenceConfiguration(String jdbcUrl, String username, String password) {

  public Connection openConnection() throws SQLException {
    return DriverManager.getConnection(jdbcUrl, username, password);
  }
}
