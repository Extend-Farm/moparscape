package io.github.ffakira.moparscape.client.net.packet;

import io.github.ffakira.moparscape.net.PacketBuffer;

public final class CameraPacketHandler {

    private CameraPacketHandler() {
    }

    public static int[] readCameraLock(PacketBuffer packetBuffer)
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

    public static int[] readCameraAngle(PacketBuffer packetBuffer)
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

    public static int[] computeCameraAngles(int cameraX, int cameraY, int cameraZ, int targetX, int targetY, int targetZ)
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

    public static int[] readHintIconState(PacketBuffer packetBuffer)
    {
        int hintType = packetBuffer.method408();
        int targetPlayerId = -1;
        int hintOffsetX = 0;
        int hintOffsetY = 0;
        int hintTileX = 0;
        int hintTileY = 0;
        int hintHeight = 0;
        if(hintType == 1)
            targetPlayerId = packetBuffer.method410();
        if(hintType >= 2 && hintType <= 6)
        {
            if(hintType == 2)
            {
                hintOffsetX = 64;
                hintOffsetY = 64;
            }
            if(hintType == 3)
            {
                hintOffsetX = 0;
                hintOffsetY = 64;
            }
            if(hintType == 4)
            {
                hintOffsetX = 128;
                hintOffsetY = 64;
            }
            if(hintType == 5)
            {
                hintOffsetX = 64;
                hintOffsetY = 0;
            }
            if(hintType == 6)
            {
                hintOffsetX = 64;
                hintOffsetY = 128;
            }
            hintType = 2;
            hintTileX = packetBuffer.method410();
            hintTileY = packetBuffer.method410();
            hintHeight = packetBuffer.method408();
        }
        if(hintType == 10)
            targetPlayerId = packetBuffer.method410();
        return new int[] {
            hintType, targetPlayerId, hintOffsetX, hintOffsetY, hintTileX, hintTileY, hintHeight
        };
    }
}
