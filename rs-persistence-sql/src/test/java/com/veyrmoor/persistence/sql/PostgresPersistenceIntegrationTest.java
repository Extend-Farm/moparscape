package com.veyrmoor.persistence.sql;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.model.AccountId;
import com.veyrmoor.model.CharacterId;
import com.veyrmoor.model.StaffRole;
import com.veyrmoor.model.WorldPoint;
import com.veyrmoor.persistence.AccountRecord;
import com.veyrmoor.persistence.CharacterAppearance;
import com.veyrmoor.persistence.CharacterItemSlot;
import com.veyrmoor.persistence.CharacterProfile;
import com.veyrmoor.persistence.CharacterSkill;
import com.veyrmoor.persistence.CharacterSnapshot;
import com.veyrmoor.persistence.CharacterSocialLink;
import com.veyrmoor.persistence.ItemContainerKind;
import com.veyrmoor.persistence.PasswordHashing;
import com.veyrmoor.persistence.SocialLinkKind;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class PostgresPersistenceIntegrationTest {

  @Container
  private static final PostgreSQLContainer<?> POSTGRES =
      new PostgreSQLContainer<>("postgres:16-alpine");

  private SqlPersistenceConfiguration configuration;
  private FlywayMigrator migrator;
  private PostgresAccountRepository accountRepository;
  private PostgresCharacterRepository characterRepository;
  private PostgresAccountProvisioner accountProvisioner;

  @BeforeEach
  void setUp() {
    configuration = new SqlPersistenceConfiguration(
        POSTGRES.getJdbcUrl(),
        POSTGRES.getUsername(),
        POSTGRES.getPassword()
    );
    migrator = new FlywayMigrator();
    migrator.resetAndMigrate(configuration);
    accountRepository = new PostgresAccountRepository(configuration);
    characterRepository = new PostgresCharacterRepository(configuration);
    accountProvisioner = new PostgresAccountProvisioner(configuration);
  }

  @Test
  void accountLookupUsesCanonicalKeyWithoutDestroyingStoredNameCase() {
    AccountRecord savedAccount = accountRepository.save(new AccountRecord(
        new AccountId(1001L),
        "Akira",
        PasswordHashing.hashPassword("password")
    ));

    AccountRecord loadedAccount = accountRepository.findByUsername("akira").orElseThrow();

    assertThat(loadedAccount).isEqualTo(savedAccount);
    assertThat(loadedAccount.username()).isEqualTo("Akira");
  }

  @Test
  void characterRepositoryRoundTripsNormalizedStateRows() {
    AccountId accountId = new AccountId(2002L);
    CharacterSnapshot savedCharacter = new CharacterSnapshot(
        new CharacterId(3003L),
        accountId,
        "Akira",
        new WorldPoint(3235, 3219, 0),
        new CharacterProfile(StaffRole.ADMIN, true, 87, 19483, 1_234L, 77L),
        new CharacterAppearance(List.of(0, 1, 2, 3, 4, 5)),
        List.of(
            new CharacterSkill(0, 99, 13_034_431),
            new CharacterSkill(5, 43, 50_000)
        ),
        List.of(
            new CharacterItemSlot(ItemContainerKind.INVENTORY, 0, 555, 1000),
            new CharacterItemSlot(ItemContainerKind.EQUIPMENT, 2, 1712, 1),
            new CharacterItemSlot(ItemContainerKind.BANK, 12, 995, 500_000)
        ),
        List.of(
            new CharacterSocialLink(SocialLinkKind.FRIEND, 42L),
            new CharacterSocialLink(SocialLinkKind.IGNORE, 99L)
        )
    );
    accountRepository.save(new AccountRecord(accountId, "Akira", PasswordHashing.hashPassword("password")));

    CharacterSnapshot persistedCharacter = characterRepository.save(savedCharacter);
    CharacterSnapshot loadedCharacter = characterRepository.loadByAccountId(accountId).orElseThrow();

    assertThat(loadedCharacter).isEqualTo(persistedCharacter);
  }

  @Test
  void resetAndMigrateWipesPersistedRows() {
    AccountId accountId = new AccountId(4004L);
    accountRepository.save(new AccountRecord(accountId, "Akira", PasswordHashing.hashPassword("password")));
    characterRepository.save(new CharacterSnapshot(
        new CharacterId(5005L),
        accountId,
        "Akira",
        new WorldPoint(3232, 3218, 0)
    ));

    migrator.resetAndMigrate(configuration);

    PostgresAccountRepository freshAccountRepository = new PostgresAccountRepository(configuration);
    PostgresCharacterRepository freshCharacterRepository = new PostgresCharacterRepository(configuration);
    assertThat(freshAccountRepository.findByUsername("akira")).isEmpty();
    assertThat(freshCharacterRepository.loadByAccountId(accountId)).isEmpty();
  }

  @Test
  void provisionerCreatesHashedAccountAndStarterCharacter() {
    AccountProvisioningResult result = accountProvisioner.provision(
        new AccountProvisioningRequest("Akira", "correct horse battery staple", "Akira")
    );

    AccountRecord account = accountRepository.findByUsername("akira").orElseThrow();
    CharacterSnapshot character = characterRepository.loadByAccountId(account.id()).orElseThrow();

    assertThat(result.accountCreated()).isTrue();
    assertThat(result.characterCreated()).isTrue();
    assertThat(result.passwordUpdated()).isTrue();
    assertThat(account.passwordHash()).startsWith("pbkdf2-sha256$");
    assertThat(PasswordHashing.matches(account.passwordHash(), "correct horse battery staple")).isTrue();
    assertThat(account.passwordHash()).isNotEqualTo("correct horse battery staple");
    assertThat(character.displayName()).isEqualTo("Akira");
    assertThat(character.worldPoint()).isEqualTo(new WorldPoint(3232, 3218, 0));
    assertThat(character.skills()).hasSize(21);
    assertThat(character.skills()).anySatisfy(skill -> {
      assertThat(skill.skillId()).isEqualTo(3);
      assertThat(skill.currentLevel()).isEqualTo(10);
    });
  }

  @Test
  void provisionerIsIdempotentAndCanRotatePasswordWithoutRecreatingCharacter() {
    AccountProvisioningResult first = accountProvisioner.provision(
        new AccountProvisioningRequest("Akira", "first password", "Akira")
    );

    AccountProvisioningResult second = accountProvisioner.provision(
        new AccountProvisioningRequest("akira", "second password", "Akira")
    );

    AccountRecord account = accountRepository.findByUsername("AKIRA").orElseThrow();
    CharacterSnapshot character = characterRepository.loadByAccountId(account.id()).orElseThrow();

    assertThat(second.accountId()).isEqualTo(first.accountId());
    assertThat(second.characterId()).isEqualTo(first.characterId());
    assertThat(second.accountCreated()).isFalse();
    assertThat(second.characterCreated()).isFalse();
    assertThat(second.passwordUpdated()).isTrue();
    assertThat(PasswordHashing.matches(account.passwordHash(), "second password")).isTrue();
    assertThat(character.id()).isEqualTo(first.characterId());
  }
}
