package com.song.nuclear_craft.items.guns;

import com.song.nuclear_craft.NuclearCraft;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import net.minecraft.world.item.Item.Properties;

public class Rocket extends Item {
    private final int tooltipLines;
    public Rocket(Properties properties, int tooltipLines) {
        super(properties);
        this.tooltipLines = tooltipLines;
    }

    public Rocket(Properties properties){
        this(properties, 0);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        for(int l=0; l<this.tooltipLines; l++){
            // Fix: Use new registry system to get item registry name
            ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(this);
            tooltip.add(Component.translatable(String.format("tooltip.%s.%s.line%d", NuclearCraft.MODID,
                    registryName != null ? registryName.getPath() : "unknown", l)));
        }
        tooltip.add(Component.translatable(String.format("tooltip.%s.rocket_load.line0", NuclearCraft.MODID)));
        tooltip.add(Component.translatable(String.format("tooltip.%s.rocket_load.line1", NuclearCraft.MODID)));
    }
}