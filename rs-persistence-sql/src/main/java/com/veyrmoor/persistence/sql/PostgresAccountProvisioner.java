package com.veyrmoor.persistence.sql;

import com.veyrmoor.model.AccountId;
import com.veyrmoor.model.CharacterId;
import com.veyrmoor.model.WorldPoint;
import com.veyrmoor.persistence.CharacterAppearance;
import com.veyrmoor.persistence.CharacterProfile;
import com.veyrmoor.persistence.CharacterSkill;
import com.veyrmoor.persistence.PasswordHashing;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class PostgresAccountProvisioner {

  private static final WorldPoint DEFAULT_START_POINT = new WorldPoint(3232, 3218, 0);
  private static final int DEFAULT_SKILL_COUNT = 21;
  private static final int HITPOINTS_SKILL_ID = 3;
  private static final int DEFAULT_HITPOINTS_LEVEL = 10;

  private final SqlPersistenceConfiguration configuration;

  public PostgresAccountProvisioner(SqlPersistenceConfiguration configuration) {
    this.configuration = configuration;
  }

  public boolean provisionForLogin(String loginName, String password) {
    AccountProvisioningResult result = provision(new AccountProvisioningRequest(loginName, password, loginName));
    return result.accountCreated() || result.characterCreated();
  }

  public AccountProvisioningResult provision(AccountProvisioningRequest request) {
    try (Connection connection = configuration.openConnection()) {
      connection.setAutoCommit(false);
      try {
        ExistingAccount existingAccount = findAccount(connection, request.loginName());
        AccountId accountId;
        CharacterId characterId;
        boolean accountCreated = false;
        boolean characterCreated = false;
        boolean passwordUpdated = false;

        if (existingAccount == null) {
          accountId = nextAccountId(connection);
          insertAccount(connection, accountId, request.loginName());
          insertOrUpdatePassword(connection, accountId, PasswordHashing.hashPassword(request.password()));
          accountCreated = true;
          passwordUpdated = true;
        } else {
          accountId = existingAccount.id();
          updateAccountLoginName(connection, accountId, request.loginName());
          if (!PasswordHashing.matches(existingAccount.passwordHash(), request.password())) {
            insertOrUpdatePassword(connection, accountId, PasswordHashing.hashPassword(request.password()));
            passwordUpdated = true;
          }
        }

        CharacterId existingCharacterId = findCharacterId(connection, accountId);
        if (existingCharacterId == null) {
          characterId = nextCharacterId(connection);
          insertCharacterShell(connection, characterId, accountId, request.characterDisplayName());
          insertDefaultCharacterState(connection, characterId);
          characterCreated = true;
        } else {
          characterId = existingCharacterId;
        }

        connection.commit();
        return new AccountProvisioningResult(
            accountId,
            characterId,
            request.loginName(),
            request.characterDisplayName(),
            accountCreated,
            characterCreated,
            passwordUpdated
        );
      } catch (SQLException sqlException) {
        connection.rollback();
        throw sqlException;
      } finally {
        connection.setAutoCommit(true);
      }
    } catch (SQLException sqlException) {
      throw new IllegalStateException("Failed to provision account " + request.loginName(), sqlException);
    }
  }

  private ExistingAccount findAccount(Connection connection, String loginName) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        """
        select a.id, a.login_name, c.password_hash
        from accounts a
        left join account_credentials c on c.account_id = a.id
        where a.login_name_key = ?
        """
    )) {
      statement.setString(1, SqlNamePolicy.accountLoginKey(loginName));
      try (ResultSet resultSet = statement.executeQuery()) {
        if (!resultSet.next()) {
          return null;
        }
        return new ExistingAccount(
            new AccountId(resultSet.getLong("id")),
            resultSet.getString("login_name"),
            resultSet.getString("password_hash")
        );
      }
    }
  }

  private CharacterId findCharacterId(Connection connection, AccountId accountId) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        "select id from characters where account_id = ?"
    )) {
      statement.setLong(1, accountId.value());
      try (ResultSet resultSet = statement.executeQuery()) {
        if (!resultSet.next()) {
          return null;
        }
        return new CharacterId(resultSet.getLong("id"));
      }
    }
  }

  private AccountId nextAccountId(Connection connection) throws SQLException {
    return new AccountId(nextSequenceValue(connection, "account_id_seq"));
  }

  private CharacterId nextCharacterId(Connection connection) throws SQLException {
    return new CharacterId(nextSequenceValue(connection, "character_id_seq"));
  }

  private long nextSequenceValue(Connection connection, String sequenceName) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        "select nextval(cast(? as regclass))"
    )) {
      statement.setString(1, sequenceName);
      try (ResultSet resultSet = statement.executeQuery()) {
        resultSet.next();
        return resultSet.getLong(1);
      }
    }
  }

  private void insertAccount(Connection connection, AccountId accountId, String loginName) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        """
        insert into accounts (id, login_name, login_name_key)
        values (?, ?, ?)
        """
    )) {
      statement.setLong(1, accountId.value());
      statement.setString(2, loginName);
      statement.setString(3, SqlNamePolicy.accountLoginKey(loginName));
      statement.executeUpdate();
    }
  }

  private void updateAccountLoginName(Connection connection, AccountId accountId, String loginName) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        """
        update accounts
        set login_name = ?,
            login_name_key = ?,
            updated_at = now()
        where id = ?
        """
    )) {
      statement.setString(1, loginName);
      statement.setString(2, SqlNamePolicy.accountLoginKey(loginName));
      statement.setLong(3, accountId.value());
      statement.executeUpdate();
    }
  }

  private void insertOrUpdatePassword(Connection connection, AccountId accountId, String passwordHash) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        """
        insert into account_credentials (account_id, password_hash)
        values (?, ?)
        on conflict (account_id) do update
        set password_hash = excluded.password_hash,
            password_updated_at = now()
        """
    )) {
      statement.setLong(1, accountId.value());
      statement.setString(2, passwordHash);
      statement.executeUpdate();
    }
  }

  private void insertCharacterShell(
      Connection connection,
      CharacterId characterId,
      AccountId accountId,
      String displayName
  ) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        """
        insert into characters (id, account_id, display_name, display_name_key)
        values (?, ?, ?, ?)
        """
    )) {
      statement.setLong(1, characterId.value());
      statement.setLong(2, accountId.value());
      statement.setString(3, displayName);
      statement.setString(4, SqlNamePolicy.characterDisplayNameKey(displayName));
      statement.executeUpdate();
    }
  }

  private void insertDefaultCharacterState(Connection connection, CharacterId characterId) throws SQLException {
    insertPosition(connection, characterId, DEFAULT_START_POINT);
    insertProfile(connection, characterId, CharacterProfile.defaults());
    insertAppearance(connection, characterId, CharacterAppearance.defaults());
    insertSkills(connection, characterId, defaultSkills());
  }

  private void insertPosition(Connection connection, CharacterId characterId, WorldPoint worldPoint) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        """
        insert into character_positions (character_id, world_x, world_y, plane)
        values (?, ?, ?, ?)
        """
    )) {
      statement.setLong(1, characterId.value());
      statement.setInt(2, worldPoint.x());
      statement.setInt(3, worldPoint.y());
      statement.setInt(4, worldPoint.plane());
      statement.executeUpdate();
    }
  }

  private void insertProfile(Connection connection, CharacterId characterId, CharacterProfile profile) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        """
        insert into character_profiles
          (character_id, rights, is_member, run_energy, last_login_day, game_time_counter, game_count_counter)
        values (?, ?, ?, ?, ?, ?, ?)
      """
    )) {
      statement.setLong(1, characterId.value());
      statement.setShort(2, (short) profile.staffRole().id());
      statement.setBoolean(3, profile.member());
      statement.setInt(4, profile.runEnergy());
      statement.setObject(5, profile.lastLoginDay());
      statement.setLong(6, profile.gameTimeCounter());
      statement.setLong(7, profile.gameCountCounter());
      statement.executeUpdate();
    }
  }

  private void insertAppearance(Connection connection, CharacterId characterId, CharacterAppearance appearance) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        """
        insert into character_appearance_slots (character_id, slot_index, look_value)
        values (?, ?, ?)
        """
    )) {
      for (int slotIndex = 0; slotIndex < appearance.lookValues().size(); slotIndex++) {
        statement.setLong(1, characterId.value());
        statement.setShort(2, (short) slotIndex);
        statement.setInt(3, appearance.lookValues().get(slotIndex));
        statement.addBatch();
      }
      statement.executeBatch();
    }
  }

  private void insertSkills(Connection connection, CharacterId characterId, List<CharacterSkill> skills) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        """
        insert into character_skills (character_id, skill_id, current_level, experience)
        values (?, ?, ?, ?)
        """
    )) {
      for (CharacterSkill skill : skills) {
        statement.setLong(1, characterId.value());
        statement.setShort(2, (short) skill.skillId());
        statement.setShort(3, (short) skill.currentLevel());
        statement.setInt(4, skill.experience());
        statement.addBatch();
      }
      statement.executeBatch();
    }
  }

  private List<CharacterSkill> defaultSkills() {
    ArrayList<CharacterSkill> skills = new ArrayList<>(DEFAULT_SKILL_COUNT);
    for (int skillId = 0; skillId < DEFAULT_SKILL_COUNT; skillId++) {
      int currentLevel = skillId == HITPOINTS_SKILL_ID ? DEFAULT_HITPOINTS_LEVEL : 1;
      skills.add(new CharacterSkill(skillId, currentLevel, experienceForLevel(currentLevel)));
    }
    return List.copyOf(skills);
  }

  private int experienceForLevel(int level) {
    if (level <= 1) {
      return 0;
    }

    int accumulated = 0;
    for (int currentLevel = 2; currentLevel <= level; currentLevel++) {
      int previousLevel = currentLevel - 1;
      int points = (int) Math.floor(previousLevel + 300.0 * Math.pow(2.0, previousLevel / 7.0));
      accumulated += points;
    }
    return accumulated / 4;
  }

  private record ExistingAccount(AccountId id, String loginName, String passwordHash) {
  }
}
