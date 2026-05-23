package com.veyrmoor.protocol;

public final class ProtocolCodecException extends RuntimeException {

  public ProtocolCodecException(String message) {
    super(message);
  }

  public ProtocolCodecException(String message, Throwable cause) {
    super(message, cause);
  }
}
