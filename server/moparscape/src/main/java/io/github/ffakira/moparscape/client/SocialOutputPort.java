package io.github.ffakira.moparscape.client;

interface SocialOutputPort {

  void addChatMessage(String message, int chatType, String sender, boolean messageFilterFlag);
}
