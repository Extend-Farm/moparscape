package com.veyrmoor.persistence;

import com.veyrmoor.model.AccountId;
import com.veyrmoor.model.CharacterId;
import com.veyrmoor.model.WorldPoint;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public record CharacterSnapshot(
    CharacterId id,
    AccountId accountId,
    String displayName,
    WorldPoint worldPoint,
    CharacterProfile profile,
    CharacterAppearance appearance,
    List<CharacterSkill> skills,
    List<CharacterItemSlot> itemSlots,
    List<CharacterSocialLink> socialLinks
) {

  public CharacterSnapshot {
    profile = profile == null ? CharacterProfile.defaults() : profile;
    appearance = appearance == null ? CharacterAppearance.defaults() : appearance;
    skills = List.copyOf(skills == null ? List.of() : skills);
    itemSlots = List.copyOf(itemSlots == null ? List.of() : itemSlots);
    socialLinks = List.copyOf(socialLinks == null ? List.of() : socialLinks);
  }

  public CharacterSnapshot(
      CharacterId id,
      AccountId accountId,
      String displayName,
      WorldPoint worldPoint
  ) {
    this(id, accountId, displayName, worldPoint, CharacterProfile.defaults(), CharacterAppearance.defaults(), List.of(), List.of(),
        List.of());
  }

  public List<CharacterItemSlot> inventorySlots() {
    return itemSlotsByKind(ItemContainerKind.INVENTORY);
  }

  public List<CharacterItemSlot> equipmentSlots() {
    return itemSlotsByKind(ItemContainerKind.EQUIPMENT);
  }

  public List<CharacterItemSlot> bankSlots() {
    return itemSlotsByKind(ItemContainerKind.BANK);
  }

  public List<CharacterSocialLink> friendLinks() {
    return socialLinksByKind(SocialLinkKind.FRIEND);
  }

  public List<CharacterSocialLink> ignoreLinks() {
    return socialLinksByKind(SocialLinkKind.IGNORE);
  }

  private List<CharacterItemSlot> itemSlotsByKind(ItemContainerKind containerKind) {
    return itemSlots.stream()
        .filter(slot -> slot.containerKind() == containerKind)
        .sorted(Comparator.comparingInt(CharacterItemSlot::slotIndex))
        .collect(Collectors.toUnmodifiableList());
  }

  private List<CharacterSocialLink> socialLinksByKind(SocialLinkKind linkKind) {
    return socialLinks.stream()
        .filter(link -> link.linkKind() == linkKind)
        .collect(Collectors.toUnmodifiableList());
  }
}
