package io.github.ffakira.rsps.persistence.sql;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;

public class FlywayMigrator {

  public MigrateResult migrate(SqlPersistenceConfiguration configuration) {
    Flyway flyway = Flyway.configure()
        .dataSource(configuration.jdbcUrl(), configuration.username(), configuration.password())
        .load();
    return flyway.migrate();
  }
}
