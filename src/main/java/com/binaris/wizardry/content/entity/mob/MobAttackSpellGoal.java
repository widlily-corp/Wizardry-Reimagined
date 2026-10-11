package com.binaris.wizardry.content.entity.mob;

import com.binaris.wizardry.registry.Spell;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class MobAttackSpellGoal extends Goal {
    private final AbstractWizardEntity wizard;
    private final float maxAttackDistance;
    private final int baseCooldown;
    private LivingEntity target;
    private int cooldown;
    private int seeTime;

    public MobAttackSpellGoal(AbstractWizardEntity wizard, float maxDistance, int baseCooldown) {
        this.wizard = wizard;
        this.maxAttackDistance = maxDistance * maxDistance;
        this.baseCooldown = baseCooldown;
        this.cooldown = 20;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.wizard.getTarget();
        if (livingEntity == null || !livingEntity.isAlive()) return false;
        this.target = livingEntity;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void stop() {
        this.target = null;
        this.seeTime = 0;
        this.cooldown = 20;
    }

    @Override
    public void tick() {
        if (this.target == null) return;

        double distanceSq = this.wizard.distanceToSqr(this.target);
        boolean hasLineOfSight = this.wizard.getSensing().hasLineOfSight(this.target);

        if (hasLineOfSight) {
            this.seeTime++;
        } else {
            this.seeTime = 0;
        }

        if (this.cooldown > 0) {
            this.cooldown--;
            return;
        }

        if (distanceSq > (double) this.maxAttackDistance || !hasLineOfSight || this.seeTime < 3) {
            this.cooldown = 10;
            return;
        }

        List<Spell> spells = new ArrayList<>(this.wizard.getSpells());
        if (!spells.isEmpty() && !this.wizard.level().isClientSide) {
            Spell spell = spells.get(this.wizard.getRandom().nextInt(spells.size()));
            if (spell != null) {
                this.wizard.swing(InteractionHand.MAIN_HAND);
                spell.cast(this.wizard.level(), this.wizard);
                this.cooldown = this.baseCooldown + spell.getCooldown();
                return;
            }
        }

        this.cooldown = this.baseCooldown;
    }
}
