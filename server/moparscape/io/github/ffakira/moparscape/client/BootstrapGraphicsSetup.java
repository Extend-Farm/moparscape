package io.github.ffakira.moparscape.client;

final class BootstrapGraphicsSetup {

    static final class State {
        int[] viewportScanlineOffsets;
        int[] chatboxScanlineOffsets;
        int[] fullScreenScanlineOffsets;
    }

    private BootstrapGraphicsSetup() {
    }

    static void prepareMapbackClipMasks(IndexedSprite mapBack, int[] topClipStarts, int[] topClipWidths, int[] sideClipStarts, int[] sideClipWidths)
    {
        for(int row = 0; row < 33; row++)
        {
            int start = 999;
            int end = 0;
            for(int column = 0; column < 34; column++)
            {
                if(mapBack.aByteArray1450[column + row * mapBack.anInt1452] == 0)
                {
                    if(start == 999)
                        start = column;
                    continue;
                }
                if(start == 999)
                    continue;
                end = column;
                break;
            }

            topClipStarts[row] = start;
            topClipWidths[row] = end - start;
        }

        for(int row = 5; row < 156; row++)
        {
            int start = 999;
            int end = 0;
            for(int column = 25; column < 172; column++)
            {
                if(mapBack.aByteArray1450[column + row * mapBack.anInt1452] == 0 && (column > 34 || row > 34))
                {
                    if(start == 999)
                        start = column;
                    continue;
                }
                if(start == 999)
                    continue;
                end = column;
                break;
            }

            sideClipStarts[row - 5] = start - 25;
            sideClipWidths[row - 5] = end - start;
        }
    }

    static State initializeRasterizerAndSceneVisibility(boolean softwareVisibility)
    {
        State state = new State();
        Rasterizer3D.method365(-950, 479, 96);
        state.viewportScanlineOffsets = Rasterizer3D.anIntArray1472;
        Rasterizer3D.method365(-950, 190, 261);
        state.chatboxScanlineOffsets = Rasterizer3D.anIntArray1472;
        Rasterizer3D.method365(-950, 512, 334);
        state.fullScreenScanlineOffsets = Rasterizer3D.anIntArray1472;
        int visibilityMap[] = new int[9];
        for(int index = 0; index < 9; index++)
        {
            int angle = 128 + index * 32 + 15;
            int perspective = 600 + angle * 3;
            int sine = Rasterizer3D.anIntArray1470[angle];
            visibilityMap[index] = perspective * sine >> 16;
        }

        SceneGraph.method310(500, 800, 512, 334, visibilityMap, softwareVisibility);
        return state;
    }
}
