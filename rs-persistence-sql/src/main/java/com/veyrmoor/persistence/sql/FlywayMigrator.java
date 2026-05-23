package com.veyrmoor.persistence.sql;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;

public class FlywayMigrator {

  public MigrateResult migrate(SqlPersistenceConfiguration configuration) {
    return flyway(configuration, false).migrate();
  }

  public MigrateResult resetAndMigrate(SqlPersistenceConfiguration configuration) {
    Flyway flyway = flyway(configuration, true);
    flyway.clean();
    return flyway.migrate();
  }

  private Flyway flyway(SqlPersistenceConfiguration configuration, boolean cleanEnabled) {
    return Flyway.configure()
        .dataSource(configuration.jdbcUrl(), configuration.username(), configuration.password())
        .cleanDisabled(!cleanEnabled)
        .load();
  }
}
