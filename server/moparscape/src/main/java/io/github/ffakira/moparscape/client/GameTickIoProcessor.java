package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.net.PacketBuffer;
import io.github.ffakira.moparscape.net.BufferedConnection;
import java.io.IOException;

final class GameTickIoProcessor {

    static final class MouseBatchState {
        int lastMouseX;
        int lastMouseY;
        int repeatCount;
    }

    private GameTickIoProcessor() {
    }

    static MouseBatchState processMouseRecorderBatch(MouseRecorder recorder, PacketBuffer outboundBuffer, boolean mouseTrackingEnabled, int lastMouseX, int lastMouseY, int repeatCount)
    {
        MouseBatchState state = new MouseBatchState();
        state.lastMouseX = lastMouseX;
        state.lastMouseY = lastMouseY;
        state.repeatCount = repeatCount;
        if(!mouseTrackingEnabled)
        {
            recorder.anInt810 = 0;
            return state;
        }
        if(recorder.anInt810 < 40)
            return state;
        outboundBuffer.method397((byte)6, 45);
        outboundBuffer.method398(0);
        int payloadStart = outboundBuffer.anInt1406;
        int consumed = 0;
        for(int index = 0; index < recorder.anInt810; index++)
        {
            if(payloadStart - outboundBuffer.anInt1406 >= 240)
                break;
            consumed++;
            int mouseY = recorder.anIntArray807[index];
            if(mouseY < 0)
                mouseY = 0;
            else
            if(mouseY > 502)
                mouseY = 502;
            int mouseX = recorder.anIntArray809[index];
            if(mouseX < 0)
                mouseX = 0;
            else
            if(mouseX > 764)
                mouseX = 764;
            int packedPos = mouseY * 765 + mouseX;
            if(recorder.anIntArray807[index] == -1 && recorder.anIntArray809[index] == -1)
            {
                mouseX = -1;
                mouseY = -1;
                packedPos = 0x7ffff;
            }
            if(mouseX == state.lastMouseX && mouseY == state.lastMouseY)
            {
                if(state.repeatCount < 2047)
                    state.repeatCount++;
            } else
            {
                int dx = mouseX - state.lastMouseX;
                state.lastMouseX = mouseX;
                int dy = mouseY - state.lastMouseY;
                state.lastMouseY = mouseY;
                if(state.repeatCount < 8 && dx >= -32 && dx <= 31 && dy >= -32 && dy <= 31)
                {
                    dx += 32;
                    dy += 32;
                    outboundBuffer.method399((state.repeatCount << 12) + (dx << 6) + dy);
                    state.repeatCount = 0;
                } else
                if(state.repeatCount < 8)
                {
                    outboundBuffer.method401(0x800000 + (state.repeatCount << 19) + packedPos);
                    state.repeatCount = 0;
                } else
                {
                    outboundBuffer.method402(0xc0000000 + (state.repeatCount << 19) + packedPos);
                    state.repeatCount = 0;
                }
            }
        }

        outboundBuffer.method407(outboundBuffer.anInt1406 - payloadStart, (byte)0);
        if(consumed >= recorder.anInt810)
        {
            recorder.anInt810 = 0;
        } else
        {
            recorder.anInt810 -= consumed;
            for(int index = 0; index < recorder.anInt810; index++)
            {
                recorder.anIntArray809[index] = recorder.anIntArray809[index + consumed];
                recorder.anIntArray807[index] = recorder.anIntArray807[index + consumed];
            }

        }
        return state;
    }

    static long encodeMouseClickPacket(PacketBuffer outboundBuffer, int clickType, int mouseY, int mouseX, long currentTickTime, long previousTickTime)
    {
        long elapsed = (currentTickTime - previousTickTime) / 50L;
        if(elapsed > 4095L)
            elapsed = 4095L;
        int clampedMouseY = mouseY;
        if(clampedMouseY < 0)
            clampedMouseY = 0;
        else
        if(clampedMouseY > 502)
            clampedMouseY = 502;
        int clampedMouseX = mouseX;
        if(clampedMouseX < 0)
            clampedMouseX = 0;
        else
        if(clampedMouseX > 764)
            clampedMouseX = 764;
        int packedPos = clampedMouseY * 765 + clampedMouseX;
        int clickMode = clickType == 2 ? 1 : 0;
        outboundBuffer.method397((byte)6, 241);
        outboundBuffer.method402(((int)elapsed << 20) + (clickMode << 19) + packedPos);
        return currentTickTime;
    }

    static int flushOutbound(BufferedConnection connection, PacketBuffer outboundBuffer)
    {
        try
        {
            if(connection != null && outboundBuffer.anInt1406 > 0)
            {
                connection.method271(outboundBuffer.anInt1406, 0, outboundBuffer.aByteArray1405, 0);
                outboundBuffer.anInt1406 = 0;
                return 1;
            }
        }
        catch(IOException _ex)
        {
            return -1;
        }
        catch(Exception exception)
        {
            return -2;
        }
        return 0;
    }
}
