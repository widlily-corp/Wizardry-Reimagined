package com.binaris.wizardry.content.item.bauble;

import com.binaris.wizardry.content.item.wand.IManaItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class RingOfCondensationItem extends RingItem {
    public static final int RECHARGE_INTERVAL = 150;

    public RingOfCondensationItem(Rarity rarity) {
        super(rarity);
    }

    @Override
    public void onWornTick(ItemStack stack, Player player, Level level) {
        if (player.tickCount % RECHARGE_INTERVAL == 0) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack itemStack = player.getInventory().getItem(i);
                if (!itemStack.isEmpty() && itemStack.getItem() instanceof IManaItem manaItem && !manaItem.isManaFull(itemStack)) {
                    manaItem.rechargeMana(itemStack, 1);
                }
            }
        }
    }
}
