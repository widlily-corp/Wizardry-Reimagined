package com.binaris.wizardry.content.item.bauble;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class AmuletOfWitherImmunityItem extends AmuletItem {
    public AmuletOfWitherImmunityItem(Rarity rarity) {
        super(rarity);
    }

    @Override
    public void onWornTick(ItemStack stack, Player player, Level level) {
        if (player.hasEffect(MobEffects.WITHER)) {
            player.removeEffect(MobEffects.WITHER);
        }
    }
}
