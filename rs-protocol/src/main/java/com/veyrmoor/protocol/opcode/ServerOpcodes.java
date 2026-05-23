package com.veyrmoor.protocol.opcode;

public final class ServerOpcodes {

  public static final int HANDSHAKE_ACCEPTED = 101;
  public static final int LOGIN_ACCEPTED = 102;
  public static final int LOGIN_REJECTED = 103;
  public static final int CHARACTER_BOOTSTRAP = 104;
  public static final int WORLD_SNAPSHOT = 105;
  public static final int ENTITY_POSITION = 106;
  public static final int ENTITY_ACTION_SEQUENCE = 107;
  public static final int PUBLIC_CHAT = 108;

  private ServerOpcodes() {
  }
}
