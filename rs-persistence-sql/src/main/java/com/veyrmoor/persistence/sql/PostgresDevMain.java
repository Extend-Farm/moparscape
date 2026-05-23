package com.veyrmoor.persistence.sql;

import java.sql.SQLException;

public final class PostgresDevMain {

  private PostgresDevMain() {
  }

  public static void main(String[] args) throws SQLException {
    SqlPersistenceConfiguration configuration = SqlPersistenceEnvironment.load();
    String command = args.length == 0 ? "migrate" : args[0];
    switch (command) {
      case "migrate" -> migrate(configuration);
      case "migrate-and-provision" -> migrateAndProvision(configuration);
      case "provision-account" -> provisionAccount(configuration);
      case "reset" -> reset(configuration);
      case "reset-and-provision" -> resetAndProvision(configuration);
      case "test-connection" -> testConnection(configuration);
      default -> throw new IllegalArgumentException("Unsupported database command: " + command);
    }
  }

  private static void migrate(SqlPersistenceConfiguration configuration) {
    var result = new FlywayMigrator().migrate(configuration);
    System.out.println(
        "Migrated PostgreSQL database at "
            + configuration.jdbcUrl()
            + " using user "
            + configuration.username()
            + " to version "
            + result.targetSchemaVersion
    );
  }

  private static void migrateAndProvision(SqlPersistenceConfiguration configuration) {
    migrate(configuration);
    printProvisioningResult(new PostgresAccountProvisioner(configuration).provision(SqlAccountProvisioningEnvironment.require()));
  }

  private static void provisionAccount(SqlPersistenceConfiguration configuration) {
    printProvisioningResult(new PostgresAccountProvisioner(configuration).provision(SqlAccountProvisioningEnvironment.require()));
  }

  private static void reset(SqlPersistenceConfiguration configuration) {
    var result = new FlywayMigrator().resetAndMigrate(configuration);
    System.out.println(
        "Reset and migrated PostgreSQL database at "
            + configuration.jdbcUrl()
            + " using user "
            + configuration.username()
            + " to version "
            + result.targetSchemaVersion
    );
  }

  private static void resetAndProvision(SqlPersistenceConfiguration configuration) {
    reset(configuration);
    printProvisioningResult(new PostgresAccountProvisioner(configuration).provision(SqlAccountProvisioningEnvironment.require()));
  }

  private static void testConnection(SqlPersistenceConfiguration configuration) throws SQLException {
    try (var connection = configuration.openConnection()) {
      String productName = connection.getMetaData().getDatabaseProductName();
      String databaseUrl = connection.getMetaData().getURL();
      System.out.println(
          "Connected to "
              + productName
              + " at "
              + databaseUrl
              + " as "
              + configuration.username()
      );
    }
  }

  private static void printProvisioningResult(AccountProvisioningResult result) {
    System.out.println(
        "Provisioned account '"
            + result.loginName()
            + "' (accountId="
            + result.accountId().value()
            + ", characterId="
            + result.characterId().value()
            + ", accountCreated="
            + result.accountCreated()
            + ", characterCreated="
            + result.characterCreated()
            + ", passwordUpdated="
            + result.passwordUpdated()
            + ")"
    );
  }
}
