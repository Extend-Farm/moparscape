package com.veyrmoor.persistence.sql;

public final class SqlPersistenceEnvironment {

  public static final String JDBC_URL_ENV = "RSPS_DB_JDBC_URL";
  public static final String USERNAME_ENV = "RSPS_DB_USERNAME";
  public static final String PASSWORD_ENV = "RSPS_DB_PASSWORD";
  public static final String PASSWORD_FILE_ENV = "RSPS_DB_PASSWORD_FILE";

  public static final String DEFAULT_JDBC_URL = "jdbc:postgresql://127.0.0.1:55432/moparscape";
  public static final String DEFAULT_USERNAME = "moparscape";
  public static final String DEFAULT_PASSWORD = "moparscape";

  private SqlPersistenceEnvironment() {
  }

  public static SqlPersistenceConfiguration load() {
    return new SqlPersistenceConfiguration(
        EnvironmentValueLoader.optionalOrDefault(JDBC_URL_ENV, DEFAULT_JDBC_URL),
        EnvironmentValueLoader.optionalOrDefault(USERNAME_ENV, DEFAULT_USERNAME),
        EnvironmentValueLoader.optionalOrDefault(PASSWORD_ENV, PASSWORD_FILE_ENV, DEFAULT_PASSWORD)
    );
  }
}
