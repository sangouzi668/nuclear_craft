package com.song.nuclear_craft.client;

import com.song.nuclear_craft.NuclearCraft;
import com.song.nuclear_craft.blocks.container.C4BombContainerScreen;
import com.song.nuclear_craft.blocks.container.ContainerTypeList;
import com.song.nuclear_craft.entities.EntityRegister;
import com.song.nuclear_craft.events.ClientEventBusSubscriber;
import com.song.nuclear_craft.events.ClientEventForgeSubscriber;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = NuclearCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    
    public static KeyMapping gunReload;
    public static KeyMapping zoom;
    
    public static void clientSetup(final FMLClientSetupEvent event){
        // Initialize key bindings (if not already initialized)
        if (gunReload == null) {
            gunReload = new KeyMapping("key."+ NuclearCraft.MODID+".load_ammo", GLFW.GLFW_KEY_R, "key."+NuclearCraft.MODID+".categories");
        }
        if (zoom == null) {
            zoom = new KeyMapping("key."+NuclearCraft.MODID+".zoom", GLFW.GLFW_KEY_Z, "key."+NuclearCraft.MODID+".categories");
        }

        // Register container screens (must be executed in main thread)
        event.enqueueWork(() -> {
            // Fix 1: Use .get() to get MenuType
            MenuScreens.register(ContainerTypeList.C4_BOMB_CONTAINER_TYPE.get(), C4BombContainerScreen::new);
        });

        // Register entity renderers
        net.minecraft.client.renderer.entity.EntityRenderers.register(EntityRegister.BULLET_ENTITY.get(), ThrownItemRenderer::new);
        net.minecraft.client.renderer.entity.EntityRenderers.register(EntityRegister.NUKE_EXPLOSION_HANDLER_TYPE.get(), NukeHandlerRenderer::new);

        // Register event handlers
        MinecraftForge.EVENT_BUS.register(new ClientEventForgeSubscriber());
        MinecraftForge.EVENT_BUS.register(new ClientEventBusSubscriber());
    }
    
    // Use events to register key mappings
    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        // Initialize key bindings (if not already initialized)
        if (gunReload == null) {
            gunReload = new KeyMapping("key."+ NuclearCraft.MODID+".load_ammo", GLFW.GLFW_KEY_R, "key."+NuclearCraft.MODID+".categories");
        }
        if (zoom == null) {
            zoom = new KeyMapping("key."+NuclearCraft.MODID+".zoom", GLFW.GLFW_KEY_Z, "key."+NuclearCraft.MODID+".categories");
        }
        
        // Register key mappings
        event.register(gunReload);
        event.register(zoom);
    }
}