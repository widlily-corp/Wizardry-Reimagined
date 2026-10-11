package com.binaris.wizardry.content.item.bauble;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class AmuletOfGlideItem extends AmuletItem {
    public AmuletOfGlideItem(Rarity rarity) {
        super(rarity);
    }

    @Override
    public void onWornTick(ItemStack stack, Player player, Level level) {
        if (player.fallDistance > 3.0F && player.getDeltaMovement().y < -0.1D) {
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 60, 0, false, false, true));
        }
    }
}
