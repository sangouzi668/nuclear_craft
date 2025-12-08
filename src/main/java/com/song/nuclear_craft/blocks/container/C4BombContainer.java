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
        // 修复：使用 .get() 获取实际的 MenuType
        super(ContainerTypeList.C4_BOMB_CONTAINER_TYPE.get(), windowId);
        this.tileEntity = tileEntity;
        
        // 移除玩家物品栏槽位，只显示C4专用GUI
        // 不添加任何槽位，因为C4 GUI只是用于设置和显示，不需要物品交互
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
        // TODO: 实现正确的交互距离检查
        // return isWithinUsableDistance(canInteractWithCallable, playerIn, BlockList.C4_ATOMIC_BOMB);
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // 由于容器没有任何槽位（包括玩家物品栏），快速移动操作不适用
        return ItemStack.EMPTY;
    }
    
    // 添加玩家物品栏槽位
    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }
    
    // 添加玩家快捷栏槽位
    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}