package com.binaris.wizardry.content.item;

import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBMobEffects;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

public class FrostAxeItem extends AxeItem {

    public FrostAxeItem() {
        super(MagicItemTier.TIER, new Properties().durability(12000).rarity(Rarity.UNCOMMON));
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity entity) {
        if (!entity.level().isClientSide && EBMobEffects.FROST != null && EBMobEffects.FROST.get() != null) {
            int duration = EBSpells.FROST_AXE != null && EBSpells.FROST_AXE.hasProperty(DefaultProperties.EFFECT_DURATION)
                    ? EBSpells.FROST_AXE.property(DefaultProperties.EFFECT_DURATION) : 100;
            int strength = EBSpells.FROST_AXE != null && EBSpells.FROST_AXE.hasProperty(DefaultProperties.EFFECT_STRENGTH)
                    ? EBSpells.FROST_AXE.property(DefaultProperties.EFFECT_STRENGTH) : 1;
            target.addEffect(new MobEffectInstance(EBMobEffects.FROST, duration, strength));
        }
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
