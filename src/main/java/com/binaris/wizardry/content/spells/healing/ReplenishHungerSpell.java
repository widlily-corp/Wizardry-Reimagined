package com.binaris.wizardry.content.spells.healing;

import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ReplenishHungerSpell extends Spell {

    public ReplenishHungerSpell() {
        super(Elements.HEALING, SpellTiers.NOVICE, 5, 20, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        if (player.getFoodData().getFoodLevel() >= 20) return false;

        player.getFoodData().eat(6, 5.0f);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), EBSounds.SPELL_REPLENISH_HUNGER.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        if (level.isClientSide) {
            for (int i = 0; i < 10; i++) {
                level.addParticle(ParticleTypes.HAPPY_VILLAGER,
                        player.getX() + (level.random.nextDouble() - 0.5) * 0.8,
                        player.getY() + 0.8 + level.random.nextDouble() * 0.4,
                        player.getZ() + (level.random.nextDouble() - 0.5) * 0.8, 0, 0.1, 0);
            }
        }
        return true;
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        if (caster instanceof Player player) {
            return cast(level, player);
        }
        return false;
    }
}
