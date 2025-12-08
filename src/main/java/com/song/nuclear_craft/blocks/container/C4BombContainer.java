package com.song.nuclear_craft.blocks.container;

import com.song.nuclear_craft.blocks.tileentity.C4BombTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;

import java.util.Objects;

public class C4BombContainer extends AbstractContainerMenu {
    public final C4BombTileEntity tileEntity;
    // private final IWorldPosCallable canInteractWithCallable;

    public C4BombContainer(int windowId, Inventory playerInventory, C4BombTileEntity tileEntity){
        // Fix: Use .get() to get the actual MenuType
        super(ContainerTypeList.C4_BOMB_CONTAINER_TYPE.get(), windowId);
        this.tileEntity = tileEntity;
        
        // Remove player inventory slots, only show C4-specific GUI
        // Do not add any slots, as C4 GUI is only for settings and display, no item interaction needed
    }

    public C4BombContainer(int windowId, Inventory playerInventory, FriendlyByteBuf data){
        this(windowId, playerInventory, getTEntity(playerInventory, data));
    }

    public static C4BombTileEntity getTEntity(Inventory playerInventory, FriendlyByteBuf data){
        Objects.requireNonNull(playerInventory, "playerInv cannot be null");
        if(data == null){
            return null;
        }
        BlockPos blockPos = data.readBlockPos();
        BlockEntity entity = playerInventory.player.getCommandSenderWorld().getBlockEntity(blockPos);
        if (entity instanceof C4BombTileEntity){
            return (C4BombTileEntity)entity;
        }else {
            throw new IllegalStateException("tile entity is not correct at" + blockPos);
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        // TODO: Implement correct interaction distance check
        // return isWithinUsableDistance(canInteractWithCallable, playerIn, BlockList.C4_ATOMIC_BOMB);
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // Since the container has no slots (including player inventory), quick move operation is not applicable
        return ItemStack.EMPTY;
    }
    
    // Add player inventory slots
    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }
    
    // Add player hotbar slots
    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}