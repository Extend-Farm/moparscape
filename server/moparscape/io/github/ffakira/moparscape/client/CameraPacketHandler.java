package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.net.PacketBuffer;

final class CameraPacketHandler {

    private CameraPacketHandler() {
    }

    static int[] readCameraLock(PacketBuffer packetBuffer)
    {
        int tileX = packetBuffer.method408();
        int tileY = packetBuffer.method408();
        int heightOffset = packetBuffer.method410();
        int speed = packetBuffer.method408();
        int acceleration = packetBuffer.method408();
        return new int[] {
            tileX, tileY, heightOffset, speed, acceleration
        };
    }

    static int[] readCameraAngle(PacketBuffer packetBuffer)
    {
        int tileX = packetBuffer.method408();
        int tileY = packetBuffer.method408();
        int heightOffset = packetBuffer.method410();
        int speed = packetBuffer.method408();
        int acceleration = packetBuffer.method408();
        return new int[] {
            tileX, tileY, heightOffset, speed, acceleration
        };
    }

    static int[] computeCameraAngles(int cameraX, int cameraY, int cameraZ, int targetX, int targetY, int targetZ)
    {
        int dx = targetX - cameraX;
        int dy = targetY - cameraY;
        int dz = targetZ - cameraZ;
        int horizontalDistance = (int)Math.sqrt(dx * dx + dz * dz);
        int pitch = (int)(Math.atan2(dy, horizontalDistance) * 325.94900000000001D) & 0x7ff;
        int yaw = (int)(Math.atan2(dx, dz) * -325.94900000000001D) & 0x7ff;
        if(pitch < 128)
            pitch = 128;
        if(pitch > 383)
            pitch = 383;
        return new int[] {
            pitch, yaw
        };
    }
}
