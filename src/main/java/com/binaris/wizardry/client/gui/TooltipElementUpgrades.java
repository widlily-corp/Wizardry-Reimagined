package com.binaris.wizardry.client.gui;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.item.wand.WandItem;
import com.binaris.wizardry.content.item.wand.WandUpgrades;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import static com.binaris.wizardry.client.gui.ArcaneWorkbenchScreen.TOOLTIP_BORDER;
import static com.binaris.wizardry.client.gui.ArcaneWorkbenchScreen.TOOLTIP_WIDTH;

public class TooltipElementUpgrades extends TooltipElement {
    private static final int ITEM_SIZE = 16;
    private static final int ITEM_SPACING = 2;
    public final ArcaneWorkbenchScreen screen;

    public TooltipElementUpgrades(ArcaneWorkbenchScreen screen, int spaceAfter) {
        super(spaceAfter);
        this.screen = screen;
    }

    @Override
    protected boolean isVisible(ItemStack stack) {
        return stack.getItem() instanceof WandItem wand && wand.getTotalUpgrades(stack) > 0;
    }

    @Override
    protected int getHeight(ItemStack stack) {
        if (!(stack.getItem() instanceof WandItem wand)) return 0;
        int total = wand.getTotalUpgrades(stack);
        int rows = 1 + (total * (ITEM_SIZE + ITEM_SPACING) - ITEM_SPACING) / (TOOLTIP_WIDTH - TOOLTIP_BORDER * 2);
        return rows * (ITEM_SIZE + ITEM_SPACING) - ITEM_SPACING;
    }

    @Override
    protected void drawBackground(GuiGraphics guiGraphics, int x, int y, ItemStack stack, float partialTicks, int mouseX, int mouseY) {
        if (!(stack.getItem() instanceof WandItem wand)) return;

        int x1 = 0;
        for (String upgradeId : WandUpgrades.ALL_UPGRADE_IDS) {
            int level = wand.getUpgradeLevel(stack, upgradeId);
            if (level > 0) {
                ResourceLocation itemLoc = ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, upgradeId + "_upgrade");
                Item item = BuiltInRegistries.ITEM.get(itemLoc);
                ItemStack itemStack = (item != null && item != Items.AIR) ? new ItemStack(item, level) : ItemStack.EMPTY;

                if (!itemStack.isEmpty()) {
                    guiGraphics.renderFakeItem(itemStack, x + x1, y);
                    guiGraphics.renderItemDecorations(Minecraft.getInstance().font, itemStack, x + x1, y);
                } else {
                    String shortLabel = upgradeId.substring(0, Math.min(3, upgradeId.length())) + ":" + level;
                    guiGraphics.drawString(Minecraft.getInstance().font, shortLabel, x + x1, y + 4, 0xCCCCCC, false);
                }

                x1 += ITEM_SIZE + ITEM_SPACING;
                if (x1 + ITEM_SIZE > TOOLTIP_WIDTH - TOOLTIP_BORDER * 2) {
                    x1 = 0;
                    y += ITEM_SIZE + ITEM_SPACING;
                }
            }
        }
    }

    @Override
    protected void drawForeground(GuiGraphics guiGraphics, int x, int y, ItemStack stack, int mouseX, int mouseY) {
    }
}
