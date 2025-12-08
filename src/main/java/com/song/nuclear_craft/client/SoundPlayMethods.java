package com.song.nuclear_craft.client;

import com.song.nuclear_craft.misc.SoundEventList;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class SoundPlayMethods {
    public static void c4Beep(BlockPos pos){
        // CLIENT SIDE ONLY
        Level world = Minecraft.getInstance().level;
        assert world != null;
        world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.C4_BEEP.get(), SoundSource.BLOCKS, 1f, 1.0f, false);
    }

    public static void activeSound(BlockPos pos){
        Level world = Minecraft.getInstance().level;
        assert world != null;
        world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.BOMB_PLANTED.get(), SoundSource.PLAYERS, 1f, 1.0f, true);
    }

    public static void playLoadSound(BlockPos pos){
        // Rocket launcher load sound
        Level world = Minecraft.getInstance().level;
        assert world != null;
        world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.ROCKET_LOAD.get(), SoundSource.PLAYERS, 1f, 1.0f, true);
    }

    public static void playSoundFromString(BlockPos pos, String name){
        Level world = Minecraft.getInstance().level;
        assert world != null;
        if(name==null){
            return;
        }
        switch (name){
            case "c4_beep":
                SoundPlayMethods.c4Beep(new BlockPos(pos.getX(), pos.getY(), pos.getZ())); break;
            case "c4_activate":
                SoundPlayMethods.activeSound(new BlockPos(pos.getX(), pos.getY(), pos.getZ())); break;
            case "rocket_load":
                SoundPlayMethods.playLoadSound(new BlockPos(pos.getX(), pos.getY(), pos.getZ())); break;
            case "desert_eagle":
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.DESERT_EAGLE.get(), SoundSource.PLAYERS, 0.25f, 1.0f, false); break;
            case "no_ammo":
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.NO_AMMO.get(), SoundSource.PLAYERS, 1f, 1.0f, false); break;
            case "de_reload_empty":
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.DE_RELOAD_EMPTY.get(), SoundSource.PLAYERS, 1f, 1.0f, false); break;
            case "glock":
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.GLOCK.get(), SoundSource.PLAYERS, 0.25f, 1.0f, false); break;
            case "fn57":
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.FN57.get(), SoundSource.PLAYERS, 0.4f, 1.0f, false); break;
            case "usp":
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.USP.get(), SoundSource.PLAYERS, 0.25f, 1.0f, false); break;
            case "ak47":
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.AK47.get(), SoundSource.PLAYERS, 0.25f, 1.0f, false); break;
            case "ak47_reload":
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.AK47_RELOAD.get(), SoundSource.PLAYERS, 1f, 1.0f, false); break;
            case "awp":
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.AWP.get(), SoundSource.PLAYERS, 0.25f, 1.0f, false); break;
            case "awp_reload":
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.AWP_RELOAD.get(), SoundSource.PLAYERS, 1f, 1.0f, false); break;
            case "barrett":
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.BARRETT.get(), SoundSource.PLAYERS, 1f, 1.0f, false); break;
            case "m4a4":
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.M4A4.get(), SoundSource.PLAYERS, 1f, 1.0f, false); break;
            case "m4a4_reload":
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.M4A4_RELOAD.get(), SoundSource.PLAYERS, 1f, 1.0f, false); break;
            case "xm1014":
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.XM1014.get(), SoundSource.PLAYERS, 0.25f, 1.0f, false); break;
            case "xm1014_reload":
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.XM1014_RELOAD.get(), SoundSource.PLAYERS, 1.5f, 1.0f, false); break;
            case "nova":
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.NOVA.get(), SoundSource.PLAYERS, 0.25f, 1.0f, false); break;
            case "p90":
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.P90.get(), SoundSource.PLAYERS, 1f, 1.0f, false); break;
            case "p90_reload":
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.P90_RELOAD.get(), SoundSource.PLAYERS, 1f, 1.0f, false); break;
            case "defusing":
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.DEFUSING.get(), SoundSource.PLAYERS, 5f, 1.0f, false); break;
            case "defused":
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEventList.DEFUSED.get(), SoundSource.PLAYERS, 1f, 1.0f, false); break;
        }
    }
}
