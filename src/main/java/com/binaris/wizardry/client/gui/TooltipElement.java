package com.binaris.wizardry.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public abstract class TooltipElement {
    public final int spaceAfter;
    private final TooltipElement[] children;

    public TooltipElement(int spaceAfter, TooltipElement... children) {
        this.spaceAfter = spaceAfter;
        this.children = children;
    }

    public int getTotalHeight(ItemStack stack) {
        if (!this.isVisible(stack)) return 0;
        int height = this.getHeight(stack);
        for (TooltipElement child : children) {
            height += child.getTotalHeight(stack);
        }
        return height + spaceAfter;
    }

    public int drawBackgroundLayer(GuiGraphics guiGraphics, int x, int y, ItemStack stack, float partialTicks, int mouseX, int mouseY) {
        if (!this.isVisible(stack)) return y;
        this.drawBackground(guiGraphics, x, y, stack, partialTicks, mouseX, mouseY);
        y += this.getHeight(stack);
        for (TooltipElement child : children) {
            y = child.drawBackgroundLayer(guiGraphics, x, y, stack, partialTicks, mouseX, mouseY);
        }
        return y + spaceAfter;
    }

    public int drawForegroundLayer(GuiGraphics guiGraphics, int x, int y, ItemStack stack, int mouseX, int mouseY) {
        if (!this.isVisible(stack)) return y;
        this.drawForeground(guiGraphics, x, y, stack, mouseX, mouseY);
        y += this.getHeight(stack);
        for (TooltipElement child : children) {
            y = child.drawForegroundLayer(guiGraphics, x, y, stack, mouseX, mouseY);
        }
        return y + spaceAfter;
    }

    protected abstract boolean isVisible(ItemStack stack);

    protected abstract int getHeight(ItemStack stack);

    protected abstract void drawBackground(GuiGraphics guiGraphics, int x, int y, ItemStack stack, float partialTicks, int mouseX, int mouseY);

    protected abstract void drawForeground(GuiGraphics guiGraphics, int x, int y, ItemStack stack, int mouseX, int mouseY);
}
