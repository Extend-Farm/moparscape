package com.veyrmoor.server.runtime;

@FunctionalInterface
interface PublicChatBroadcaster {

  void broadcast(String speakerDisplayName, String text);
}
