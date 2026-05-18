package io.github.ffakira.rsps.persistence.sql;

import io.github.ffakira.rsps.model.AccountId;
import io.github.ffakira.rsps.model.CharacterId;
import io.github.ffakira.rsps.model.WorldPoint;
import io.github.ffakira.rsps.persistence.CharacterAppearance;
import io.github.ffakira.rsps.persistence.CharacterItemSlot;
import io.github.ffakira.rsps.persistence.CharacterRepository;
import io.github.ffakira.rsps.persistence.CharacterSkill;
import io.github.ffakira.rsps.persistence.CharacterSocialLink;
import io.github.ffakira.rsps.persistence.CharacterSnapshot;
import io.github.ffakira.rsps.persistence.CharacterProfile;
import io.github.ffakira.rsps.persistence.ItemContainerKind;
import io.github.ffakira.rsps.persistence.SocialLinkKind;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgresCharacterRepository implements CharacterRepository {

  private final SqlPersistenceConfiguration configuration;

  public PostgresCharacterRepository(SqlPersistenceConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public Optional<CharacterSnapshot> loadByAccountId(AccountId accountId) {
    try (var connection = configuration.openConnection();
         PreparedStatement statement = connection.prepareStatement(
             """
             select c.id, c.account_id, c.display_name,
                    p.world_x, p.world_y, p.plane,
                    pr.rights, pr.is_member, pr.run_energy, pr.last_login_day, pr.game_time_counter, pr.game_count_counter,
                    ap.look_values
             from characters c
             join character_positions p on p.character_id = c.id
             left join character_profiles pr on pr.character_id = c.id
             left join character_appearances ap on ap.character_id = c.id
             where c.account_id = ?
             """
         )) {
      statement.setLong(1, accountId.value());
      try (ResultSet resultSet = statement.executeQuery()) {
        if (!resultSet.next()) {
          return Optional.empty();
        }
        return Optional.of(map(connection, resultSet));
      }
    } catch (SQLException sqlException) {
      throw new IllegalStateException("Failed to load character for account " + accountId.value(), sqlException);
    }
  }

  @Override
  public CharacterSnapshot save(CharacterSnapshot characterSnapshot) {
    try (var connection = configuration.openConnection()) {
      connection.setAutoCommit(false);
      try (PreparedStatement characterStatement = connection.prepareStatement(
          """
          insert into characters (id, account_id, display_name)
          values (?, ?, ?)
          on conflict (id) do update
          set account_id = excluded.account_id,
              display_name = excluded.display_name
          """
      );
           PreparedStatement positionStatement = connection.prepareStatement(
               """
               insert into character_positions (character_id, world_x, world_y, plane)
               values (?, ?, ?, ?)
               on conflict (character_id) do update
               set world_x = excluded.world_x,
                   world_y = excluded.world_y,
                   plane = excluded.plane,
                   updated_at = now()
               """
           );
           PreparedStatement profileStatement = connection.prepareStatement(
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
           );
           PreparedStatement appearanceStatement = connection.prepareStatement(
               """
               insert into character_appearances (character_id, look_values)
               values (?, ?)
               on conflict (character_id) do update
               set look_values = excluded.look_values
               """
           );
           PreparedStatement skillStatement = connection.prepareStatement(
               """
               insert into character_skill_progression (character_id, current_levels, experience_values)
               values (?, ?, ?)
               on conflict (character_id) do update
               set current_levels = excluded.current_levels,
                   experience_values = excluded.experience_values
               """
           );
           PreparedStatement deleteItemSlotsStatement = connection.prepareStatement(
               "delete from character_item_slots where character_id = ?"
           );
           PreparedStatement insertItemSlotStatement = connection.prepareStatement(
               """
               insert into character_item_slots (character_id, container_kind, slot_index, item_id, quantity)
               values (?, ?, ?, ?, ?)
               """
           );
           PreparedStatement deleteSocialLinksStatement = connection.prepareStatement(
               "delete from character_social_links where character_id = ?"
           );
           PreparedStatement insertSocialLinkStatement = connection.prepareStatement(
               """
               insert into character_social_links (character_id, link_kind, target_value)
               values (?, ?, ?)
               """
           )) {
        characterStatement.setLong(1, characterSnapshot.id().value());
        characterStatement.setLong(2, characterSnapshot.accountId().value());
        characterStatement.setString(3, characterSnapshot.displayName());
        characterStatement.executeUpdate();

        positionStatement.setLong(1, characterSnapshot.id().value());
        positionStatement.setInt(2, characterSnapshot.worldPoint().x());
        positionStatement.setInt(3, characterSnapshot.worldPoint().y());
        positionStatement.setInt(4, characterSnapshot.worldPoint().plane());
        positionStatement.executeUpdate();

        saveProfile(characterSnapshot, profileStatement);
        saveAppearance(connection, characterSnapshot, appearanceStatement);
        saveSkills(connection, characterSnapshot, skillStatement);
        replaceItemSlots(characterSnapshot, deleteItemSlotsStatement, insertItemSlotStatement);
        replaceSocialLinks(characterSnapshot, deleteSocialLinksStatement, insertSocialLinkStatement);

        connection.commit();
      } catch (SQLException sqlException) {
        connection.rollback();
        throw sqlException;
      } finally {
        connection.setAutoCommit(true);
      }
      return characterSnapshot;
    } catch (SQLException sqlException) {
      throw new IllegalStateException("Failed to save character " + characterSnapshot.displayName(), sqlException);
    }
  }

  private CharacterSnapshot map(java.sql.Connection connection, ResultSet resultSet) throws SQLException {
    CharacterId characterId = new CharacterId(resultSet.getLong("id"));
    return new CharacterSnapshot(
        characterId,
        new AccountId(resultSet.getLong("account_id")),
        resultSet.getString("display_name"),
        new WorldPoint(resultSet.getInt("world_x"), resultSet.getInt("world_y"), resultSet.getInt("plane")),
        mapProfile(resultSet),
        mapAppearance(resultSet.getArray("look_values")),
        loadSkills(connection, characterId),
        loadItemSlots(connection, characterId),
        loadSocialLinks(connection, characterId)
    );
  }

  private CharacterProfile mapProfile(ResultSet resultSet) throws SQLException {
    short rights = resultSet.getShort("rights");
    boolean hasProfile = !resultSet.wasNull();
    if (!hasProfile) {
      return CharacterProfile.defaults();
    }
    return new CharacterProfile(
        rights,
        resultSet.getBoolean("is_member"),
        resultSet.getInt("run_energy"),
        (Integer) resultSet.getObject("last_login_day"),
        resultSet.getLong("game_time_counter"),
        resultSet.getLong("game_count_counter")
    );
  }

  private CharacterAppearance mapAppearance(Array sqlArray) throws SQLException {
    if (sqlArray == null) {
      return CharacterAppearance.defaults();
    }
    return new CharacterAppearance(toIntegerList((Integer[]) sqlArray.getArray()));
  }

  private List<CharacterSkill> loadSkills(java.sql.Connection connection, CharacterId characterId) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        """
        select current_levels, experience_values
        from character_skill_progression
        where character_id = ?
        """
    )) {
      statement.setLong(1, characterId.value());
      try (ResultSet resultSet = statement.executeQuery()) {
        if (!resultSet.next()) {
          return List.of();
        }
        Short[] levels = (Short[]) resultSet.getArray("current_levels").getArray();
        Integer[] experienceValues = (Integer[]) resultSet.getArray("experience_values").getArray();
        int skillCount = Math.min(levels.length, experienceValues.length);
        List<CharacterSkill> skills = new ArrayList<>(skillCount);
        for (int skillId = 0; skillId < skillCount; skillId++) {
          skills.add(new CharacterSkill(skillId, levels[skillId], experienceValues[skillId]));
        }
        return List.copyOf(skills);
      }
    }
  }

  private List<CharacterItemSlot> loadItemSlots(java.sql.Connection connection, CharacterId characterId) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        """
        select container_kind, slot_index, item_id, quantity
        from character_item_slots
        where character_id = ?
        order by container_kind, slot_index
        """
    )) {
      statement.setLong(1, characterId.value());
      try (ResultSet resultSet = statement.executeQuery()) {
        List<CharacterItemSlot> slots = new ArrayList<>();
        while (resultSet.next()) {
          slots.add(new CharacterItemSlot(
              ItemContainerKind.fromDatabaseValue(resultSet.getShort("container_kind")),
              resultSet.getShort("slot_index"),
              resultSet.getInt("item_id"),
              resultSet.getInt("quantity")
          ));
        }
        return List.copyOf(slots);
      }
    }
  }

  private List<CharacterSocialLink> loadSocialLinks(java.sql.Connection connection, CharacterId characterId) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        """
        select link_kind, target_value
        from character_social_links
        where character_id = ?
        order by link_kind, target_value
        """
    )) {
      statement.setLong(1, characterId.value());
      try (ResultSet resultSet = statement.executeQuery()) {
        List<CharacterSocialLink> links = new ArrayList<>();
        while (resultSet.next()) {
          links.add(new CharacterSocialLink(
              SocialLinkKind.fromDatabaseValue(resultSet.getShort("link_kind")),
              resultSet.getLong("target_value")
          ));
        }
        return List.copyOf(links);
      }
    }
  }

  private void saveProfile(CharacterSnapshot characterSnapshot, PreparedStatement statement) throws SQLException {
    statement.setLong(1, characterSnapshot.id().value());
    statement.setShort(2, characterSnapshot.profile().rights());
    statement.setBoolean(3, characterSnapshot.profile().member());
    statement.setInt(4, characterSnapshot.profile().runEnergy());
    if (characterSnapshot.profile().lastLoginDay() == null) {
      statement.setObject(5, null);
    } else {
      statement.setInt(5, characterSnapshot.profile().lastLoginDay());
    }
    statement.setLong(6, characterSnapshot.profile().gameTimeCounter());
    statement.setLong(7, characterSnapshot.profile().gameCountCounter());
    statement.executeUpdate();
  }

  private void saveAppearance(
      java.sql.Connection connection,
      CharacterSnapshot characterSnapshot,
      PreparedStatement statement
  ) throws SQLException {
    statement.setLong(1, characterSnapshot.id().value());
    statement.setArray(2, connection.createArrayOf("integer", characterSnapshot.appearance().lookValues().toArray(Integer[]::new)));
    statement.executeUpdate();
  }

  private void saveSkills(
      java.sql.Connection connection,
      CharacterSnapshot characterSnapshot,
      PreparedStatement statement
  ) throws SQLException {
    List<CharacterSkill> skills = characterSnapshot.skills();
    int arraySize = skills.stream().mapToInt(CharacterSkill::skillId).max().orElse(-1) + 1;
    Short[] levels = new Short[arraySize];
    Integer[] experience = new Integer[arraySize];
    for (CharacterSkill skill : skills) {
      levels[skill.skillId()] = (short) skill.currentLevel();
      experience[skill.skillId()] = skill.experience();
    }
    for (int index = 0; index < arraySize; index++) {
      if (levels[index] == null) {
        levels[index] = 0;
      }
      if (experience[index] == null) {
        experience[index] = 0;
      }
    }
    statement.setLong(1, characterSnapshot.id().value());
    statement.setArray(2, connection.createArrayOf("smallint", levels));
    statement.setArray(3, connection.createArrayOf("integer", experience));
    statement.executeUpdate();
  }

  private void replaceItemSlots(
      CharacterSnapshot characterSnapshot,
      PreparedStatement deleteStatement,
      PreparedStatement insertStatement
  ) throws SQLException {
    deleteStatement.setLong(1, characterSnapshot.id().value());
    deleteStatement.executeUpdate();

    for (CharacterItemSlot itemSlot : characterSnapshot.itemSlots()) {
      insertStatement.setLong(1, characterSnapshot.id().value());
      insertStatement.setShort(2, itemSlot.containerKind().databaseValue());
      insertStatement.setShort(3, (short) itemSlot.slotIndex());
      insertStatement.setInt(4, itemSlot.itemId());
      insertStatement.setInt(5, itemSlot.quantity());
      insertStatement.addBatch();
    }
    insertStatement.executeBatch();
  }

  private void replaceSocialLinks(
      CharacterSnapshot characterSnapshot,
      PreparedStatement deleteStatement,
      PreparedStatement insertStatement
  ) throws SQLException {
    deleteStatement.setLong(1, characterSnapshot.id().value());
    deleteStatement.executeUpdate();

    for (CharacterSocialLink socialLink : characterSnapshot.socialLinks()) {
      insertStatement.setLong(1, characterSnapshot.id().value());
      insertStatement.setShort(2, socialLink.linkKind().databaseValue());
      insertStatement.setLong(3, socialLink.targetValue());
      insertStatement.addBatch();
    }
    insertStatement.executeBatch();
  }

  private List<Integer> toIntegerList(Integer[] values) {
    List<Integer> list = new ArrayList<>(values.length);
    for (Integer value : values) {
      list.add(value);
    }
    return List.copyOf(list);
  }
}
