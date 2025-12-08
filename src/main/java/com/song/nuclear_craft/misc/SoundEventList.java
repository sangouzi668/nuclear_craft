package com.song.nuclear_craft.misc;

import com.song.nuclear_craft.NuclearCraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundEventList {
    // 创建延迟注册器（推荐方式）
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = 
        DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, NuclearCraft.MODID);
    
    // 注册所有声音事件
    public static final RegistryObject<SoundEvent> BOMB_PLANTED = 
        registerSoundEvent("bomb_planted");
    public static final RegistryObject<SoundEvent> C4_BEEP = 
        registerSoundEvent("c4_beep");
    public static final RegistryObject<SoundEvent> ROCKET_LOAD = 
        registerSoundEvent("loading");
    public static final RegistryObject<SoundEvent> DESERT_EAGLE = 
        registerSoundEvent("desert_eagle");
    public static final RegistryObject<SoundEvent> NO_AMMO = 
        registerSoundEvent("no_ammo");
    public static final RegistryObject<SoundEvent> DE_RELOAD_EMPTY = 
        registerSoundEvent("de_reload_empty");
    public static final RegistryObject<SoundEvent> GLOCK = 
        registerSoundEvent("glock");
    public static final RegistryObject<SoundEvent> FN57 = 
        registerSoundEvent("fn57");
    public static final RegistryObject<SoundEvent> USP = 
        registerSoundEvent("usp");
    public static final RegistryObject<SoundEvent> AK47 = 
        registerSoundEvent("ak47");
    public static final RegistryObject<SoundEvent> AK47_RELOAD = 
        registerSoundEvent("ak47_reload");
    public static final RegistryObject<SoundEvent> AWP = 
        registerSoundEvent("awp");
    public static final RegistryObject<SoundEvent> AWP_RELOAD = 
        registerSoundEvent("awp_reload");
    public static final RegistryObject<SoundEvent> ZOOM = 
        registerSoundEvent("zoom");
    public static final RegistryObject<SoundEvent> BARRETT = 
        registerSoundEvent("barrett");
    public static final RegistryObject<SoundEvent> M4A4 = 
        registerSoundEvent("m4a4");
    public static final RegistryObject<SoundEvent> M4A4_RELOAD = 
        registerSoundEvent("m4a4_reload");
    public static final RegistryObject<SoundEvent> XM1014 = 
        registerSoundEvent("xm1014");
    public static final RegistryObject<SoundEvent> XM1014_RELOAD = 
        registerSoundEvent("xm1014_reload");
    public static final RegistryObject<SoundEvent> NOVA = 
        registerSoundEvent("nova");
    public static final RegistryObject<SoundEvent> P90 = 
        registerSoundEvent("p90");
    public static final RegistryObject<SoundEvent> P90_RELOAD = 
        registerSoundEvent("p90_reload");
    public static final RegistryObject<SoundEvent> DEFUSING = 
        registerSoundEvent("defusing");
    public static final RegistryObject<SoundEvent> DEFUSED = 
        registerSoundEvent("defused");
    
    // 辅助注册方法
    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> 
            SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(NuclearCraft.MODID, name)));
    }
    
    // 旧式直接创建方法（不推荐，仅作参考）
    /*
    public static SoundEvent BOMB_PLANTED = 
        SoundEvent.createVariableRangeEvent(new ResourceLocation(NuclearCraft.MODID, "bomb_planted"));
    */
}