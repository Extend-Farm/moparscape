package com.veyrmoor.persistence.sql;

import com.veyrmoor.model.AccountId;
import com.veyrmoor.persistence.AccountRecord;
import com.veyrmoor.persistence.AccountRepository;
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
                 """
                 select a.id, a.login_name, c.password_hash
                 from accounts a
                 join account_credentials c on c.account_id = a.id
                 where a.login_name_key = ?
                 """
             )) {
      statement.setString(1, SqlNamePolicy.accountLoginKey(username));
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
    String loginName = SqlNamePolicy.accountLoginName(accountRecord.username());
    String loginNameKey = SqlNamePolicy.accountLoginKey(loginName);
    if (accountRecord.passwordHash() == null || accountRecord.passwordHash().isBlank()) {
      throw new IllegalArgumentException("password hash cannot be blank");
    }

    try (var connection = configuration.openConnection()) {
      connection.setAutoCommit(false);
      try (PreparedStatement accountStatement = connection.prepareStatement(
          """
          insert into accounts (id, login_name, login_name_key)
          values (?, ?, ?)
          on conflict (id) do update
          set login_name = excluded.login_name,
              login_name_key = excluded.login_name_key,
              updated_at = now()
          """
      );
           PreparedStatement credentialStatement = connection.prepareStatement(
               """
               insert into account_credentials (account_id, password_hash)
               values (?, ?)
               on conflict (account_id) do update
               set password_hash = excluded.password_hash,
                   password_updated_at = now()
               """
           )) {
        accountStatement.setLong(1, accountRecord.id().value());
        accountStatement.setString(2, loginName);
        accountStatement.setString(3, loginNameKey);
        accountStatement.executeUpdate();

        credentialStatement.setLong(1, accountRecord.id().value());
        credentialStatement.setString(2, accountRecord.passwordHash());
        credentialStatement.executeUpdate();

        connection.commit();
      } catch (SQLException sqlException) {
        connection.rollback();
        throw sqlException;
      } finally {
        connection.setAutoCommit(true);
      }
      return new AccountRecord(accountRecord.id(), loginName, accountRecord.passwordHash());
    } catch (SQLException sqlException) {
      throw new IllegalStateException("Failed to save account " + accountRecord.username(), sqlException);
    }
  }

  private AccountRecord map(ResultSet resultSet) throws SQLException {
    return new AccountRecord(
        new AccountId(resultSet.getLong("id")),
        resultSet.getString("login_name"),
        resultSet.getString("password_hash")
    );
  }
}
