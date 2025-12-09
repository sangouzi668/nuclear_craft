package com.song.nuclear_craft.entities.containers;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;

public class UraniumCentrifugeContainer extends AbstractContainerMenu {

    protected UraniumCentrifugeContainer(@Nullable MenuType<?> type, int id) {
        super(type, id);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // Simple quick move implementation, modify according to your container needs
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            
            // Need to implement specific quick move logic based on your container layout here
            // This is a basic template, needs adjustment based on actual slot count
            
            if (index < 36) {
                // Move from player inventory to container
                if (!this.moveItemStackTo(itemstack1, 36, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Move from container to player inventory
                if (!this.moveItemStackTo(itemstack1, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }
            
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        
        return itemstack;
    }
}