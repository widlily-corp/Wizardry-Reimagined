package com.binaris.wizardry.registry;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public final class NoneSpell extends Spell {
    public NoneSpell() {
        super(Elements.MAGIC, SpellTiers.NOVICE, 0, 0, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return false;
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        return false;
    }

    @Override
    public boolean requiresPacket() {
        return false;
    }
}
