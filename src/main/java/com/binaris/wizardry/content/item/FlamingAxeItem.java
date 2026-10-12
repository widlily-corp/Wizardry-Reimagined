package com.binaris.wizardry.content.item;

import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

public class FlamingAxeItem extends AxeItem {

    public FlamingAxeItem() {
        super(MagicItemTier.TIER, new Properties().durability(12000).rarity(Rarity.UNCOMMON));
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity entity) {
        int duration = EBSpells.FLAMING_AXE != null && EBSpells.FLAMING_AXE.hasProperty(DefaultProperties.EFFECT_DURATION)
                ? EBSpells.FLAMING_AXE.property(DefaultProperties.EFFECT_DURATION) : 10;
        target.igniteForSeconds(duration);
        return super.hurtEnemy(stack, target, entity);
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack stack, @NotNull ItemStack stack1) {
        return false;
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return false;
    }
}
