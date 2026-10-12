package com.binaris.wizardry.content.spells.necromancy;

import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.MinionSpell;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class SummonSkeletonLegion extends MinionSpell<AbstractSkeleton> {
    public SummonSkeletonLegion() {
        super(level -> new Skeleton(EntityType.SKELETON, level));
    }

    @Override
    protected void addMinionExtras(AbstractSkeleton minion, CastContext ctx, int alreadySpawned) {
        if (alreadySpawned % 2 == 0) {
            minion.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
        } else {
            minion.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
        }

        minion.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
        minion.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
        minion.setDropChance(EquipmentSlot.MAINHAND, 0.0f);
        minion.setDropChance(EquipmentSlot.OFFHAND, 0.0f);
        minion.setDropChance(EquipmentSlot.HEAD, 0.0f);
        minion.setDropChance(EquipmentSlot.CHEST, 0.0f);
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.MASTER, Elements.NECROMANCY, SpellTypes.MINION, SpellAction.SUMMON, 85, 20, 300)
                .add(DefaultProperties.MINION_COUNT, 6)
                .add(DefaultProperties.MINION_LIFETIME, 600)
                .add(DefaultProperties.SUMMON_RADIUS, 4)
                .build();
    }
}
