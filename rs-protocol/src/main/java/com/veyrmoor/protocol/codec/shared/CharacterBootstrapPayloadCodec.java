package com.veyrmoor.protocol.codec.shared;

import com.veyrmoor.protocol.bootstrap.BootstrapItemSlot;
import com.veyrmoor.protocol.bootstrap.BootstrapSkill;
import com.veyrmoor.protocol.bootstrap.CharacterBootstrapPayload;
import com.veyrmoor.protocol.ProtocolLimits;
import com.veyrmoor.protocol.io.PacketReader;
import com.veyrmoor.protocol.io.PacketWriter;
import java.util.ArrayList;
import java.util.List;

public final class CharacterBootstrapPayloadCodec {

  public static final CharacterBootstrapPayloadCodec INSTANCE = new CharacterBootstrapPayloadCodec();

  private CharacterBootstrapPayloadCodec() {
  }

  public void encode(PacketWriter out, CharacterBootstrapPayload payload) {
    out.writeLong(payload.accountId());
    out.writeLong(payload.characterId());
    out.writeString(payload.displayName(), ProtocolLimits.DISPLAY_NAME_BYTES);
    out.writeString(payload.regionKey(), ProtocolLimits.REGION_KEY_BYTES);
    WorldPointCodec.INSTANCE.encode(out, payload.worldPoint());
    BootstrapProfileCodec.INSTANCE.encode(out, payload.profile());
    AppearanceCodec.INSTANCE.encode(out, payload.appearance());
    writeItemSlots(out, payload.inventory(), ProtocolLimits.INVENTORY_SLOTS);
    writeItemSlots(out, payload.equipment(), ProtocolLimits.EQUIPMENT_SLOTS);
    writeSkills(out, payload.skills());
  }

  public CharacterBootstrapPayload decode(PacketReader in) {
    return new CharacterBootstrapPayload(
        in.readLong(),
        in.readLong(),
        in.readString(ProtocolLimits.DISPLAY_NAME_BYTES),
        in.readString(ProtocolLimits.REGION_KEY_BYTES),
        WorldPointCodec.INSTANCE.decode(in),
        BootstrapProfileCodec.INSTANCE.decode(in),
        AppearanceCodec.INSTANCE.decode(in),
        readItemSlots(in, ProtocolLimits.INVENTORY_SLOTS),
        readItemSlots(in, ProtocolLimits.EQUIPMENT_SLOTS),
        readSkills(in)
    );
  }

  private static void writeItemSlots(PacketWriter out, List<BootstrapItemSlot> itemSlots, int limit) {
    if (itemSlots.size() > limit) {
      throw new IllegalArgumentException("Item slot count %d exceeds %d".formatted(itemSlots.size(), limit));
    }
    out.writeShort(itemSlots.size());
    for (BootstrapItemSlot itemSlot : itemSlots) {
      ItemSlotCodec.INSTANCE.encode(out, itemSlot);
    }
  }

  private static List<BootstrapItemSlot> readItemSlots(PacketReader in, int limit) {
    int size = in.readListSize(limit);
    ArrayList<BootstrapItemSlot> itemSlots = new ArrayList<>(size);
    for (int index = 0; index < size; index++) {
      itemSlots.add(ItemSlotCodec.INSTANCE.decode(in));
    }
    return itemSlots;
  }

  private static void writeSkills(PacketWriter out, List<BootstrapSkill> skills) {
    if (skills.size() > ProtocolLimits.SKILL_COUNT) {
      throw new IllegalArgumentException("Skill count %d exceeds %d".formatted(skills.size(), ProtocolLimits.SKILL_COUNT));
    }
    out.writeShort(skills.size());
    for (BootstrapSkill skill : skills) {
      SkillCodec.INSTANCE.encode(out, skill);
    }
  }

  private static List<BootstrapSkill> readSkills(PacketReader in) {
    int size = in.readListSize(ProtocolLimits.SKILL_COUNT);
    ArrayList<BootstrapSkill> skills = new ArrayList<>(size);
    for (int index = 0; index < size; index++) {
      skills.add(SkillCodec.INSTANCE.decode(in));
    }
    return skills;
  }
}
