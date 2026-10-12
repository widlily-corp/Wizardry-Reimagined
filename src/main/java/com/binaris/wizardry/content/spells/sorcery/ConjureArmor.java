package com.binaris.wizardry.content.spells.sorcery;

import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.ConjureItemSpell;
import com.binaris.wizardry.registry.EBItems;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.Map;

public class ConjureArmor extends ConjureItemSpell {
    private static final Map<EquipmentSlot, Item> SPECTRAL_ARMOUR_MAP = ImmutableMap.of(
            EquipmentSlot.HEAD, EBItems.SPECTRAL_HELMET.get(),
            EquipmentSlot.CHEST, EBItems.SPECTRAL_CHESTPLATE.get(),
            EquipmentSlot.LEGS, EBItems.SPECTRAL_LEGGINGS.get(),
            EquipmentSlot.FEET, EBItems.SPECTRAL_BOOTS.get());

    public ConjureArmor() {
        super(EBItems.SPECTRAL_HELMET.get());
    }

    @Override
    protected boolean conjureItem(PlayerCastContext ctx) {
        boolean flag = false;

        for (EquipmentSlot slot : EntityUtil.ARMOR_SLOTS) {
            if (!ctx.caster().getItemBySlot(slot).isEmpty()) {
                continue;
            }

            ItemStack armor = new ItemStack(SPECTRAL_ARMOUR_MAP.get(slot));
            armor = addItemExtras(ctx, armor);

            int duration = hasProperty(DefaultProperties.ITEM_LIFETIME) ? property(DefaultProperties.ITEM_LIFETIME) : 1800;
            float durationMultiplier = ctx.modifiers().getFactor(SpellModifiers.DURATION);
            long currentGameTime = ctx.world().getGameTime();
            long expireTime = currentGameTime + (long) (duration * durationMultiplier);

            CustomData.update(DataComponents.CUSTOM_DATA, armor, tag -> {
                tag.putBoolean("ebwizardry:conjured", true);
                tag.putLong("ebwizardry:expire_time", expireTime);
                tag.putInt("ebwizardry:duration", (int) (duration * durationMultiplier));
            });

            ctx.caster().setItemSlot(slot, armor);
            flag = true;
        }

        return flag;
    }
}
