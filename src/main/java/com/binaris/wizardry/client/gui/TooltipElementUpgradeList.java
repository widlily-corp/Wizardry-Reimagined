package com.binaris.wizardry.client.gui;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.item.wand.WandItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

public class TooltipElementUpgradeList extends TooltipElementText {
    public TooltipElementUpgradeList(ArcaneWorkbenchScreen screen, int spaceAfter) {
        super(null, Style.EMPTY.withColor(ChatFormatting.WHITE), spaceAfter, new TooltipElementUpgrades(screen, 0));
    }

    @Override
    protected Component getText(ItemStack stack) {
        return Component.translatable("container." + WizardryMod.MODID + ".arcane_workbench.upgrades");
    }

    @Override
    protected int getHeight(ItemStack stack) {
        return super.getHeight(stack) + ArcaneWorkbenchScreen.LINE_SPACING_NARROW;
    }

    @Override
    protected boolean isVisible(ItemStack stack) {
        return stack.getItem() instanceof WandItem wand && wand.getTotalUpgrades(stack) > 0;
    }
}
