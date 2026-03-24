package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.cache.Archive;
import io.github.ffakira.moparscape.net.PacketBuffer;

final class BootstrapConfigLoader {

    private BootstrapConfigLoader() {
    }

    static void loadTexturesAndConfig(GameClientCore core, Archive texturesArchive, Archive configArchive, byte textureBrightnessByte, boolean memberState)
    {
        core.method13(83, (byte)4, "Unpacking textures");
        Rasterizer3D.method368(texturesArchive, 0);
        Rasterizer3D.method372(0.80000000000000004D, textureBrightnessByte);
        Rasterizer3D.method367(20, true);
        core.method13(86, (byte)4, "Unpacking config");
        SequenceDefinition.method257(0, configArchive);
        ObjectDefinition.method576(configArchive);
        FloorDefinition.method260(0, configArchive);
        ItemDefinition.method193(configArchive);
        NpcDefinition.method162(configArchive);
        IdentityKitDefinition.method535(0, configArchive);
        SpotAnimationDefinition.method264(0, configArchive);
        VarpDefinition.method546(0, configArchive);
        VarBitDefinition.method533(0, configArchive);
        ItemDefinition.aBoolean182 = memberState;
    }

    static void loadSoundsIfNeeded(GameClientCore core, boolean skipSounds, Archive soundsArchive)
    {
        if(skipSounds)
            return;
        core.method13(90, (byte)4, "Unpacking sounds");
        byte soundData[] = soundsArchive.method571("sounds.dat", null);
        PacketBuffer packetBuffer = new PacketBuffer(soundData, 891);
        SoundEffect.method240(0, packetBuffer);
    }

    static void loadInterfaces(GameClientCore core, Archive interfaceArchive, Archive mediaArchive, FontRenderer[] fonts)
    {
        core.method13(95, (byte)4, "Unpacking interfaces");
        Widget.method205(interfaceArchive, fonts, (byte)-84, mediaArchive);
    }
}
