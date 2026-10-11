package com.binaris.wizardry.content.item.upgrade;

import com.binaris.wizardry.registry.SpellTier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class ArcaneTomeItem extends Item {
    private final Supplier<SpellTier> tierSupplier;

    public ArcaneTomeItem(Supplier<SpellTier> tierSupplier, Rarity rarity) {
        super(new Properties().stacksTo(1).rarity(rarity));
        this.tierSupplier = tierSupplier;
    }

    public SpellTier getTier() {
        return tierSupplier.get();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        SpellTier tier = getTier();
        if (tier != null) {
            tooltip.add(tier.getDescriptionFormatted());
            SpellTier prevTier = tier.previous();
            if (prevTier != null && prevTier != tier) {
                tooltip.add(Component.translatable(
                        "item.ebwizardry.arcane_tome.desc",
                        prevTier.getDescriptionFormatted().getString(),
                        tier.getDescriptionFormatted().getString()
                ).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }
}
