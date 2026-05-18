package io.github.ffakira.rsps.protocol;

public record ProtocolVersion(String value) {

  public static ProtocolVersion current() {
    return new ProtocolVersion("rsps-modern-v1");
  }
}
