package com.binaris.wizardry.content.item.bauble;

import com.binaris.wizardry.content.item.armor.WizardArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class AmuletOfArcaneDefenseItem extends AmuletItem {
    public static final int RECHARGE_INTERVAL = 100;

    public AmuletOfArcaneDefenseItem(Rarity rarity) {
        super(rarity);
    }

    @Override
    public void onWornTick(ItemStack stack, Player player, Level level) {
        if (player.tickCount % RECHARGE_INTERVAL == 0) {
            for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
                ItemStack armorStack = player.getItemBySlot(slot);
                if (!armorStack.isEmpty() && armorStack.getItem() instanceof WizardArmorItem armor && !armor.isManaFull(armorStack)) {
                    armor.rechargeMana(armorStack, 1);
                }
            }
        }
    }
}
