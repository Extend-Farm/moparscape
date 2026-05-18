package io.github.ffakira.rsps.persistence;

public record CharacterSocialLink(SocialLinkKind linkKind, long targetValue) {

  public CharacterSocialLink {
    if (targetValue < 0) {
      throw new IllegalArgumentException("Social link target cannot be negative");
    }
  }
}
