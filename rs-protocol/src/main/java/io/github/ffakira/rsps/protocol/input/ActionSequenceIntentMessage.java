package io.github.ffakira.rsps.protocol.input;

import io.github.ffakira.rsps.protocol.ClientMessage;

public record ActionSequenceIntentMessage(int actionSequenceId) implements ClientMessage {

  public ActionSequenceIntentMessage {
    actionSequenceId = Math.max(-1, actionSequenceId);
  }
}
