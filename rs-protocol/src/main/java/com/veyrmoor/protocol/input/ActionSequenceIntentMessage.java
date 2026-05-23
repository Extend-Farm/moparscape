package com.veyrmoor.protocol.input;

import com.veyrmoor.protocol.ClientMessage;

public record ActionSequenceIntentMessage(int actionSequenceId) implements ClientMessage {

  public ActionSequenceIntentMessage {
    actionSequenceId = Math.max(-1, actionSequenceId);
  }
}
