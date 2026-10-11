package com.binaris.wizardry.content.entity.mob;

import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;

public class WizardLookAtTradePlayerGoal extends LookAtPlayerGoal {
    private final WizardEntity wizard;

    public WizardLookAtTradePlayerGoal(WizardEntity wizard) {
        super(wizard, Player.class, 8.0F);
        this.wizard = wizard;
    }

    @Override
    public boolean canUse() {
        if (this.wizard.isTrading()) {
            this.lookAt = this.wizard.getTradingPlayer();
            return true;
        }
        return false;
    }
}
