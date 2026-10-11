package com.binaris.wizardry.content.item.upgrade;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WandUpgradeItem extends Item {
    private final String upgradeId;

    public WandUpgradeItem(String upgradeId, Properties properties) {
        super(properties.stacksTo(16).rarity(Rarity.UNCOMMON));
        this.upgradeId = upgradeId;
    }

    public WandUpgradeItem(String upgradeId) {
        this(upgradeId, new Properties());
    }

    public String getUpgradeId() {
        return upgradeId;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("item.ebwizardry." + upgradeId + ".desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.ebwizardry.wand_upgrade.generic.desc").withStyle(ChatFormatting.DARK_GRAY));
    }
}
