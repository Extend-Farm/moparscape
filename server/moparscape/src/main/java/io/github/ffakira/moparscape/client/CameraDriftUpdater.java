package io.github.ffakira.moparscape.client;

final class CameraDriftUpdater {

    static final class DriftState {
        int primaryTimer;
        int primaryX;
        int primaryY;
        int primaryDeltaX;
        int primaryDeltaY;
        int primaryShake;
        int primaryDeltaShake;
        int secondaryTimer;
        int secondaryPitchOffset;
        int secondaryYawOffset;
        int secondaryPitchDelta;
        int secondaryYawDelta;
    }

    private CameraDriftUpdater() {
    }

    static DriftState update(
        int primaryTimer,
        int primaryX,
        int primaryY,
        int primaryDeltaX,
        int primaryDeltaY,
        int primaryShake,
        int primaryDeltaShake,
        int secondaryTimer,
        int secondaryPitchOffset,
        int secondaryYawOffset,
        int secondaryPitchDelta,
        int secondaryYawDelta
    ) {
        DriftState state = new DriftState();
        state.primaryTimer = primaryTimer;
        state.primaryX = primaryX;
        state.primaryY = primaryY;
        state.primaryDeltaX = primaryDeltaX;
        state.primaryDeltaY = primaryDeltaY;
        state.primaryShake = primaryShake;
        state.primaryDeltaShake = primaryDeltaShake;
        state.secondaryTimer = secondaryTimer;
        state.secondaryPitchOffset = secondaryPitchOffset;
        state.secondaryYawOffset = secondaryYawOffset;
        state.secondaryPitchDelta = secondaryPitchDelta;
        state.secondaryYawDelta = secondaryYawDelta;

        state.primaryTimer++;
        if(state.primaryTimer > 500)
        {
            state.primaryTimer = 0;
            int random = (int)(Math.random() * 8D);
            if((random & 1) == 1)
                state.primaryX += state.primaryDeltaX;
            if((random & 2) == 2)
                state.primaryY += state.primaryDeltaY;
            if((random & 4) == 4)
                state.primaryShake += state.primaryDeltaShake;
        }
        if(state.primaryX < -50)
            state.primaryDeltaX = 2;
        if(state.primaryX > 50)
            state.primaryDeltaX = -2;
        if(state.primaryY < -55)
            state.primaryDeltaY = 2;
        if(state.primaryY > 55)
            state.primaryDeltaY = -2;
        if(state.primaryShake < -40)
            state.primaryDeltaShake = 1;
        if(state.primaryShake > 40)
            state.primaryDeltaShake = -1;

        state.secondaryTimer++;
        if(state.secondaryTimer > 500)
        {
            state.secondaryTimer = 0;
            int random = (int)(Math.random() * 8D);
            if((random & 1) == 1)
                state.secondaryPitchOffset += state.secondaryPitchDelta;
            if((random & 2) == 2)
                state.secondaryYawOffset += state.secondaryYawDelta;
        }
        if(state.secondaryPitchOffset < -60)
            state.secondaryPitchDelta = 2;
        if(state.secondaryPitchOffset > 60)
            state.secondaryPitchDelta = -2;
        if(state.secondaryYawOffset < -20)
            state.secondaryYawDelta = 1;
        if(state.secondaryYawOffset > 10)
            state.secondaryYawDelta = -1;
        return state;
    }
}
