package com.song.nuclear_craft.blocks.container;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.song.nuclear_craft.NuclearCraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class C4BombContainerScreen extends AbstractContainerScreen<C4BombContainer> {
    public int i;
    public int j;

    private static final ResourceLocation C4_GUI_TEXTURES = ResourceLocation.fromNamespaceAndPath(NuclearCraft.MODID, "textures/gui/container/c4_bomb_container.png");

    public C4BombContainerScreen(C4BombContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        // Set GUI size, only show C4-specific part, excluding player inventory
        this.imageWidth = 176; // 根据GUI纹理的实际宽度调整
        this.imageHeight = 166; // 根据GUI纹理的实际高度调整
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouse_x, int mouse_y) {
        i = (this.width - this.imageWidth) / 2;
        j = (this.height - this.imageHeight) / 2;
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(C4_GUI_TEXTURES, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int x, int y) {
        PoseStack poseStack = guiGraphics.pose();
        // Use GuiGraphics drawString method
        guiGraphics.drawString(this.font, this.menu.tileEntity.inputPanel + " s", 83, 13, 4210752, false);
        
        if (this.menu.tileEntity.isActive()) {
            guiGraphics.drawString(this.font, 
                Component.translatable("menu." + NuclearCraft.MODID + ".c4_bomb.counter").getString() + 
                this.menu.tileEntity.getCounter() + " s", 33, 33, 4210752, false);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}