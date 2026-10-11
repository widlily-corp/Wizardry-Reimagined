package com.binaris.wizardry.content.item.bauble;

import com.binaris.wizardry.content.item.armor.WizardArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class AmuletOfRecoveryItem extends AmuletItem {
    public static final int HEAL_INTERVAL = 40;
    public static final int MANA_COST = 5;

    public AmuletOfRecoveryItem(Rarity rarity) {
        super(rarity);
    }

    @Override
    public void onWornTick(ItemStack stack, Player player, Level level) {
        if (player.tickCount % HEAL_INTERVAL == 0 && player.getHealth() < player.getMaxHealth() / 2.0F) {
            for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.CHEST, EquipmentSlot.HEAD, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
                ItemStack armorStack = player.getItemBySlot(slot);
                if (!armorStack.isEmpty() && armorStack.getItem() instanceof WizardArmorItem armor && armor.getMana(armorStack) >= MANA_COST) {
                    armor.consumeMana(armorStack, MANA_COST, player);
                    player.heal(1.0F);
                    break;
                }
            }
        }
    }
}
