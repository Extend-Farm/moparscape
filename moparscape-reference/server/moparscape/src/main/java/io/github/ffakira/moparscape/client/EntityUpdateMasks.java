package io.github.ffakira.moparscape.client;

final class EntityUpdateMasks {

    private EntityUpdateMasks() {
    }

    static final class Npc {
        // TODO: Confirm semantics of each legacy mask bit from protocol docs.
        static final int ANIMATION = 0x10;
        static final int HIT_PRIMARY = 0x08;
        static final int GRAPHIC = 0x80;
        static final int INTERACTING_ENTITY = 0x20;
        static final int FORCED_CHAT = 0x01;
        static final int HIT_SECONDARY = 0x40;
        static final int TRANSFORM = 0x02;
        static final int FACE_COORDINATES = 0x04;

        private Npc() {
        }
    }

    static final class Player {
        // TODO: Confirm semantics of each legacy mask bit from protocol docs.
        static final int FORCE_MOVEMENT = 0x400;
        static final int GRAPHIC = 0x100;
        static final int ANIMATION = 0x08;
        static final int FORCED_CHAT = 0x04;
        static final int PUBLIC_CHAT = 0x80;
        static final int INTERACTING_ENTITY = 0x01;
        static final int APPEARANCE = 0x10;
        static final int FACE_COORDINATES = 0x02;
        static final int HIT_PRIMARY = 0x20;
        static final int HIT_SECONDARY = 0x200;

        private Player() {
        }
    }
}
