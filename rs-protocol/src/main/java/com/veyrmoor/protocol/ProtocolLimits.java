package com.veyrmoor.protocol;

public final class ProtocolLimits {

  public static final int PROTOCOL_VERSION_BYTES = 32;
  public static final int CLIENT_BUILD_BYTES = 64;
  public static final int USERNAME_BYTES = 20;
  public static final int PASSWORD_BYTES = 128;
  public static final int DISPLAY_NAME_BYTES = 32;
  public static final int REGION_KEY_BYTES = 32;
  public static final int DISCONNECT_REASON_BYTES = 128;
  public static final int MOTD_BYTES = 256;
  public static final int PUBLIC_CHAT_TEXT_BYTES = 80;
  public static final int INVENTORY_SLOTS = 28;
  public static final int EQUIPMENT_SLOTS = 14;
  public static final int SKILL_COUNT = 32;
  public static final int APPEARANCE_LOOK_VALUES = 16;

  private ProtocolLimits() {
  }
}
