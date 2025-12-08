package com.song.nuclear_craft.blocks.container;

import com.song.nuclear_craft.NuclearCraft;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerTypeList {
    // 创建 DeferredRegister
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, NuclearCraft.MODID);
    
    // 使用 RegistryObject 注册容器类型
    public static final RegistryObject<MenuType<C4BombContainer>> C4_BOMB_CONTAINER_TYPE = CONTAINERS.register("c4_bomb_container", 
        () -> IForgeMenuType.create((windowId, inv, data) -> new C4BombContainer(windowId, inv, data)));
}