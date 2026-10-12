package com.binaris.wizardry.content.spells.necromancy;

import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.content.spells.abstr.MinionSpell;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SummonSkeleton extends MinionSpell<AbstractSkeleton> {
    public SummonSkeleton() {
        super(l -> new Skeleton(EntityType.SKELETON, l));
    }

    @Override
    protected AbstractSkeleton createMinion(Level world, @Nullable LivingEntity caster, SpellModifiers modifiers) {
        return super.createMinion(world, caster, modifiers);
    }

    @Override
    protected void addMinionExtras(AbstractSkeleton minion, CastContext ctx, int alreadySpawned) {
        minion.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        minion.setDropChance(EquipmentSlot.MAINHAND, 0.0f);
    }
}
