package com.veyrmoor.protocol.opcode;

public final class ClientOpcodes {

  public static final int HANDSHAKE_REQUEST = 1;
  public static final int LOGIN_REQUEST = 2;
  public static final int MOVE_INTENT = 3;
  public static final int ACTION_SEQUENCE_INTENT = 4;
  public static final int DISCONNECT_NOTICE = 5;
  public static final int PUBLIC_CHAT_INTENT = 6;

  private ClientOpcodes() {
  }
}
