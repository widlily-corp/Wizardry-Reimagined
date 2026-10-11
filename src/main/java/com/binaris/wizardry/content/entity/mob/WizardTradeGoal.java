package com.binaris.wizardry.content.entity.mob;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class WizardTradeGoal extends Goal {
    private final WizardEntity wizard;

    public WizardTradeGoal(WizardEntity wizard) {
        this.wizard = wizard;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.wizard.isAlive() || this.wizard.isInWater() || !this.wizard.onGround() || this.wizard.hurtMarked) {
            return false;
        }
        Player player = this.wizard.getTradingPlayer();
        if (player == null) return false;
        return this.wizard.distanceToSqr(player) <= 16.0D;
    }

    @Override
    public void start() {
        this.wizard.getNavigation().stop();
    }
}
