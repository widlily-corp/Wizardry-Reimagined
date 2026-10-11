package com.binaris.wizardry.content.spells.healing;

import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class GreaterHealSpell extends Spell {

    public GreaterHealSpell() {
        super(Elements.HEALING, SpellTiers.ADVANCED, 25, 45, 5);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        caster.heal(16.0f);
        caster.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 300, 1));
        caster.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1));

        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_GREATER_HEAL.get(), SoundSource.PLAYERS, 1.2f, 1.0f);
        if (level.isClientSide) {
            for (int i = 0; i < 16; i++) {
                level.addParticle(ParticleTypes.HEART, caster.getX() + (level.random.nextDouble() - 0.5) * 1.0,
                        caster.getY() + 1.0 + level.random.nextDouble() * 0.8,
                        caster.getZ() + (level.random.nextDouble() - 0.5) * 1.0, 0, 0.15, 0);
            }
        }
        return true;
    }
}
