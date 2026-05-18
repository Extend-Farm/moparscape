package io.github.ffakira.rsps.persistence.sql;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

public final class PostgresDevMain {

  private PostgresDevMain() {
  }

  public static void main(String[] args) throws SQLException {
    SqlPersistenceConfiguration configuration = SqlPersistenceEnvironment.load();
    String command = args.length == 0 ? "migrate" : args[0];
    switch (command) {
      case "migrate" -> migrate(configuration);
      case "test-connection" -> testConnection(configuration);
      case "import-character" -> importCharacter(configuration, args);
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

  private static void importCharacter(SqlPersistenceConfiguration configuration, String[] args) {
    if (args.length < 2 || args[1].isBlank()) {
      throw new IllegalArgumentException("Missing character name. Usage: import-character <username>");
    }
    String username = args[1];
    new FlywayMigrator().migrate(configuration);
    new PostgresCharacterFileImporter()
        .importCharacter(configuration, resolveCharactersDirectory(Path.of(".").toAbsolutePath().normalize()), username);
    System.out.println("Imported character '" + username + "' into PostgreSQL.");
  }

  private static Path resolveCharactersDirectory(Path workingDirectory) {
    Path[] candidates = {
        workingDirectory.resolve("client/characters"),
        workingDirectory.resolve("../client/characters").normalize(),
        workingDirectory.resolve("../../client/characters").normalize()
    };
    for (Path candidate : candidates) {
      if (Files.isDirectory(candidate)) {
        return candidate;
      }
    }
    throw new IllegalStateException("Unable to locate client/characters from " + workingDirectory);
  }
}
