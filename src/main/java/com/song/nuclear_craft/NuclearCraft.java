package com.song.nuclear_craft;

import com.song.nuclear_craft.blocks.BlockList;
import com.song.nuclear_craft.blocks.container.ContainerTypeList; // Ensure correct import
import com.song.nuclear_craft.blocks.tileentity.TileEntityRegister;
import com.song.nuclear_craft.client.ClientSetup;
import com.song.nuclear_craft.effects.EffectRegister;
import com.song.nuclear_craft.entities.EntityRegister;
import com.song.nuclear_craft.items.Ammo.AmmoSize;
import com.song.nuclear_craft.items.Ammo.AmmoType;
import com.song.nuclear_craft.items.ItemList;
import com.song.nuclear_craft.misc.ConfigClient;
import com.song.nuclear_craft.misc.ConfigCommon;
import com.song.nuclear_craft.commands.NuclearCommand;
import com.song.nuclear_craft.network.NuclearCraftPacketHandler;
import com.song.nuclear_craft.particles.*;
import com.song.nuclear_craft.misc.SoundEventList;
import net.minecraftforge.event.server.ServerStartingEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;



@Mod(NuclearCraft.MODID)
public class NuclearCraft
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "nuclear_craft";
    
    public static final CreativeModeTab ITEM_GROUP = CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.weapons"))
        .icon(() -> new ItemStack(ItemList.ATOMIC_BOMB_ROCKET.get()))
        .displayItems((pParameters, pOutput) -> {
            // Manually add items to item group
            // Weapons
            pOutput.accept(ItemList.ROCKET_LAUNCHER.get());
            pOutput.accept(ItemList.ROCKET_LAUNCHER_ATOMIC_BOMB.get());
            pOutput.accept(ItemList.ROCKET_LAUNCHER_INCENDIARY.get());
            pOutput.accept(ItemList.ROCKET_LAUNCHER_SMOKE.get());
            pOutput.accept(ItemList.ROCKET_LAUNCHER_HIGH_EXPLOSIVE.get());
            pOutput.accept(ItemList.ROCKET_LAUNCHER_WATER_DROP.get());
            pOutput.accept(ItemList.DESERT_EAGLE.get());
            pOutput.accept(ItemList.GLOCK.get());
            pOutput.accept(ItemList.FN57.get());
            pOutput.accept(ItemList.USP.get());
            pOutput.accept(ItemList.AK47.get());
            pOutput.accept(ItemList.AWP.get());
            pOutput.accept(ItemList.BARRETT.get());
            pOutput.accept(ItemList.M4A4.get());
            pOutput.accept(ItemList.XM1014.get());
            pOutput.accept(ItemList.NOVA.get());
            pOutput.accept(ItemList.P90.get());
            
            // Defuse kits
            pOutput.accept(ItemList.WOOD_DEFUSE_KIT.get());
            pOutput.accept(ItemList.IRON_DEFUSE_KIT.get());
            pOutput.accept(ItemList.GOLD_DEFUSE_KIT.get());
            pOutput.accept(ItemList.DIAMOND_DEFUSE_KIT.get());
            pOutput.accept(ItemList.NETHERITE_DEFUSE_KIT.get());
            
            // Rockets
            pOutput.accept(ItemList.ATOMIC_BOMB_ROCKET.get());
            pOutput.accept(ItemList.INCENDIARY_ROCKET.get());
            pOutput.accept(ItemList.SMOKE_ROCKET.get());
            pOutput.accept(ItemList.HIGH_EXPLOSIVE_ROCKET.get());
            pOutput.accept(ItemList.WATER_DROP_ROCKET.get());
            
            // C4 Bombs
            pOutput.accept(ItemList.C4_ATOMIC_BOMB.get());
            pOutput.accept(ItemList.C4_HIGH_EXPLOSIVE.get());
            pOutput.accept(ItemList.C4_INCENDIARY.get());
            pOutput.accept(ItemList.C4_SMOKE.get());
            
            // Statues
            pOutput.accept(ItemList.STATUE_OF_LIBERTY.get());
            pOutput.accept(ItemList.STATUE_OF_RIFLE_AMMO.get());
            pOutput.accept(ItemList.STATUE_OF_SHOTGUN_AMMO.get());
            pOutput.accept(ItemList.STATUE_OF_ROCKET.get());
            pOutput.accept(ItemList.STATUE_OF_EXPLOSIVE.get());
        })
        .build();
        
    public static final CreativeModeTab AMMO_ITEM_GROUP = CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.bullets"))
        .icon(() -> new ItemStack(ItemList.AMMO_REGISTRIES_TYPE.get(AmmoSize.SIZE_127).get(AmmoType.NORMAL).get()))
        .displayItems((pParameters, pOutput) -> {
            // Manually add ammo to item group
            for (AmmoSize ammoSize : AmmoSize.values()) {
                if (ItemList.AMMO_REGISTRIES_TYPE.containsKey(ammoSize)) {
                    for (AmmoType ammoType : AmmoType.values()) {
                        if (ItemList.AMMO_REGISTRIES_TYPE.get(ammoSize).containsKey(ammoType)) {
                            pOutput.accept(ItemList.AMMO_REGISTRIES_TYPE.get(ammoSize).get(ammoType).get());
                        }
                    }
                }
            }
            
            // Add birdshot
            for (AmmoType ammoType : ItemList.BIRD_SHOT_MAP.keySet()) {
                pOutput.accept(ItemList.BIRD_SHOT_MAP.get(ammoType).get());
            }
        })
        .build();

    public NuclearCraft(FMLJavaModLoadingContext context, ModLoadingContext modLoadingContext) {
        // Register event listeners
        context.getModEventBus().addListener(this::setup);
        context.getModEventBus().addListener(this::enqueueIMC);
        context.getModEventBus().addListener(this::processIMC);
        context.getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);

        // Register all DeferredRegisters
        EntityRegister.ENTITIES.register(context.getModEventBus());
        ParticleRegister.PARTICLES.register(context.getModEventBus());
        ItemList.ITEMS.register(context.getModEventBus());
        BlockList.BLOCKS.register(context.getModEventBus());
        SoundEventList.SOUND_EVENTS.register(context.getModEventBus());
        
        EffectRegister.EFFECTS.register(context.getModEventBus());
        TileEntityRegister.TILE_ENTITY_TYPES.register(context.getModEventBus());
        ContainerTypeList.CONTAINERS.register(context.getModEventBus()); // 现在应该能正常工作了

        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, ConfigClient.CLIENT);
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigCommon.COMMON);
    }
    
    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        
        
        NuclearCraftPacketHandler.register();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ClientSetup.clientSetup(event);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("examplemod", "helloworld", () -> { 
            LOGGER.info("Hello world from the MDK"); 
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        LOGGER.info("Got IMC {}", event.getIMCStream()
                .map(InterModComms.IMCMessage::messageSupplier)
                .collect(Collectors.toList()));
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        NuclearCommand.register(event.getServer().getCommands().getDispatcher());
    }
    
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        
        @SubscribeEvent
        public static void onParticleFactoryRegistry(final RegisterParticleProvidersEvent event){
            Minecraft.getInstance().particleEngine.register(
                ParticleRegister.NUKE_PARTICLE_SMOKE.get(), 
                BigSmokeParticle.NukeParticleFactory::new
            );
            Minecraft.getInstance().particleEngine.register(
                ParticleRegister.NUKE_PARTICLE_FIRE.get(), 
                BigSmokeParticle.NukeParticleFactory::new
            );
            Minecraft.getInstance().particleEngine.register(
                ParticleRegister.BIG_SMOKE.get(), 
                BigSmokeParticle.BigSmokeFactory::new
            );
            Minecraft.getInstance().particleEngine.register(
                ParticleRegister.RESTRICTED_HEIGHT_SMOKE_PARTICLE.get(), 
                RestrictedSmokeParticle.RestrictedHeightFactory::new
            );
            Minecraft.getInstance().particleEngine.register(
                ParticleRegister.MUSHROOM_SMOKE_PARTICLE.get(), 
                RestrictedSmokeParticle.MushroomFactory::new
            );
            Minecraft.getInstance().particleEngine.register(
                ParticleRegister.SHOCK_WAVE.get(), 
                ShockWaveParticle.Factory::new
            );
            Minecraft.getInstance().particleEngine.register(
                ParticleRegister.EXPLODE_CORE.get(), 
                ExplodeCoreParticle.Factory::new
            );
        }
    }
}