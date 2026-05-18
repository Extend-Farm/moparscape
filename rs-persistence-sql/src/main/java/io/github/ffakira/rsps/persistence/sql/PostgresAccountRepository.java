package io.github.ffakira.rsps.persistence.sql;

import io.github.ffakira.rsps.model.AccountId;
import io.github.ffakira.rsps.persistence.AccountRecord;
import io.github.ffakira.rsps.persistence.AccountRepository;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PostgresAccountRepository implements AccountRepository {

  private final SqlPersistenceConfiguration configuration;

  public PostgresAccountRepository(SqlPersistenceConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public Optional<AccountRecord> findByUsername(String username) {
    try (var connection = configuration.openConnection();
         PreparedStatement statement =
             connection.prepareStatement(
                 "select id, username, password_hash from accounts where username_normalized = lower(?)"
             )) {
      statement.setString(1, username);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (!resultSet.next()) {
          return Optional.empty();
        }
        return Optional.of(map(resultSet));
      }
    } catch (SQLException sqlException) {
      throw new IllegalStateException("Failed to load account " + username, sqlException);
    }
  }

  @Override
  public AccountRecord save(AccountRecord accountRecord) {
    try (var connection = configuration.openConnection();
         PreparedStatement statement = connection.prepareStatement(
             """
             insert into accounts (id, username, password_hash)
             values (?, ?, ?)
             on conflict (id) do update
             set username = excluded.username,
                 password_hash = excluded.password_hash
             """
         )) {
      statement.setLong(1, accountRecord.id().value());
      statement.setString(2, accountRecord.username());
      statement.setString(3, accountRecord.passwordHash());
      statement.executeUpdate();
      return accountRecord;
    } catch (SQLException sqlException) {
      throw new IllegalStateException("Failed to save account " + accountRecord.username(), sqlException);
    }
  }

  private AccountRecord map(ResultSet resultSet) throws SQLException {
    return new AccountRecord(
        new AccountId(resultSet.getLong("id")),
        resultSet.getString("username"),
        resultSet.getString("password_hash")
    );
  }
}
