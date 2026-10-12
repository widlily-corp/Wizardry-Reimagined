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

public class InvisibilitySpell extends Spell {

    public InvisibilitySpell() {
        super(Elements.SORCERY, SpellTiers.APPRENTICE, 15, 30, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        caster.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 900, 0));
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_INVISIBILITY.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        if (level.isClientSide) {
            for (int i = 0; i < 15; i++) {
                level.addParticle(ParticleTypes.SMOKE,
                        caster.getX() + (level.random.nextDouble() - 0.5) * 0.8,
                        caster.getY() + level.random.nextDouble() * 1.5,
                        caster.getZ() + (level.random.nextDouble() - 0.5) * 0.8, 0, 0.05, 0);
            }
        }
        return true;
    }
}
