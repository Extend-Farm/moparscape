package io.github.ffakira.rsps.persistence.sql;

import io.github.ffakira.rsps.model.WorldPoint;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

final class CharacterFileDocumentParser {

  private static final short INVENTORY_CONTAINER = 0;
  private static final short EQUIPMENT_CONTAINER = 1;
  private static final short BANK_CONTAINER = 2;
  private static final short FRIEND_LINK = 0;
  private static final short IGNORE_LINK = 1;

  CharacterFileDocument parse(Path characterFile) {
    Builder builder = new Builder();
    Section currentSection = Section.NONE;
    List<String> lines;
    try {
      lines = Files.readAllLines(characterFile, StandardCharsets.UTF_8);
    } catch (IOException ioException) {
      throw new IllegalStateException("Failed to read character file " + characterFile, ioException);
    }

    for (String rawLine : lines) {
      String line = rawLine.trim();
      if (line.isEmpty()) {
        continue;
      }
      if (line.startsWith("[") && line.endsWith("]")) {
        currentSection = Section.fromHeader(line);
        continue;
      }
      int separatorIndex = line.indexOf('=');
      if (separatorIndex < 0) {
        continue;
      }

      String key = line.substring(0, separatorIndex).trim();
      String value = line.substring(separatorIndex + 1).trim();
      currentSection.apply(builder, key, value);
    }
    return builder.build();
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
        int lookValue = Integer.parseInt(fields[1]);
        builder.lookValues[slot] = lookValue;
      }
    },
    SKILLS {
      @Override
      void apply(Builder builder, String key, String value) {
        if (!"character-skill".equals(key)) {
          return;
        }
        String[] fields = value.split("\\s+");
        int skillId = Integer.parseInt(fields[0]);
        builder.skillLevels[skillId] = Short.parseShort(fields[1]);
        builder.skillExperience[skillId] = Integer.parseInt(fields[2]);
      }
    },
    ITEMS {
      @Override
      void apply(Builder builder, String key, String value) {
        appendItemSlot(builder, INVENTORY_CONTAINER, key, value);
      }
    },
    EQUIPMENT {
      @Override
      void apply(Builder builder, String key, String value) {
        appendItemSlot(builder, EQUIPMENT_CONTAINER, key, value);
      }
    },
    BANK {
      @Override
      void apply(Builder builder, String key, String value) {
        appendItemSlot(builder, BANK_CONTAINER, key, value);
      }
    },
    FRIENDS {
      @Override
      void apply(Builder builder, String key, String value) {
        appendSocialLink(builder, FRIEND_LINK, key, value);
      }
    },
    IGNORES {
      @Override
      void apply(Builder builder, String key, String value) {
        appendSocialLink(builder, IGNORE_LINK, key, value);
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

    static void appendItemSlot(Builder builder, short containerKind, String key, String value) {
      if (!key.startsWith("character-")) {
        return;
      }
      String[] fields = value.split("\\s+");
      int slotIndex = Integer.parseInt(fields[0]);
      int itemId = Integer.parseInt(fields[1]);
      int quantity = Integer.parseInt(fields[2]);
      if (itemId < 0 || quantity <= 0) {
        return;
      }
      builder.itemSlots.add(new CharacterFileDocument.CharacterItemSlot(containerKind, (short) slotIndex, itemId, quantity));
    }

    static void appendSocialLink(Builder builder, short linkKind, String key, String value) {
      if (!key.startsWith("character-")) {
        return;
      }
      String[] fields = value.split("\\s+");
      long targetValue = Long.parseLong(fields[1]);
      builder.socialLinks.add(new CharacterFileDocument.CharacterSocialLink(linkKind, targetValue));
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
    private final int[] lookValues = new int[] {-1, -1, -1, -1, -1, -1};
    private final short[] skillLevels = new short[25];
    private final int[] skillExperience = new int[25];
    private final List<CharacterFileDocument.CharacterItemSlot> itemSlots = new ArrayList<>();
    private final List<CharacterFileDocument.CharacterSocialLink> socialLinks = new ArrayList<>();

    private CharacterFileDocument build() {
      return new CharacterFileDocument(
          username,
          password,
          new WorldPoint(worldX, worldY, plane),
          rights,
          member,
          runEnergy,
          lastLoginDay,
          gameTimeCounter,
          gameCountCounter,
          lookValues.clone(),
          skillLevels.clone(),
          skillExperience.clone(),
          List.copyOf(itemSlots),
          List.copyOf(socialLinks)
      );
    }
  }
}
