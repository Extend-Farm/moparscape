package io.github.ffakira.moparscape.client;

final class ChatNameTagParser {

    private ChatNameTagParser() {
    }

    static ParsedChatName parse(String rawName) {
        if (rawName == null) {
            return new ParsedChatName(null, 0);
        }
        if (rawName.startsWith("@cr1@")) {
            return new ParsedChatName(rawName.substring(5), 1);
        }
        if (rawName.startsWith("@cr2@")) {
            return new ParsedChatName(rawName.substring(5), 2);
        }
        return new ParsedChatName(rawName, 0);
    }

    static final class ParsedChatName {
        final String name;
        final int crownIcon;

        ParsedChatName(String name, int crownIcon) {
            this.name = name;
            this.crownIcon = crownIcon;
        }
    }
}
