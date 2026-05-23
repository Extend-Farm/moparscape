package com.veyrmoor.persistence.sql;

import com.veyrmoor.model.AccountId;
import com.veyrmoor.model.CharacterId;
import com.veyrmoor.model.WorldPoint;
import com.veyrmoor.persistence.CharacterAppearance;
import com.veyrmoor.persistence.CharacterItemSlot;
import com.veyrmoor.persistence.CharacterProfile;
import com.veyrmoor.persistence.CharacterRepository;
import com.veyrmoor.persistence.CharacterSkill;
import com.veyrmoor.persistence.CharacterSnapshot;
import com.veyrmoor.persistence.CharacterSocialLink;
import com.veyrmoor.persistence.ItemContainerKind;
import com.veyrmoor.persistence.SocialLinkKind;
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
                    pr.rights, pr.is_member, pr.run_energy, pr.last_login_day, pr.game_time_counter, pr.game_count_counter
             from characters c
             join character_positions p on p.character_id = c.id
             left join character_profiles pr on pr.character_id = c.id
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
    String displayName = SqlNamePolicy.characterDisplayName(characterSnapshot.displayName());
    try (var connection = configuration.openConnection()) {
      connection.setAutoCommit(false);
      try (PreparedStatement characterStatement = connection.prepareStatement(
          """
          insert into characters (id, account_id, display_name, display_name_key)
          values (?, ?, ?, ?)
          on conflict (id) do update
          set account_id = excluded.account_id,
              display_name = excluded.display_name,
              display_name_key = excluded.display_name_key,
              updated_at = now()
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
                   game_count_counter = excluded.game_count_counter,
                   updated_at = now()
               """
           );
           PreparedStatement deleteAppearanceStatement = connection.prepareStatement(
               "delete from character_appearance_slots where character_id = ?"
           );
           PreparedStatement insertAppearanceStatement = connection.prepareStatement(
               """
               insert into character_appearance_slots (character_id, slot_index, look_value)
               values (?, ?, ?)
               """
           );
           PreparedStatement deleteSkillStatement = connection.prepareStatement(
               "delete from character_skills where character_id = ?"
           );
           PreparedStatement insertSkillStatement = connection.prepareStatement(
               """
               insert into character_skills (character_id, skill_id, current_level, experience)
               values (?, ?, ?, ?)
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
        characterStatement.setString(3, displayName);
        characterStatement.setString(4, SqlNamePolicy.characterDisplayNameKey(displayName));
        characterStatement.executeUpdate();

        positionStatement.setLong(1, characterSnapshot.id().value());
        positionStatement.setInt(2, characterSnapshot.worldPoint().x());
        positionStatement.setInt(3, characterSnapshot.worldPoint().y());
        positionStatement.setInt(4, characterSnapshot.worldPoint().plane());
        positionStatement.executeUpdate();

        saveProfile(characterSnapshot, profileStatement);
        replaceAppearance(characterSnapshot, deleteAppearanceStatement, insertAppearanceStatement);
        replaceSkills(characterSnapshot, deleteSkillStatement, insertSkillStatement);
        replaceItemSlots(characterSnapshot, deleteItemSlotsStatement, insertItemSlotStatement);
        replaceSocialLinks(characterSnapshot, deleteSocialLinksStatement, insertSocialLinkStatement);

        connection.commit();
      } catch (SQLException sqlException) {
        connection.rollback();
        throw sqlException;
      } finally {
        connection.setAutoCommit(true);
      }
      return new CharacterSnapshot(
          characterSnapshot.id(),
          characterSnapshot.accountId(),
          displayName,
          characterSnapshot.worldPoint(),
          characterSnapshot.profile(),
          characterSnapshot.appearance(),
          characterSnapshot.skills(),
          characterSnapshot.itemSlots(),
          characterSnapshot.socialLinks()
      );
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
        loadAppearance(connection, characterId),
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

  private CharacterAppearance loadAppearance(java.sql.Connection connection, CharacterId characterId) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        """
        select slot_index, look_value
        from character_appearance_slots
        where character_id = ?
        order by slot_index
        """
    )) {
      statement.setLong(1, characterId.value());
      try (ResultSet resultSet = statement.executeQuery()) {
        List<Integer> lookValues = new ArrayList<>();
        while (resultSet.next()) {
          lookValues.add(resultSet.getInt("look_value"));
        }
        if (lookValues.isEmpty()) {
          return CharacterAppearance.defaults();
        }
        return new CharacterAppearance(lookValues);
      }
    }
  }

  private List<CharacterSkill> loadSkills(java.sql.Connection connection, CharacterId characterId) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        """
        select skill_id, current_level, experience
        from character_skills
        where character_id = ?
        order by skill_id
        """
    )) {
      statement.setLong(1, characterId.value());
      try (ResultSet resultSet = statement.executeQuery()) {
        List<CharacterSkill> skills = new ArrayList<>();
        while (resultSet.next()) {
          skills.add(new CharacterSkill(
              resultSet.getInt("skill_id"),
              resultSet.getInt("current_level"),
              resultSet.getInt("experience")
          ));
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

  private void replaceAppearance(
      CharacterSnapshot characterSnapshot,
      PreparedStatement deleteStatement,
      PreparedStatement insertStatement
  ) throws SQLException {
    deleteStatement.setLong(1, characterSnapshot.id().value());
    deleteStatement.executeUpdate();

    List<Integer> lookValues = characterSnapshot.appearance().lookValues();
    for (int slotIndex = 0; slotIndex < lookValues.size(); slotIndex++) {
      insertStatement.setLong(1, characterSnapshot.id().value());
      insertStatement.setShort(2, (short) slotIndex);
      insertStatement.setInt(3, lookValues.get(slotIndex));
      insertStatement.addBatch();
    }
    insertStatement.executeBatch();
  }

  private void replaceSkills(
      CharacterSnapshot characterSnapshot,
      PreparedStatement deleteStatement,
      PreparedStatement insertStatement
  ) throws SQLException {
    deleteStatement.setLong(1, characterSnapshot.id().value());
    deleteStatement.executeUpdate();

    for (CharacterSkill skill : characterSnapshot.skills()) {
      insertStatement.setLong(1, characterSnapshot.id().value());
      insertStatement.setShort(2, (short) skill.skillId());
      insertStatement.setShort(3, (short) skill.currentLevel());
      insertStatement.setInt(4, skill.experience());
      insertStatement.addBatch();
    }
    insertStatement.executeBatch();
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
}
