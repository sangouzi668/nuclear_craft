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
        // 初始化按键绑定
        gunReload = new KeyMapping("key."+ NuclearCraft.MODID+".load_ammo", GLFW.GLFW_KEY_R, "key."+NuclearCraft.MODID+".categories");
        zoom = new KeyMapping("key."+NuclearCraft.MODID+".zoom", GLFW.GLFW_KEY_Z, "key."+NuclearCraft.MODID+".categories");

        // 注册容器屏幕（需要在主线程中执行）
        event.enqueueWork(() -> {
            // 修复1: 使用 .get() 获取 MenuType
            MenuScreens.register(ContainerTypeList.C4_BOMB_CONTAINER_TYPE.get(), C4BombContainerScreen::new);
        });

        // 注册实体渲染器
        net.minecraft.client.renderer.entity.EntityRenderers.register(EntityRegister.BULLET_ENTITY.get(), ThrownItemRenderer::new);
        net.minecraft.client.renderer.entity.EntityRenderers.register(EntityRegister.NUKE_EXPLOSION_HANDLER_TYPE.get(), NukeHandlerRenderer::new);

        // 注册事件处理器
        MinecraftForge.EVENT_BUS.register(new ClientEventForgeSubscriber());
        MinecraftForge.EVENT_BUS.register(new ClientEventBusSubscriber());
    }
    
    // 使用事件来注册按键映射
    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        // 初始化按键绑定（如果尚未初始化）
        if (gunReload == null) {
            gunReload = new KeyMapping("key."+ NuclearCraft.MODID+".load_ammo", GLFW.GLFW_KEY_R, "key."+NuclearCraft.MODID+".categories");
        }
        if (zoom == null) {
            zoom = new KeyMapping("key."+NuclearCraft.MODID+".zoom", GLFW.GLFW_KEY_Z, "key."+NuclearCraft.MODID+".categories");
        }
        
        // 注册按键映射
        event.register(gunReload);
        event.register(zoom);
    }
}