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

public class HealSpell extends Spell {

    public HealSpell() {
        super(Elements.HEALING, SpellTiers.NOVICE, 5, 15, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        if (caster.getHealth() >= caster.getMaxHealth()) return false;

        caster.heal(6.0f);
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_HEAL.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        if (level.isClientSide) {
            for (int i = 0; i < 8; i++) {
                level.addParticle(ParticleTypes.HEART, caster.getX() + (level.random.nextDouble() - 0.5) * 0.8,
                        caster.getY() + 1.0 + level.random.nextDouble() * 0.5,
                        caster.getZ() + (level.random.nextDouble() - 0.5) * 0.8, 0, 0.1, 0);
            }
        }
        return true;
    }
}
