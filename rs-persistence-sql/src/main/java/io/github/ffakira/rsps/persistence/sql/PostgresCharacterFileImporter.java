package io.github.ffakira.rsps.persistence.sql;

import io.github.ffakira.rsps.model.AccountId;
import io.github.ffakira.rsps.model.CharacterId;
import io.github.ffakira.rsps.persistence.PasswordHashing;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;

public final class PostgresCharacterFileImporter {

  public void importCharacter(SqlPersistenceConfiguration configuration, Path charactersDirectory, String username) {
    CharacterFileDocument document = new CharacterFileDocumentParser()
        .parse(charactersDirectory.resolve(normalize(username) + ".txt"));

    AccountId accountId = accountId(document.username());
    CharacterId characterId = new CharacterId(accountId.value());
    String passwordHash = PasswordHashing.hashPassword(document.password());

    try (Connection connection = configuration.openConnection()) {
      connection.setAutoCommit(false);
      try {
        upsertAccount(connection, accountId, document.username(), passwordHash);
        upsertCharacter(connection, characterId, accountId, document.username());
        upsertPosition(connection, characterId, document);
        upsertProfile(connection, characterId, document);
        upsertAppearance(connection, characterId, document);
        upsertSkillProgression(connection, characterId, document);
        replaceItemSlots(connection, characterId, document);
        replaceSocialLinks(connection, characterId, document);
        connection.commit();
      } catch (SQLException sqlException) {
        connection.rollback();
        throw sqlException;
      } finally {
        connection.setAutoCommit(true);
      }
    } catch (SQLException sqlException) {
      throw new IllegalStateException("Failed to import character " + username + " into PostgreSQL", sqlException);
    }
  }

  private void upsertAccount(Connection connection, AccountId accountId, String username, String passwordHash)
      throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        """
        insert into accounts (id, username, password_hash)
        values (?, ?, ?)
        on conflict (id) do update
        set username = excluded.username,
            password_hash = excluded.password_hash
        """
    )) {
      statement.setLong(1, accountId.value());
      statement.setString(2, username);
      statement.setString(3, passwordHash);
      statement.executeUpdate();
    }
  }

  private void upsertCharacter(
      Connection connection,
      CharacterId characterId,
      AccountId accountId,
      String displayName
  ) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        """
        insert into characters (id, account_id, display_name)
        values (?, ?, ?)
        on conflict (id) do update
        set account_id = excluded.account_id,
            display_name = excluded.display_name
        """
    )) {
      statement.setLong(1, characterId.value());
      statement.setLong(2, accountId.value());
      statement.setString(3, displayName);
      statement.executeUpdate();
    }
  }

  private void upsertPosition(Connection connection, CharacterId characterId, CharacterFileDocument document)
      throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        """
        insert into character_positions (character_id, world_x, world_y, plane)
        values (?, ?, ?, ?)
        on conflict (character_id) do update
        set world_x = excluded.world_x,
            world_y = excluded.world_y,
            plane = excluded.plane,
            updated_at = now()
        """
    )) {
      statement.setLong(1, characterId.value());
      statement.setInt(2, document.worldPoint().x());
      statement.setInt(3, document.worldPoint().y());
      statement.setInt(4, document.worldPoint().plane());
      statement.executeUpdate();
    }
  }

  private void upsertProfile(Connection connection, CharacterId characterId, CharacterFileDocument document)
      throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        """
        insert into character_profiles
          (character_id, rights, is_member, run_energy, last_login_day, game_time_counter, game_count_counter)
        values (?, ?, ?, ?, ?, ?, ?)
        on conflict (character_id) do update
        set rights = excluded.rights,
            is_member = excluded.is_member,
            run_energy = excluded.run_energy,
            last_login_day = excluded.last_login_day,
            game_time_counter = excluded.game_time_counter,
            game_count_counter = excluded.game_count_counter
        """
    )) {
      statement.setLong(1, characterId.value());
      statement.setShort(2, document.rights());
      statement.setBoolean(3, document.member());
      statement.setInt(4, document.runEnergy());
      if (document.lastLoginDay() == null) {
        statement.setObject(5, null);
      } else {
        statement.setInt(5, document.lastLoginDay());
      }
      statement.setLong(6, document.gameTimeCounter());
      statement.setLong(7, document.gameCountCounter());
      statement.executeUpdate();
    }
  }

  private void upsertAppearance(Connection connection, CharacterId characterId, CharacterFileDocument document)
      throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        """
        insert into character_appearances (character_id, look_values)
        values (?, ?)
        on conflict (character_id) do update
        set look_values = excluded.look_values
        """
    )) {
      statement.setLong(1, characterId.value());
      statement.setArray(2, connection.createArrayOf("integer", box(document.lookValues())));
      statement.executeUpdate();
    }
  }

  private void upsertSkillProgression(Connection connection, CharacterId characterId, CharacterFileDocument document)
      throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        """
        insert into character_skill_progression (character_id, current_levels, experience_values)
        values (?, ?, ?)
        on conflict (character_id) do update
        set current_levels = excluded.current_levels,
            experience_values = excluded.experience_values
        """
    )) {
      statement.setLong(1, characterId.value());
      statement.setArray(2, connection.createArrayOf("smallint", box(document.skillLevels())));
      statement.setArray(3, connection.createArrayOf("integer", box(document.skillExperience())));
      statement.executeUpdate();
    }
  }

  private void replaceItemSlots(Connection connection, CharacterId characterId, CharacterFileDocument document)
      throws SQLException {
    try (PreparedStatement deleteStatement = connection.prepareStatement(
        "delete from character_item_slots where character_id = ?"
    )) {
      deleteStatement.setLong(1, characterId.value());
      deleteStatement.executeUpdate();
    }

    try (PreparedStatement insertStatement = connection.prepareStatement(
        """
        insert into character_item_slots (character_id, container_kind, slot_index, item_id, quantity)
        values (?, ?, ?, ?, ?)
        """
    )) {
      for (CharacterFileDocument.CharacterItemSlot itemSlot : document.itemSlots()) {
        insertStatement.setLong(1, characterId.value());
        insertStatement.setShort(2, itemSlot.containerKind());
        insertStatement.setShort(3, itemSlot.slotIndex());
        insertStatement.setInt(4, itemSlot.itemId());
        insertStatement.setInt(5, itemSlot.quantity());
        insertStatement.addBatch();
      }
      insertStatement.executeBatch();
    }
  }

  private void replaceSocialLinks(Connection connection, CharacterId characterId, CharacterFileDocument document)
      throws SQLException {
    try (PreparedStatement deleteStatement = connection.prepareStatement(
        "delete from character_social_links where character_id = ?"
    )) {
      deleteStatement.setLong(1, characterId.value());
      deleteStatement.executeUpdate();
    }

    try (PreparedStatement insertStatement = connection.prepareStatement(
        """
        insert into character_social_links (character_id, link_kind, target_value)
        values (?, ?, ?)
        """
    )) {
      for (CharacterFileDocument.CharacterSocialLink socialLink : document.socialLinks()) {
        insertStatement.setLong(1, characterId.value());
        insertStatement.setShort(2, socialLink.linkKind());
        insertStatement.setLong(3, socialLink.targetValue());
        insertStatement.addBatch();
      }
      insertStatement.executeBatch();
    }
  }

  private static Long[] box(long[] values) {
    Long[] boxed = new Long[values.length];
    for (int index = 0; index < values.length; index++) {
      boxed[index] = values[index];
    }
    return boxed;
  }

  private static Integer[] box(int[] values) {
    Integer[] boxed = new Integer[values.length];
    for (int index = 0; index < values.length; index++) {
      boxed[index] = values[index];
    }
    return boxed;
  }

  private static Short[] box(short[] values) {
    Short[] boxed = new Short[values.length];
    for (int index = 0; index < values.length; index++) {
      boxed[index] = values[index];
    }
    return boxed;
  }

  private AccountId accountId(String username) {
    long value = Integer.toUnsignedLong(normalize(username).hashCode()) + 1L;
    return new AccountId(value);
  }

  private String normalize(String value) {
    return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
  }
}
