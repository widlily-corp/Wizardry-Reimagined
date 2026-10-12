package com.binaris.wizardry.content.spells.sorcery;

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

public class DarkVisionSpell extends Spell {

    public DarkVisionSpell() {
        super(Elements.SORCERY, SpellTiers.NOVICE, 5, 15, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        caster.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1200, 0));
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_DARK_VISION.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        if (level.isClientSide) {
            for (int i = 0; i < 8; i++) {
                level.addParticle(ParticleTypes.GLOW,
                        caster.getX() + (level.random.nextDouble() - 0.5) * 0.6,
                        caster.getEyeY() + (level.random.nextDouble() - 0.5) * 0.3,
                        caster.getZ() + (level.random.nextDouble() - 0.5) * 0.6, 0, 0.02, 0);
            }
        }
        return true;
    }
}
