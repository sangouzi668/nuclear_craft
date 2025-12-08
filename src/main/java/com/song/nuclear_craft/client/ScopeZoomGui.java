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
        // Use new render method
        drawGuiContainerBackgroundLayer(guiGraphics, 0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight());
    }

    public void drawGuiContainerBackgroundLayer(GuiGraphics guiGraphics, int i, int j, int xSize, int ySize) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        assert this.mc != null;
        
        // Use GuiGraphics blit method
        guiGraphics.blit(ZOOM_TEXTURE, 0, 0, 0, 0, xSize, ySize, xSize, ySize);
    }
    
    // Keep PoseStack version method for backward compatibility
    public void drawGuiContainerBackgroundLayer(PoseStack matrixStack, int i, int j, int xSize, int ySize) {
        // Create a temporary GuiGraphics to call the new version method
        GuiGraphics guiGraphics = new GuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource());
        drawGuiContainerBackgroundLayer(guiGraphics, i, j, xSize, ySize);
    }
}