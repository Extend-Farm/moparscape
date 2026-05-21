package io.github.ffakira.rsps.protocol;

public record ActionSequenceIntentMessage(int actionSequenceId) implements ClientMessage {

  public ActionSequenceIntentMessage {
    actionSequenceId = Math.max(-1, actionSequenceId);
  }
}
