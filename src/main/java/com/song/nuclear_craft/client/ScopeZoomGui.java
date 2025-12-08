package com.song.nuclear_craft.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.song.nuclear_craft.NuclearCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.resources.ResourceLocation;

public class ScopeZoomGui implements Renderable {
    private static final ResourceLocation ZOOM_TEXTURE = ResourceLocation.fromNamespaceAndPath(NuclearCraft.MODID, "textures/gui/container/scope_zoom.png");
    public Minecraft mc;

    public ScopeZoomGui(Minecraft mc){
        this.mc = mc;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // 使用新的 render 方法
        drawGuiContainerBackgroundLayer(guiGraphics, 0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight());
    }

    public void drawGuiContainerBackgroundLayer(GuiGraphics guiGraphics, int i, int j, int xSize, int ySize) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        assert this.mc != null;
        
        // 使用 GuiGraphics 的 blit 方法
        guiGraphics.blit(ZOOM_TEXTURE, 0, 0, 0, 0, xSize, ySize, xSize, ySize);
    }
    
    // 为了向后兼容，保留 PoseStack 版本的方法
    public void drawGuiContainerBackgroundLayer(PoseStack matrixStack, int i, int j, int xSize, int ySize) {
        // 创建一个临时的 GuiGraphics 来调用新版本的方法
        GuiGraphics guiGraphics = new GuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource());
        drawGuiContainerBackgroundLayer(guiGraphics, i, j, xSize, ySize);
    }
}