package com.binaris.wizardry.client.gui;

import com.binaris.wizardry.content.item.wand.WandItem;
import com.binaris.wizardry.registry.SpellTier;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import static com.binaris.wizardry.client.gui.ArcaneWorkbenchScreen.TOOLTIP_BORDER;
import static com.binaris.wizardry.client.gui.ArcaneWorkbenchScreen.TOOLTIP_WIDTH;

public class TooltipElementProgressionBar extends TooltipElement {
    private final int height;

    public TooltipElementProgressionBar(int height, int spaceAfter) {
        super(spaceAfter);
        this.height = height;
    }

    @Override
    protected boolean isVisible(ItemStack stack) {
        return stack.getItem() instanceof WandItem;
    }

    @Override
    protected int getHeight(ItemStack stack) {
        return Minecraft.getInstance().font.lineHeight + ArcaneWorkbenchScreen.LINE_SPACING_NARROW + ArcaneWorkbenchScreen.PROGRESSION_BAR_HEIGHT;
    }

    @Override
    protected void drawBackground(GuiGraphics guiGraphics, int x, int y, ItemStack stack, float partialTicks, int mouseX, int mouseY) {
        y += Minecraft.getInstance().font.lineHeight + ArcaneWorkbenchScreen.LINE_SPACING_NARROW;

        float progressFraction = 1.0f;
        SpellTier nextTier = getNextTier(stack);
        if (nextTier != null && stack.getItem() instanceof WandItem wand) {
            progressFraction = Math.min(1.0f, (float) wand.getProgression(stack) / nextTier.getProgression());
        }

        // Empty bar background
        guiGraphics.blit(ArcaneWorkbenchScreen.ARCANE_WORKBENCH_CONTAINER_TEXTURE, x, y,
                ArcaneWorkbenchScreen.MAIN_GUI_WIDTH, height + ArcaneWorkbenchScreen.PROGRESSION_BAR_HEIGHT,
                ArcaneWorkbenchScreen.PROGRESSION_BAR_WIDTH, ArcaneWorkbenchScreen.PROGRESSION_BAR_HEIGHT,
                ArcaneWorkbenchScreen.TEXTURE_WIDTH, ArcaneWorkbenchScreen.TEXTURE_HEIGHT);

        // Filled bar (progress)
        int filledWidth = (int) (ArcaneWorkbenchScreen.PROGRESSION_BAR_WIDTH * progressFraction);
        if (filledWidth > 0) {
            guiGraphics.blit(ArcaneWorkbenchScreen.ARCANE_WORKBENCH_CONTAINER_TEXTURE, x, y,
                    ArcaneWorkbenchScreen.MAIN_GUI_WIDTH, height,
                    filledWidth, ArcaneWorkbenchScreen.PROGRESSION_BAR_HEIGHT,
                    ArcaneWorkbenchScreen.TEXTURE_WIDTH, ArcaneWorkbenchScreen.TEXTURE_HEIGHT);
        }
    }

    @Override
    protected void drawForeground(GuiGraphics guiGraphics, int x, int y, ItemStack stack, int mouseX, int mouseY) {
        if (!(stack.getItem() instanceof WandItem wand)) return;

        SpellTier tier = wand.getTier();
        Component tierDesc = tier.getDescriptionFormatted();
        TextColor tierColor = tierDesc.getStyle().getColor();
        int colorVal = tierColor != null ? tierColor.getValue() : ChatFormatting.WHITE.getColor();
        guiGraphics.drawString(Minecraft.getInstance().font, tierDesc, x, y, colorVal, true);

        SpellTier nextTier = getNextTier(stack);
        if (nextTier != null) {
            Component s = nextTier.getDescriptionFormatted().copy().withStyle(ChatFormatting.DARK_GRAY);
            if (wand.getProgression(stack) >= nextTier.getProgression()) {
                s = nextTier.getDescriptionFormatted();
            }
            TextColor nextColor = s.getStyle().getColor();
            int nextColorVal = nextColor != null ? nextColor.getValue() : ChatFormatting.GRAY.getColor();
            int strWidth = Minecraft.getInstance().font.width(s);
            guiGraphics.drawString(Minecraft.getInstance().font, s, x + TOOLTIP_WIDTH - TOOLTIP_BORDER * 2 - strWidth, y, nextColorVal, true);
        }
    }

    private @Nullable SpellTier getNextTier(ItemStack stack) {
        if (stack.getItem() instanceof WandItem wand) {
            SpellTier tier = wand.getTier();
            if (tier != SpellTiers.MASTER.get()) {
                return SpellTiers.getNextByLevel(tier.getLevel() + 1);
            }
        }
        return null;
    }
}
