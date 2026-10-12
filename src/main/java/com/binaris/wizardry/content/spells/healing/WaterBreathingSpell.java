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

public class WaterBreathingSpell extends Spell {

    public WaterBreathingSpell() {
        super(Elements.HEALING, SpellTiers.NOVICE, 5, 15, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        caster.setAirSupply(caster.getMaxAirSupply());
        caster.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 1200, 0));
        caster.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 1200, 0));

        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_WATER_BREATHING.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        if (level.isClientSide) {
            for (int i = 0; i < 10; i++) {
                level.addParticle(ParticleTypes.BUBBLE,
                        caster.getX() + (level.random.nextDouble() - 0.5) * 0.8,
                        caster.getEyeY() + (level.random.nextDouble() - 0.5) * 0.4,
                        caster.getZ() + (level.random.nextDouble() - 0.5) * 0.8, 0, 0.1, 0);
            }
        }
        return true;
    }
}
