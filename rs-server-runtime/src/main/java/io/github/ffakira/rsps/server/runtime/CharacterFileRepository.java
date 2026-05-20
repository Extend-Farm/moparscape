package io.github.ffakira.rsps.server.runtime;

import io.github.ffakira.rsps.model.AccountId;
import io.github.ffakira.rsps.model.CharacterId;
import io.github.ffakira.rsps.model.WorldPoint;
import io.github.ffakira.rsps.persistence.AccountRecord;
import io.github.ffakira.rsps.persistence.AccountRepository;
import io.github.ffakira.rsps.persistence.CharacterAppearance;
import io.github.ffakira.rsps.persistence.CharacterItemSlot;
import io.github.ffakira.rsps.persistence.CharacterProfile;
import io.github.ffakira.rsps.persistence.CharacterRepository;
import io.github.ffakira.rsps.persistence.CharacterSkill;
import io.github.ffakira.rsps.persistence.CharacterSnapshot;
import io.github.ffakira.rsps.persistence.CharacterSocialLink;
import io.github.ffakira.rsps.persistence.ItemContainerKind;
import io.github.ffakira.rsps.persistence.SocialLinkKind;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

public final class CharacterFileRepository implements AccountRepository, CharacterRepository {

  private final Path charactersDirectory;

  public CharacterFileRepository(Path charactersDirectory) {
    this.charactersDirectory = charactersDirectory;
  }

  @Override
  public Optional<AccountRecord> findByUsername(String username) {
    String normalizedUsername = normalize(username);
    Path characterFile = charactersDirectory.resolve(normalizedUsername + ".txt");
    if (!Files.isRegularFile(characterFile)) {
      return Optional.empty();
    }

    ParsedCharacterFile parsedCharacterFile = parseCharacterFile(characterFile);
    if (!normalizedUsername.equals(normalize(parsedCharacterFile.username()))) {
      return Optional.empty();
    }
    AccountId accountId = accountId(normalizedUsername);
    return Optional.of(new AccountRecord(accountId, normalizedUsername, parsedCharacterFile.password()));
  }

  @Override
  public AccountRecord save(AccountRecord accountRecord) {
    throw new UnsupportedOperationException("Character file repository is read-only");
  }

  @Override
  public Optional<CharacterSnapshot> loadByAccountId(AccountId accountId) {
    try (Stream<Path> characterFiles = Files.list(charactersDirectory)) {
      return characterFiles
          .filter(path -> path.getFileName().toString().endsWith(".txt"))
          .map(this::parseCharacterFile)
          .filter(parsedCharacterFile -> accountId(parsedCharacterFile.username()).equals(accountId))
          .findFirst()
          .map(parsedCharacterFile -> parsedCharacterFile.toSnapshot(accountId(parsedCharacterFile.username())));
    } catch (IOException ioException) {
      throw new IllegalStateException("Failed to scan character directory " + charactersDirectory, ioException);
    }
  }

  @Override
  public CharacterSnapshot save(CharacterSnapshot characterSnapshot) {
    throw new UnsupportedOperationException("Character file repository is read-only");
  }

  private ParsedCharacterFile parseCharacterFile(Path characterFile) {
    List<String> lines;
    try {
      lines = Files.readAllLines(characterFile, StandardCharsets.UTF_8);
    } catch (IOException ioException) {
      throw new IllegalStateException("Failed to read character file " + characterFile, ioException);
    }

    Builder builder = new Builder();
    Section section = Section.NONE;
    for (String rawLine : lines) {
      String line = rawLine.trim();
      if (line.isEmpty()) {
        continue;
      }
      if (line.startsWith("[") && line.endsWith("]")) {
        section = Section.fromHeader(line);
        continue;
      }
      int separatorIndex = line.indexOf('=');
      if (separatorIndex < 0) {
        continue;
      }
      String key = line.substring(0, separatorIndex).trim();
      String value = line.substring(separatorIndex + 1).trim();
      section.apply(builder, key, value);
    }
    return builder.build();
  }

  private AccountId accountId(String username) {
    long value = Integer.toUnsignedLong(normalize(username).hashCode()) + 1L;
    return new AccountId(value);
  }

  private String normalize(String value) {
    return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
  }

  private enum Section {
    NONE {
      @Override
      void apply(Builder builder, String key, String value) {
      }
    },
    ACCOUNT {
      @Override
      void apply(Builder builder, String key, String value) {
        switch (key) {
          case "character-username" -> builder.username = value;
          case "character-password" -> builder.password = value;
          default -> {
          }
        }
      }
    },
    CHARACTER {
      @Override
      void apply(Builder builder, String key, String value) {
        switch (key) {
          case "character-height" -> builder.plane = Integer.parseInt(value);
          case "character-posx" -> builder.worldX = Integer.parseInt(value);
          case "character-posy" -> builder.worldY = Integer.parseInt(value);
          case "character-rights" -> builder.rights = Short.parseShort(value);
          case "character-ismember" -> builder.member = !"0".equals(value);
          case "character-lastlogin" -> builder.lastLoginDay = Integer.parseInt(value);
          case "character-energy" -> builder.runEnergy = Integer.parseInt(value);
          case "character-gametime" -> builder.gameTimeCounter = Long.parseLong(value);
          case "character-gamecount" -> builder.gameCountCounter = Long.parseLong(value);
          default -> {
          }
        }
      }
    },
    LOOK {
      @Override
      void apply(Builder builder, String key, String value) {
        if (!"character-look".equals(key)) {
          return;
        }
        String[] fields = value.split("\\s+");
        int slot = Integer.parseInt(fields[0]);
        builder.lookValues.set(slot, Integer.parseInt(fields[1]));
      }
    },
    SKILLS {
      @Override
      void apply(Builder builder, String key, String value) {
        if (!"character-skill".equals(key)) {
          return;
        }
        String[] fields = value.split("\\s+");
        builder.skills.add(new CharacterSkill(
            Integer.parseInt(fields[0]),
            Integer.parseInt(fields[1]),
            Integer.parseInt(fields[2])
        ));
      }
    },
    ITEMS {
      @Override
      void apply(Builder builder, String key, String value) {
        appendItemSlot(builder, ItemContainerKind.INVENTORY, key, value);
      }
    },
    EQUIPMENT {
      @Override
      void apply(Builder builder, String key, String value) {
        appendItemSlot(builder, ItemContainerKind.EQUIPMENT, key, value);
      }
    },
    BANK {
      @Override
      void apply(Builder builder, String key, String value) {
        appendItemSlot(builder, ItemContainerKind.BANK, key, value);
      }
    },
    FRIENDS {
      @Override
      void apply(Builder builder, String key, String value) {
        appendSocialLink(builder, SocialLinkKind.FRIEND, key, value);
      }
    },
    IGNORES {
      @Override
      void apply(Builder builder, String key, String value) {
        appendSocialLink(builder, SocialLinkKind.IGNORE, key, value);
      }
    };

    abstract void apply(Builder builder, String key, String value);

    static Section fromHeader(String header) {
      return switch (header) {
        case "[ACCOUNT]" -> ACCOUNT;
        case "[CHARACTER]" -> CHARACTER;
        case "[LOOK]" -> LOOK;
        case "[SKILLS]" -> SKILLS;
        case "[ITEMS]" -> ITEMS;
        case "[EQUIPMENT]" -> EQUIPMENT;
        case "[BANK]" -> BANK;
        case "[FRIENDS]" -> FRIENDS;
        case "[IGNORES]" -> IGNORES;
        default -> NONE;
      };
    }

    static void appendItemSlot(Builder builder, ItemContainerKind containerKind, String key, String value) {
      if (!key.startsWith("character-")) {
        return;
      }
      String[] fields = value.split("\\s+");
      int slotIndex = Integer.parseInt(fields[0]);
      int itemId = decodeLegacyItemId(containerKind, Integer.parseInt(fields[1]));
      int quantity = Integer.parseInt(fields[2]);
      if (itemId < 0 || quantity <= 0) {
        return;
      }
      builder.itemSlots.add(new CharacterItemSlot(containerKind, slotIndex, itemId, quantity));
    }

    static int decodeLegacyItemId(ItemContainerKind containerKind, int storedItemId) {
      return switch (containerKind) {
        case INVENTORY, BANK -> storedItemId - 1;
        case EQUIPMENT -> storedItemId;
      };
    }

    static void appendSocialLink(Builder builder, SocialLinkKind linkKind, String key, String value) {
      if (!key.startsWith("character-")) {
        return;
      }
      String[] fields = value.split("\\s+");
      builder.socialLinks.add(new CharacterSocialLink(linkKind, Long.parseLong(fields[1])));
    }
  }

  private static final class Builder {
    private String username = "";
    private String password = "";
    private int worldX;
    private int worldY;
    private int plane;
    private short rights;
    private boolean member;
    private int runEnergy = 100;
    private Integer lastLoginDay;
    private long gameTimeCounter;
    private long gameCountCounter;
    private final List<Integer> lookValues = new ArrayList<>(List.of(-1, -1, -1, -1, -1, -1));
    private final List<CharacterSkill> skills = new ArrayList<>();
    private final List<CharacterItemSlot> itemSlots = new ArrayList<>();
    private final List<CharacterSocialLink> socialLinks = new ArrayList<>();

    private ParsedCharacterFile build() {
      return new ParsedCharacterFile(
          username,
          password,
          new WorldPoint(worldX, worldY, plane),
          new CharacterProfile(rights, member, runEnergy, lastLoginDay, gameTimeCounter, gameCountCounter),
          new CharacterAppearance(lookValues),
          List.copyOf(skills),
          List.copyOf(itemSlots),
          List.copyOf(socialLinks)
      );
    }
  }

  private record ParsedCharacterFile(
      String username,
      String password,
      WorldPoint worldPoint,
      CharacterProfile profile,
      CharacterAppearance appearance,
      List<CharacterSkill> skills,
      List<CharacterItemSlot> itemSlots,
      List<CharacterSocialLink> socialLinks
  ) {

    private CharacterSnapshot toSnapshot(AccountId accountId) {
      return new CharacterSnapshot(
          new CharacterId(accountId.value()),
          accountId,
          username,
          worldPoint,
          profile,
          appearance,
          skills,
          itemSlots,
          socialLinks
      );
    }
  }
}
